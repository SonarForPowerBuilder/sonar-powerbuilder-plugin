package org.sonar.plugins.powerbuilder.languages;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

public class PowerScriptLanguage extends AbstractLanguage {

    public static final String KEY = "powerscript";

    public PowerScriptLanguage(Configuration configuration) {
        super(KEY, "PowerScript");
    }

    @Override
    public String[] getFileSuffixes() {
        return new String[] { "srm", "sru", "srw", "srf", "srs" };
    }

}
