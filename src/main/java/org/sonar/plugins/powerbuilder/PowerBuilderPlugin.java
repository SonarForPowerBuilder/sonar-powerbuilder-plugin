package org.sonar.plugins.powerbuilder;

import org.sonar.api.Plugin;

import org.sonar.plugins.powerbuilder.languages.PowerScriptLanguage;
import org.sonar.plugins.powerbuilder.languages.PowerScriptQualityProfile;
import org.sonar.plugins.powerbuilder.rules.PowerScriptIssuesLoaderSensor;
import org.sonar.plugins.powerbuilder.rules.PowerScriptRulesDefinition;

public class PowerBuilderPlugin implements Plugin {

    @Override
    public void define(Context context) {
        context.addExtensions(PowerScriptLanguage.class, PowerScriptRulesDefinition.class,
                PowerScriptQualityProfile.class, PowerScriptIssuesLoaderSensor.class);
        context.addExtension(PowerBuilderProperties.getProperties());
    }

}
