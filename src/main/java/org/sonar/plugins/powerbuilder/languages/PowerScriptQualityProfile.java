package org.sonar.plugins.powerbuilder.languages;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import org.sonar.plugins.powerbuilder.rules.PowerScriptRulesDefinition;

public class PowerScriptQualityProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile profile = 
            context.createBuiltInQualityProfile("PowerScript Rules", PowerScriptLanguage.KEY);
        profile.setDefault(true);

        profile.activateRule("common-powerscript", "DuplicatedBlocks");

        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.EMPTY_CATCH.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.PUBLIC_FIELD.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.PROTECTED_FIELD.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.TOO_MANY_ELSEIFS.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.EXCEPTION_IN_FINALLY.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.RETURN_IN_FINALLY.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.INCORRECT_LS_FUNCTION.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.INCORRECT_TEST_SETUP.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.SYNTAX_ERROR.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.UNUSED_LOCAL_VARIABLE.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.UNUSED_PRIVATE_MEMBER.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.LOC_CASE_CLAUSE.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.LOC_METHOD.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.LOC_FILE.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.NESTING_LOOP.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.NESTING_TRY_CATCH.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.NESTING_CHOOSE_CASE.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.NESTING_CONTROL_FLOW.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.COMPLEX_EXPRESSION.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.COGNITIVE_COMPLEXITY_METHOD.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.ASSERTION_ARGUMENT_ORDER.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.DYNAMIC.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.THROW_IN_DESTRUCTOR.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.TODO_TAG.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.FIXME_TAG.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.COMMENTED_OUT_CODE.rule());

        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.CONVENTION_FUNCTION_NAME.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.CONVENTION_EVENT_NAME.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.CONVENTION_VARIABLE_NAME.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.LEFTOVER_INSERT_HERE.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.INVALID_CHARACTER_IN_COMMENT.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.FUNCTION_NAME_ONLY_IN_LS_FUNCTION.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.FORBIDDEN_VARIABLE_NAME.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.FORBIDDEN_IDENTIFIER.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.BUILT_IN_FUNCTION_CASING.rule());
        profile.activateRule(PowerScriptRulesDefinition.REPO_KEY, PowerScriptRulesDefinition.LINE_LENGTH.rule());

        profile.done();
    }

}