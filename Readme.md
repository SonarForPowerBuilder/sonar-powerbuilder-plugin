PowerBuilder-Plugin for SonarQube
=================================

Testing with local installation
-------------------------------

### Setup SonarQube

- Install the SonarQube server (https://www.sonarqube.org/downloads/)
- Start the SonarQube server (by running e.g. `bin\windows-x86-64\StartSonar.bat`)
- Create a token in the SonarQube server (e.g. under `http://localhost:9000/account/security/`)

### Build sonar-powerbuilder-plugin

- Install the Java JDK (Version of at least 1.8)
- Install Maven (https://maven.apache.org/)
- Clone this repository
- Run `mvn clean package` in the clone's root directory and copy the JAR from `target/`
  to `extensions/plugins/` directory in your SonarQube installation's directory

### Build PowerScriptAnalyzer

- Install the .NET Core 2.2 SDK (the `dotnet` command has to be available)
- Clone https://github.com/SonarForPowerBuilder/PowerScriptAnalyzer
- Run `dotnet publish -c "release"` in the clone's root directory

### Setup SonarQube Scanner

- Download the SonarQube Scanner (https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner)
- Set `conf/sonar-scanner.properties` in the scanner's directory:
  - Set sonar.host.url to your Server's URL
  - Set sonar.login to the token you generated
  - Set sonar.powerbuilder.analyzer.path to point to your PowerScriptAnalyzer.dll
    (`PowerScriptAnalyzer/bin/Release/netcoreapp2.2/publish/PowerScriptAnalyzer.dll` in the clone's directory)  
    *only works with forward slashes - /*

### Analyze PowerBuilder source code

- Checkout the PowerBuilder source code
- Open a shell in the target folder
- Replace placeholders and run `{path-to-sonar-scanner.bat} -D"sonar.projectKey={project}" -D"sonar.sources=."`

Adding new rules
----------------

- Add the new rule to `org.sonar.plugins.powerbuilder.rules.PowerScriptRulesDefinition`.
  The rule identifier must start with PB.
- Activate the rule in `org.sonar.plugins.powerbuilder.languages.PowerScriptQualityProfile` if it should be enabled in the
  default quality profile.
Everything else has to be done in the [PowerScriptAnalyzer](https://github.com/SonarForPowerBuilder/PowerScriptAnalyzer)
project.
