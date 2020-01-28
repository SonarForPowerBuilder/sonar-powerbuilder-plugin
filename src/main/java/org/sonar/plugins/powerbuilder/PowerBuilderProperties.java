package org.sonar.plugins.powerbuilder;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

public class PowerBuilderProperties {

    public static final String ANALYZER_PATH_KEY = "sonar.powerbuilder.analyzer.path";
    public static final String ANALYZER_PATH_DEFAULT_VALUE = 
            "~/git/PowerScriptAnalyzer/PowerScriptAnalyzer/bin/Release/netcoreapp2.2/PowerScriptAnalyzer.dll";

    private PowerBuilderProperties() {
    }

    public static List<PropertyDefinition> getProperties() {
        return Arrays.asList(
                PropertyDefinition.builder(ANALYZER_PATH_KEY).defaultValue(ANALYZER_PATH_DEFAULT_VALUE)
                        .name("PowerScriptAnalyzer path")
                        .description("Absolute path to the PowerScriptAnalyzer.dll on the machine that runs the scanner.")
                        .onQualifiers(Qualifiers.PROJECT).build());
    }
}