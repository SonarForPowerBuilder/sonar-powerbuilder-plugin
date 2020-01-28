package org.sonar.plugins.powerbuilder.rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.measure.Metric;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.symbol.NewSymbol;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;
import org.sonar.api.config.Configuration;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import org.sonar.plugins.powerbuilder.PowerBuilderProperties;
import org.sonar.plugins.powerbuilder.languages.PowerScriptLanguage;

public class PowerScriptIssuesLoaderSensor implements Sensor {

    private static final Logger LOG = Loggers.get(PowerScriptIssuesLoaderSensor.class);

    private static final int LOG_PROGRESS_INTERVAL_IN_MS = 10000;

    private final Configuration config;
    private final FileLinesContextFactory fileLinesContextFactory;
    private final NoSonarFilter noSonarFilter;
    private final ObjectMapper objectMapper;

    public PowerScriptIssuesLoaderSensor(Configuration config, FileLinesContextFactory fileLinesContextFactory,
            NoSonarFilter noSonarFilter) {
        this.config = config;
        this.fileLinesContextFactory = fileLinesContextFactory;
        this.noSonarFilter = noSonarFilter;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("PowerScript Issues Loader Sensor")
                .onlyOnLanguage(PowerScriptLanguage.KEY)
                .createIssuesForRuleRepository(PowerScriptRulesDefinition.REPO_KEY);
    }

    @Override
    public void execute(SensorContext context) {
        List<InputFile> files = getInputFiles(context.fileSystem());
        String activeRulesJsonFile = saveActiveRulesJson(context);
        int totalFiles = files.size();
        LOG.info(totalFiles + " total source files to be analyzed");
        long lastLog = System.currentTimeMillis();
        int i;
        for (i = 0; i < totalFiles; i++) {
            if (System.currentTimeMillis() - lastLog >= LOG_PROGRESS_INTERVAL_IN_MS) {
                lastLog = System.currentTimeMillis();
                LOG.info(i + "/" + totalFiles + " files analyzed");
            }
            InputFile file = files.get(i);
            String results = runAnalyzer(context, file, activeRulesJsonFile);
            processResults(context, file, results);
            if (context.isCancelled()) {
                break;
            }
        }
        LOG.info(i + "/" + totalFiles + " files analyzed");
    }

    private List<InputFile> getInputFiles(FileSystem fs) {
        Iterable<InputFile> filesIterable = fs.inputFiles(fs.predicates().hasLanguage(PowerScriptLanguage.KEY));
        List<InputFile> files = new ArrayList<>();
        filesIterable.forEach(files::add);
        return files;
    }

    private String saveActiveRulesJson(SensorContext context) {
        Map<String, Map<String, String>> jsonActiveRules = context.activeRules()
                .findByRepository(PowerScriptRulesDefinition.REPO_KEY)
                .stream()
                .collect(Collectors.toMap(r -> r.ruleKey().rule().substring(2), r -> r.params()));

        try {
            File file = File.createTempFile("activeRules", ".json");
            try (FileOutputStream outputStream = new FileOutputStream(file))
            {
                objectMapper.writeValue(outputStream, jsonActiveRules);
            }
            return file.getAbsolutePath();
        } catch (JsonProcessingException e) {
            LOG.error("Could not convert active rules to JSON", e);
            return "";
        } catch (IOException e) {
            LOG.error("Could not convert active rules to JSON", e);
            return "";
        }
    }

    private String runAnalyzer(SensorContext context, InputFile file, String activeRulesJsonFile) {
        LOG.debug("Analyzing " + file.uri());
        StringBuilder resultsBuilder = new StringBuilder();
        try {
            Process process = startAnalyzer(file, activeRulesJsonFile);
            try (BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = processOutput.readLine()) != null) {
                    resultsBuilder.append(line).append("\n");
                }
            }
            process.waitFor();
        } catch (IOException e) {
            logAndSaveAnalysisError(context, file, e);
        } catch (InterruptedException e) {
            logAndSaveAnalysisError(context, file, e);
        }
        return resultsBuilder.toString();
    }

    private Process startAnalyzer(InputFile file, String activeRulesJsonFile) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("dotnet", getAnalyzerPath(),
                Paths.get(file.uri()).toString(), activeRulesJsonFile);
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private String getAnalyzerPath() {
        return config.get(PowerBuilderProperties.ANALYZER_PATH_KEY)
                .orElse(PowerBuilderProperties.ANALYZER_PATH_DEFAULT_VALUE);
    }

    private void processResults(SensorContext context, InputFile file, String results) {
        try {
            JsonResults jsonResults = objectMapper.readValue(results, JsonResults.class);
            LOG.debug(jsonResults.toString());
            if (jsonResults.Issues != null) {
                saveIssues(context, file, jsonResults.Issues);
            }
            if (jsonResults.Metrics != null) {
                saveMetrics(context, file, jsonResults.Metrics);
            }
            if (jsonResults.NoSonarLines != null) {
                saveNoSonarCommentLines(context, file, jsonResults.NoSonarLines);
            }
            if (jsonResults.SymbolTable != null) {
                saveSymbolTable(context, file, jsonResults.SymbolTable);
            }
            if (jsonResults.AnalysisErrors != null) {
                saveAnalysisErrors(context, file, jsonResults.AnalysisErrors);
            }
            if (jsonResults.CpdTokens != null) {
                saveCpdTokens(context, file, jsonResults.CpdTokens);
            }
            if (jsonResults.Highlightings != null) {
                saveHighlights(context, file, jsonResults.Highlightings);
            }
        } catch (IOException e) {
            logAndSaveAnalysisError(context, file, e);
        }
    }

    private void saveIssues(SensorContext context, InputFile file, JsonIssue issues[]) {
        for (JsonIssue issue : issues) {
            saveIssue(context, file, issue);
        }
    }

    private static void saveIssue(SensorContext context, InputFile file, JsonIssue issue) {
        JsonTextRange location = issue.Location;
        NewIssue newIssue = context.newIssue()
                .forRule(RuleKey.of(PowerScriptRulesDefinition.REPO_KEY, "PB" + issue.Rule));
        TextRange range = createTextRange(file, location);
        NewIssueLocation primaryLocation = newIssue.newLocation().on(file).at(range).message(issue.Message);
        newIssue.at(primaryLocation);
        newIssue.save();
    }

    private void saveMetrics(SensorContext context, InputFile file, JsonMetrics metrics) {
        saveMetric(context, file, CoreMetrics.CLASSES, metrics.ClassCount);
        saveMetric(context, file, CoreMetrics.STATEMENTS, metrics.StatementCount);
        saveMetric(context, file, CoreMetrics.FUNCTIONS, metrics.FunctionCount);
        saveMetric(context, file, CoreMetrics.NCLOC, metrics.LinesOfCode.length);
        saveMetric(context, file, CoreMetrics.COMMENT_LINES, metrics.CommentLines.length);
        saveMetric(context, file, CoreMetrics.COMPLEXITY, metrics.CyclomaticComplexity);
        saveMetric(context, file, CoreMetrics.COGNITIVE_COMPLEXITY, metrics.CognitiveComplexity);

        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(file);
        for (int line : metrics.LinesOfCode) {
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, 1);
        }
        fileLinesContext.save();
    }

    private static <T extends Serializable> void saveMetric(SensorContext context, InputFile file, Metric<T> metric, T value) {
        context.<T>newMeasure().on(file).forMetric(metric).withValue(value).save();
    }

    private void saveNoSonarCommentLines(SensorContext context, InputFile file, Integer[] noSonarLines) {
        noSonarFilter.noSonarInFile(file, new HashSet<>(Arrays.asList(noSonarLines)));
    }

    private void saveSymbolTable(SensorContext context, InputFile file, JsonSymbol[] symbols) {
        NewSymbolTable symbolTable = context.newSymbolTable().onFile(file);
        for (JsonSymbol symbol : symbols) {
            saveSymbol(file, symbolTable, symbol);
        }
        symbolTable.save();
    }
    
    private void saveSymbol(InputFile file, NewSymbolTable symbolTable, JsonSymbol symbol) {
        NewSymbol newSymbol = symbolTable.newSymbol(createTextRange(file, symbol.Position));
        for (JsonTextRange reference : symbol.References) {
            newSymbol.newReference(createTextRange(file, reference));
        }
    }

    private void saveAnalysisErrors(SensorContext context, InputFile file, JsonAnalysisError[] analysisErrors) {
        for (JsonAnalysisError analysisError : analysisErrors) {
            saveAnalysisError(context, file, analysisError);
        }
    }

    private static void saveAnalysisError(SensorContext context, InputFile file, JsonAnalysisError analysisError) {
        LOG.error("Analysis error in " + file.uri() + ":\n" + analysisError);
        context.newAnalysisError().onFile(file).message(analysisError.Message).save();
    }

    private static void logAndSaveAnalysisError(SensorContext context, InputFile file, Exception exception) {
        String className = exception.getClass().getSimpleName();
        LOG.error(className + " while analyzing file " + file.uri(), exception);
        context.newAnalysisError().onFile(file).message(className + ": " + exception.getMessage()).save();
    }

    private void saveCpdTokens(SensorContext context, InputFile file, JsonCpdToken[] tokens) {
        NewCpdTokens newCpdTokens = context.newCpdTokens().onFile(file);
        for (JsonCpdToken token : tokens) {
            newCpdTokens.addToken(createTextRange(file, token.Position), token.Image);
        }
        newCpdTokens.save();
    }

    private void saveHighlights(SensorContext context, InputFile file, JsonHighlighting[] highlightings) {
        NewHighlighting newHighlighting = context.newHighlighting().onFile(file);
        for (JsonHighlighting highlighting : highlightings) {
            newHighlighting.highlight(createTextRange(file, highlighting.Position), getTypeOfText(highlighting));
        }
        newHighlighting.save();
    }

    private static TypeOfText getTypeOfText(JsonHighlighting highlighting) {
        String allCaps = highlighting.TypeOfText.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(Locale.ROOT);
        return TypeOfText.valueOf(allCaps);
    }

    private static TextRange createTextRange(InputFile file, JsonTextRange location) {
        if (isEofLocation(file, location)) {
            return file.selectLine(file.lines());
        }
        return file.newRange(location.Start.Line, location.Start.LineOffset, 
                location.End.Line, location.End.LineOffset);
    }

    private static boolean isEofLocation(InputFile file, JsonTextRange location) {
        int lastLine = file.lines();
        if (location.Start.Line != lastLine || location.End.Line != lastLine) {
            return false;
        }
        if (location.End.LineOffset - location.Start.LineOffset > 1) {
            return false;
        }
        return true;
    }

    public static class JsonResults {
        public JsonIssue[] Issues;
        public JsonMetrics Metrics;
        public Integer[] NoSonarLines;
        public JsonSymbol[] SymbolTable;
        public JsonAnalysisError[] AnalysisErrors;
        public JsonCpdToken[] CpdTokens;
        public JsonHighlighting[] Highlightings;
        public long AnalysisTime;

        @Override
        public String toString() {
            return "Issues: " + (Issues == null ? 0 : Issues.length) + "\n"
                    + "Metrics: " + (Metrics == null ? "null" : Metrics.toString()) + "\n"
                    + "NoSonarLines: " + Arrays.toString(NoSonarLines) + "\n"
                    + "SymbolTable: " + (SymbolTable == null ? 0 : SymbolTable.length) + "\n"
                    + "AnalysisErrors: " + Arrays.toString(AnalysisErrors) + "\n"
                    + "CpdTokens: " + (CpdTokens == null ? 0 : CpdTokens.length) + "\n"
                    + "Highlights: " + (Highlightings == null ? 0 : Highlightings.length) + "\n"
                    + "AnalysisTime: " + AnalysisTime + "ms";
        }
    }

    public static class JsonIssue {
        public String Rule;
        public JsonTextRange Location;
        public String Message;

        @Override
        public String toString() {
            return Rule + " at " + Location.toString() + ", message: '" + Message + "'";
        }
    }

    public static class JsonTextRange {
        public JsonTextPointer Start;
        public JsonTextPointer End;

        @Override
        public String toString() {
            return Start.toString() + "-" + End.toString();
        }
    }

    public static class JsonTextPointer {
        public int Line;
        public int LineOffset;

        @Override
        public String toString() {
            return Line + ":" + LineOffset;
        }
    }

    public static class JsonMetrics {
        public int ClassCount;
        public int StatementCount;
        public int FunctionCount;
        public int[] LinesOfCode;
        public int[] CommentLines;
        public int CyclomaticComplexity;
        public int CognitiveComplexity;

        @Override
        public String toString() {
            return "ClassCount: " + ClassCount + ", StatementCount: " + StatementCount + ", FunctionCount: "
                    + FunctionCount + ", LinesOfCode.length: " + LinesOfCode.length + ", CommentLines.length: "
                    + CommentLines.length + ", CyclomaticComplexity: " + CyclomaticComplexity
                    + ", CognitiveComplexity: " + CognitiveComplexity;
        }
    }

    public static class JsonSymbol {
        public JsonTextRange Position;
        public JsonTextRange[] References;

        @Override
        public String toString() {
            return Position.toString() + " (" + References.length + " references)";
        }
    }

    public static class JsonAnalysisError {
        public String Message;
        public String StackTrace;

        @Override
        public String toString() {
            return Message + "\n" + StackTrace;
        }
    }

    public static class JsonCpdToken {
        public JsonTextRange Position;
        public String Image;
    }

    public static class JsonHighlighting {
        public JsonTextRange Position;
        public String TypeOfText;
    }
}