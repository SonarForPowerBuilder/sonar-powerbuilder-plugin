/*
 * Rule descriptions are partially copied from the existing Java/C# rules, which is licensed under the LGPL v3.
 * SonarSource's license header follows:
 * 
 * Copyright (C) 2014-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.powerbuilder.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;

import org.sonar.plugins.powerbuilder.languages.PowerScriptLanguage;

public class PowerScriptRulesDefinition implements RulesDefinition {

    public static final String REPO_KEY = PowerScriptLanguage.KEY + "-rules";

    public static final RuleKey EMPTY_CATCH = RuleKey.of(REPO_KEY, "PBEmptyCatch");
    public static final RuleKey PUBLIC_FIELD = RuleKey.of(REPO_KEY, "PBPublicField");
    public static final RuleKey PROTECTED_FIELD = RuleKey.of(REPO_KEY, "PBProtectedField");
    public static final RuleKey TOO_MANY_ELSEIFS = RuleKey.of(REPO_KEY, "PBTooManyElseIfs");
    public static final RuleKey EXCEPTION_IN_FINALLY = RuleKey.of(REPO_KEY, "PBExceptionInFinally");
    public static final RuleKey RETURN_IN_FINALLY = RuleKey.of(REPO_KEY, "PBReturnInFinally");
    public static final RuleKey INCORRECT_LS_FUNCTION = RuleKey.of(REPO_KEY, "PBIncorrectLsFunction");
    public static final RuleKey INCORRECT_TEST_SETUP = RuleKey.of(REPO_KEY, "PBIncorrectTestSetup");
    public static final RuleKey SYNTAX_ERROR = RuleKey.of(REPO_KEY, "PBSyntaxError");
    public static final RuleKey UNUSED_LOCAL_VARIABLE = RuleKey.of(REPO_KEY, "PBUnusedLocalVariable");
    public static final RuleKey UNUSED_PRIVATE_MEMBER = RuleKey.of(REPO_KEY, "PBUnusedPrivateMember");
    public static final RuleKey LOC_CASE_CLAUSE = RuleKey.of(REPO_KEY, "PBLoCCaseClause");
    public static final RuleKey LOC_METHOD = RuleKey.of(REPO_KEY, "PBLoCMethod");
    public static final RuleKey LOC_FILE = RuleKey.of(REPO_KEY, "PBLoCFile");
    public static final RuleKey NESTING_LOOP = RuleKey.of(REPO_KEY, "PBNestingLoop");
    public static final RuleKey NESTING_TRY_CATCH = RuleKey.of(REPO_KEY, "PBNestingTryCatch");
    public static final RuleKey NESTING_CHOOSE_CASE = RuleKey.of(REPO_KEY, "PBNestingChooseCase");
    public static final RuleKey NESTING_CONTROL_FLOW = RuleKey.of(REPO_KEY, "PBNestingControlFlow");
    public static final RuleKey COMPLEX_EXPRESSION = RuleKey.of(REPO_KEY, "PBComplexExpression");
    public static final RuleKey COGNITIVE_COMPLEXITY_METHOD = RuleKey.of(REPO_KEY, "PBCognitiveComplexityMethod");
    public static final RuleKey ASSERTION_ARGUMENT_ORDER = RuleKey.of(REPO_KEY, "PBAssertionArgumentOrder");
    public static final RuleKey DYNAMIC = RuleKey.of(REPO_KEY, "PBDynamic");
    public static final RuleKey THROW_IN_DESTRUCTOR = RuleKey.of(REPO_KEY, "PBThrowInDestructor");
    public static final RuleKey TODO_TAG = RuleKey.of(REPO_KEY, "PBTodoTag");
    public static final RuleKey FIXME_TAG = RuleKey.of(REPO_KEY, "PBFixmeTag");
    public static final RuleKey COMMENTED_OUT_CODE = RuleKey.of(REPO_KEY, "PBCommentedOutCode");

    public static final RuleKey CONVENTION_FUNCTION_NAME = RuleKey.of(REPO_KEY, "PBConventionFunctionName");
    public static final RuleKey CONVENTION_EVENT_NAME = RuleKey.of(REPO_KEY, "PBConventionEventName");
    public static final RuleKey CONVENTION_VARIABLE_NAME = RuleKey.of(REPO_KEY, "PBConventionVariableName");
    public static final RuleKey LEFTOVER_INSERT_HERE = RuleKey.of(REPO_KEY, "PBLeftoverInsertHere");
    public static final RuleKey INVALID_CHARACTER_IN_COMMENT = RuleKey.of(REPO_KEY, "PBInvalidCharacterInComment");
    public static final RuleKey FUNCTION_NAME_ONLY_IN_LS_FUNCTION = RuleKey.of(REPO_KEY, "PBFunctionNameOnlyInLsFunction");
    public static final RuleKey FORBIDDEN_VARIABLE_NAME = RuleKey.of(REPO_KEY, "PBForbiddenVariableName");
    public static final RuleKey FORBIDDEN_IDENTIFIER = RuleKey.of(REPO_KEY, "PBForbiddenIdentifier");
    public static final RuleKey BUILT_IN_FUNCTION_CASING = RuleKey.of(REPO_KEY, "PBBuiltInFunctionCasing");
    public static final RuleKey KEYWORD_CASING = RuleKey.of(REPO_KEY, "PBKeywordCasing");
    public static final RuleKey LINE_LENGTH = RuleKey.of(REPO_KEY, "PBLineLength");

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPO_KEY, PowerScriptLanguage.KEY).setName(REPO_KEY);

        NewRule rule = repository.createRule(EMPTY_CATCH.rule())
                .setName("Catch blocks should not be empty")
                .setHtmlDescription("<p>Either handle the exception, log it or add a comment why it is not handled.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.BUG);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));

        rule = repository.createRule(PUBLIC_FIELD.rule())
                .setName("Fields should not have public accessibilty")
                .setHtmlDescription(
                        "<p>The encapsulation principle states that fields should be encapsulated by getters and setters.</p>"
                                + "<p>With public fields, you can't add additional behavior such as validation and expose the internal"
                                + " representation so it can't easily be changed.</p>"
                                + "<p>The rule only checks write access. PROTECTEDWRITE and PRIVATEWRITE with public read are allowed.</p>"
                                + "<p>Constant fields are ignored by this rule.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.VULNERABILITY);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("20min"));

        rule = repository.createRule(PROTECTED_FIELD.rule())
                .setName("Fields should have private accessibilty")
                .setHtmlDescription(
                        "<p>The encapsulation principle states that fields should be encapsulated by getters and setters.</p>"
                                + "<p>With protected fields, you can't add additional behavior such as validation and expose the internal"
                                + " representation so it can't easily be changed.</p>"
                                + "<p>The rule only checks write access. PRIVATEWRITE with protected read is allowed.</p>"
                                + "<p>PUBLIC fields are ignored because they are already covered by the rule PBPublicField.</p>"
                                + "<p>Constant fields are ignored by this rule.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("20min"));

        rule = repository.createRule(TOO_MANY_ELSEIFS.rule())
                .setName("IF statements should not have too many ELSEIFs")
                .setHtmlDescription("<p>The more <code>ELSEIF</code> statements an <code>IF</code> statement has, the more difficult it is to understand it.</p>"
                                + "<p>If the conditions are simple and the conditional blocks are small, use a <code>CHOOSE CASE</code> statement instead.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("1h"));
        rule.createParam("maximum")
                .setName("maximum")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("3")
                .setDescription("Maximum number of ELSEIFs");
        
        rule = repository.createRule(EXCEPTION_IN_FINALLY.rule())
                .setName("Exceptions should not be thrown in finally blocks")
                .setHtmlDescription("<p>Exceptions thrown from finally blocks override exceptions from try and catch blocks."
                                + " The contents of the overriden exceptions will be lost.</p>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));
        
        rule = repository.createRule(RETURN_IN_FINALLY.rule())
                .setName("Finally blocks should not contain return statements")
                .setHtmlDescription("<p>Returning normally from finally blocks overrides exceptions thrown from try and catch blocks."
                                + " The contents of the overriden exceptions will be lost.</p>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));

        rule = repository.createRule(INCORRECT_LS_FUNCTION.rule())
                .setName("\"ls_function\" variables should be equal to the function name")
                .setHtmlDescription("<p>Using local \"ls_function\" variables that don't match their function's name for log messages can be misleading"
                                + " when reading them.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("10min"));

        rule = repository.createRule(INCORRECT_TEST_SETUP.rule())
                .setName("Test setup functions should create an instance of the class that's being tested")
                .setHtmlDescription("<p>Test classes that don't test the class they are named after are either redundant or misleading.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("20min"));

        rule = repository.createRule(SYNTAX_ERROR.rule())
                .setName("Code should have correct syntax")
                .setHtmlDescription("<p>Either there is a syntax error in the code or the PowerScriptAnalyzer's grammer has a bug.</p>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.BUG);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("1h"));

        rule = repository.createRule(UNUSED_LOCAL_VARIABLE.rule())
                .setName("Unused local variables should be removed")
                .setHtmlDescription("<p>If a local variable is declared but not used, it is dead code and should be removed.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("10min"));

        rule = repository.createRule(UNUSED_PRIVATE_MEMBER.rule())
                .setName("Unused private members should be removed")
                .setHtmlDescription("<p>If a private member (instance variable or method) is declared but not used, it is dead code and should be"
                                + " removed.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("10min"));
            
        rule = repository.createRule(LOC_CASE_CLAUSE.rule())
                .setName("CASE clauses should not have too many lines of code")
                .setHtmlDescription("<p>The <code>CHOOSE CASE</code> statement should be used only to clearly define some new branches in the control flow."
                                + " As soon as a <code>CASE</code></p> clause contains too many statements this highly decreases the readability of the"
                                + " overall control flow statement. In such case, the content of the <code>CASE</code> clause should be extracted into"
                                + " a dedicated method.")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));
        rule.createParam("maximum")
                .setName("maximum")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("5")
                .setDescription("Maximum authorized lines of code in a CASE clause.");

        rule = repository.createRule(LOC_METHOD.rule())
                .setName("Methods and events should not have too many lines of code")
                .setHtmlDescription("<p>A method that grows too large tends to aggregate too many responsibilities."
                                + " Such methods inevitably become harder to understand and therefore harder to maintain."
                                + " Above a specific threshold, it is strongly advised to refactor into smaller methods which focus on"
                                + " well-defined tasks."
                                + " Those smaller methods will not only be easier to understand, but also probably easier to test.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));
        rule.createParam("maximum")
                .setName("maximum")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("80")
                .setDescription("Maximum authorized lines of code in a function or event.");

        rule = repository.createRule(LOC_FILE.rule())
                .setName("Files should not have too many lines of code")
                .setHtmlDescription("<p>A source file that grows too much tends to aggregate too many responsibilities"
                               + " and inevitably becomes harder to understand and therefore to maintain."
                               + " Above a specific threshold, it is strongly advised to refactor it into smaller pieces of code which"
                               + " focus on well-defined tasks."
                               + " Those smaller files will not only be easier to understand but also probably easier to test.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("3h"));
        rule.createParam("maximum")
                .setName("maximum")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("1000")
                .setDescription("Maximum authorized lines of code in a file.");

        rule = repository.createRule(NESTING_LOOP.rule())
                .setName("No more than two Loop statements should be nested")
                .setHtmlDescription("<p>Nesting more than two loop statements (<code>FOR</code>, <code>DO</code>) makes the code hard to"
                                + " read, refactor and therefore maintain.</p>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));

        rule = repository.createRule(NESTING_TRY_CATCH.rule())            
                .setName("TRY/CATCH blocks should not be nested")
                .setHtmlDescription("<p>Nesting <code>TRY</code>/<code>CATCH</code> blocks severly impacts the readability of source code"
                                + " because it makes it too difficult too understand which block will catch which expression.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));

        rule = repository.createRule(NESTING_CHOOSE_CASE.rule())
                .setName("CHOOSE CASE statements should not be nested")
                .setHtmlDescription("<p>Nested <code>CHOOSE CASE</code> structures are difficult to understand because you can easily"
                                + " confuse the cases of an inner <code>CHOOSE CASE</code> as belonging to an outer statement. Therefore"
                                + " nested <code>CHOOSE CASE</code> statements should be avoided.</p>"
                                + "<p>Specifically, you should structure your code to avoid the need for nested <code>CHOOSE CASE</code>"
                                + " statements, but if you cannot, then consider moving the inner <code>CHOOSE CASE</code> to another"
                                + " function.</p>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));

        rule = repository.createRule(NESTING_CONTROL_FLOW.rule())
                .setName("Control flow statements IF, CHOOSE CASE, FOR, DO, CATCH and FINALLY should not be nested too deeply")
                .setHtmlDescription("<p>Nested <code>IF</code>, <code>CHOOSE CASE</code>, <code>FOR</code>, <code>DO</code>,"
                            + " <code>CATCH</code> and <code>FINALLY</code> statements are key ingredients for making what's"
                            + " known as \"Spaghetti code\".</p>"
                            + "<p>Such code is hard to read, refactor and therefore maintain.</p>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("30min"));
        rule.createParam("maximum")
                .setName("maximum")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("3")
                .setDescription("Maximum allowed control flow statement nesting depth.");

        rule = repository.createRule(COMPLEX_EXPRESSION.rule())
                .setName("Expressions should not be too complex")
                .setHtmlDescription("<p>The complexity of an expression is defined by the number of <code>AND</code> and <code>OR</code>"
                                + " operators it contains.</p>"
                                + "<p>A single expression's complexity should not become too high to keep the code readable.</p>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(
            rule.debtRemediationFunctions().constantPerIssue("20min"));
        rule.createParam("maximum")
                .setName("maximum")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("4")
                .setDescription("Maximum number of allowed conditional operators in an expression.");
        
        rule = repository.createRule(COGNITIVE_COMPLEXITY_METHOD.rule())
                .setName("Cognitive Complexity of methods should not be too high")
                .setHtmlDescription("<p>Cognitive Complexity is a measure of how hard the control flow of a method is to understand."
                                + " Methods with high Cognitive Complexity will be difficult to maintain.</p>"
                                + "<h3>See</h3>"
                                + "<ul><li><a href=\"http://redirect.sonarsource.com/doc/cognitive-complexity.html\">Cognitive Complexity</a></li></ul>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("30min"));
        rule.createParam("maximum")
                .setName("maximum")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("20")
                .setDescription("The maximum authorized complexity.");
        
        rule = repository.createRule(ASSERTION_ARGUMENT_ORDER.rule())
                .setName("Assertion arguments should be passed in the correct order")
                .setHtmlDescription("<p>Assertion methods like assert equal always have the actual and expected argument in the same place.</p>"
                                + "<p>Swap them, and your test will still have the same outcome (succeed/fail when it should) but the error"
                                + " messages will be confusing.</p>"
                                + "<p>This rule raises an issue when the actual argument to specified methods is a hard-coded value and the"
                                + " expected argument is not.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("10min"));
        rule.createParam("function-whitelist")
                .setName("function-whitelist")
                .setType(RuleParamType.TEXT)
                .setDefaultValue("assertequal")
                .setDescription("Comma-separated whitelist of assertion functions with an expected and actual argument. Whitespace is ignored.");
        rule.createParam("expected-argument")
                .setName("expected-argument")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("-1")
                .setDescription("Index of the argument for the expected value. 0 is the first argument, -1 the last.");
        rule.createParam("actual-argument")
                .setName("actual-argument")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("-2")
                .setDescription("Index of the argument for the actual value. 0 is the first argument, -1 the last.");
        
        rule = repository.createRule(DYNAMIC.rule())
                .setName("Calls should not be dynamic")
                .setHtmlDescription("<p>Dynamic calls provide no compile time safety and thus should only be used if absolutely necessary."
                                + " Generally polymorphism is the better option.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("1h"));
        
        rule = repository.createRule(THROW_IN_DESTRUCTOR.rule())
                .setName("Destructors should not throw exceptions")
                .setHtmlDescription("<p>Destructors are called non-deterministically. If a destructor throws an exception, when and where"
                                + " the exception surfaces, if it can be catched at all.</p>")
                .setSeverity(Severity.CRITICAL)
                .setType(RuleType.BUG);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("30min"));
        
        rule = repository.createRule(TODO_TAG.rule())
                .setName("Track uses of \"TODO\" tags")
                .setHtmlDescription("<p><code>TODO</code> tags are commonly used to mark places where some more code is required, but"
                                + " which the developer wants to implement later.</p>"
                                + "<p>Sometimes the developer will not have the time or will simply forget to get back to that tag.</p>"
                                + "<p>This rule is meant to track those tags and to ensure that they do not go unnoticed.</p>")
                .setSeverity(Severity.INFO)
                .setType(RuleType.CODE_SMELL);
        rule.createParam("regex")
                .setName("regex")
                .setType(RuleParamType.STRING)
                .setDefaultValue("(TODO|TBD|!{3,}|#{3,})")
                .setDescription("Regular expression used to find TODO tags. Case-insensitive");
        
        rule = repository.createRule(FIXME_TAG.rule())
                .setName("Track uses of \"FIXME\" tags")
                .setHtmlDescription("<p><code>FIXME</code> tags are commonly used to mark places where a bug is suspected, but"
                                + " which the developer wants to deal with later.</p>"
                                + "<p>Sometimes the developer will not have the time or will simply forget to get back to that tag.</p>"
                                + "<p>This rule is meant to track those tags and to ensure that they do not go unnoticed.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.createParam("regex")
                .setName("regex")
                .setType(RuleParamType.STRING)
                .setDefaultValue("FIXME")
                .setDescription("Regular expression used to find FIXME tags. Case-insensitive");
        
        rule = repository.createRule(COMMENTED_OUT_CODE.rule())
                .setName("Sections of code should not be commented out")
                .setHtmlDescription("<p>Programmers should not comment out code as it bloats programs and reduces readability.</p>"
                                + "<p>Unused code should be deleted and can be retrieved from source control history if required.</p>"
                                + "<p>This rule counts every single line comment that doesn't start with a space as commented out code."
                                + " Member documentation headers are exempt from this.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("1min"));


        rule = repository.createRule(CONVENTION_FUNCTION_NAME.rule())
                .setName("Function names should comply with a naming convention")
                .setHtmlDescription("<p>Shared naming conventions allow teams to collaborate efficiently. This rule checks that all"
                                + " function names match a provided regular expression based on their visibility level.</p>"
                                + "<p>Function names are first checked against the format parameter for their"
                                + " visibility. The unmatched substring is then matched against the generic format parameter.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("20min"));
        rule.createParam("format")
                .setName("format")
                .setType(RuleParamType.STRING)
                .setDefaultValue("^[a-z0-9]+(_[a-z0-9]+)*$")
                .setDescription("Regular expression used to check the function names against.");
        rule.createParam("public-format")
                .setName("public-format")
                .setType(RuleParamType.STRING)
                .setDefaultValue("^ofa?_")
                .setDescription("Regular expression used to check public function names against.");
        rule.createParam("protected-format")
                .setName("protected-format")
                .setType(RuleParamType.STRING)
                .setDefaultValue("^of(p|a|o)_")
                .setDescription("Regular expression used to check protected funtion names against.");
        rule.createParam("private-format")
                .setName("private-format")
                .setType(RuleParamType.STRING)
                .setDefaultValue("^ofi_")
                .setDescription("Regular expression used to check private function names against.");
        rule.createParam("global-format")
                .setName("global-format")
                .setType(RuleParamType.STRING)
                .setDefaultValue("^f_")
                .setDescription("Regular expression used to check global function names against.");
        
        rule = repository.createRule(CONVENTION_EVENT_NAME.rule())
                .setName("Event names should comply with a naming convention")
                .setHtmlDescription("<p>Shared naming conventions allow teams to collaborate efficiently. This rule checks that all"
                                + " event names match a provided regular expression.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("20min"));
        rule.createParam("format")
                .setName("format")
                .setType(RuleParamType.STRING)
                .setDefaultValue("^((ue|testcase)_[a-z0-9]+(_[a-z0-9]+)*|[a-z]+)$")
                .setDescription("Regular expression used to check the event names against.");

        rule = repository.createRule(CONVENTION_VARIABLE_NAME.rule())
                .setName("Local and instance variable names should comply with a naming convention")
                .setHtmlDescription("<p>Shared naming conventions allow team to collaborate efficiently. This rule allows to check"
                                + " that variable names match a provided regular expression</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("10min"));
        rule.createParam("format")
                .setName("format")
                .setType(RuleParamType.STRING)
                .setDefaultValue("^((bb|b|c|ch|dwc|d|db|dc|ds|dt|ex|gs|i|l|n|o|r|rt|s|str|t|ui|ul|u|uo|w)a?_[a-z0-9]+(_[a-z0-9]+)*|rt)$")
                .setDescription("Regular expression used to check the names against.");
        rule.createParam("local-prefix")
                .setName("local-prefix")
                .setType(RuleParamType.STRING)
                .setDefaultValue("l")
                .setDescription("Prefix for local variables.");
        rule.createParam("instance-prefix")
                .setName("instance-prefix")
                .setType(RuleParamType.STRING)
                .setDefaultValue("i")
                .setDescription("Prefix for instance variables.");
        
        rule = repository.createRule(LEFTOVER_INSERT_HERE.rule())
                .setName("<INSERT_HERE>s left over by commenter should be replaced or removed")
                .setHtmlDescription("<p>commenter uses <code><INSERT_HERE></code> as a placeholder in comments it creates."
                                + " These placeholders should either be replaced by a useful comment or be removed.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("10min"));

        rule = repository.createRule(INVALID_CHARACTER_IN_COMMENT.rule())
                .setName("Characters like German umlauts should not be used in comments")
                .setHtmlDescription("<p>PowerBuilder saves non-ASCII characters in comments using a special syntax."
                                + " This syntax is hard to read when not viewed directly in PowerBuilder, e.g. when viewing differences.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("1min"));
        
        rule = repository.createRule(FUNCTION_NAME_ONLY_IN_LS_FUNCTION.rule())
                .setName("Function name should only be used in ls_function")
                .setHtmlDescription("<p>The name of functions should only be used in ls_function. Otherwise you don't know where to look when renaming the function.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("10min"));

        rule = repository.createRule(FORBIDDEN_VARIABLE_NAME.rule())
                .setName("Some names should not be used for variables")
                .setHtmlDescription("<p>Variables for special values like function names should by convention always be named the same."
                                + " This rule forbids names that might be used but by convention shouldn't.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("10min"));
        rule.createParam("blacklist")
                .setName("blacklist")
                .setType(RuleParamType.TEXT)
                .setDefaultValue("ls_functionsname, ls_functionname, ls_funktionsname, ls_funktionname")
                .setDescription("Comma-separated list of forbidden variable names. Whitespace is ignored.");

        rule = repository.createRule(FORBIDDEN_IDENTIFIER.rule())
                .setName("Some identifiers should not be used")
                .setHtmlDescription("<p>Some language features like global variables and global functions should not be used if possible.<p>"
                                + "<p>Because it might not be easy to completely get rid of them, this rule does not only apply to the"
                                + " definition of the identifier but also to its usages.</p>"
                                + "<p>The rule is not based on the type of the identifier but rather its name. This implies that forbidden"
                                + " identifiers are named in a recognizable way.</p>")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("30min"));
        rule.createParam("regex")
                .setName("regex")
                .setType(RuleParamType.STRING)
                .setDefaultValue("^(gv_|f_)")
                .setDescription("Identifiers matching this regular expression are forbidden.");
        
        rule = repository.createRule(BUILT_IN_FUNCTION_CASING.rule())
                .setName("Built-in functions should be written in Pascal Case")
                .setHtmlDescription("<p>Following the official convention, built-in function should always be written in Pascal Case.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("1min"));

        rule = repository.createRule(KEYWORD_CASING.rule())
                .setName("Keywords should be written in All Caps")
                .setHtmlDescription("<p>Keywords should always be written in All Caps so that their are easier to distinguish from other code.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("1min"));

        rule = repository.createRule(LINE_LENGTH.rule())
                .setName("Lines should not be too long")
                .setHtmlDescription("<p>Having to scroll horizontally makes it harder to get a quick overview and understanding of any piece of code.</p>")
                .setSeverity(Severity.MINOR)
                .setType(RuleType.CODE_SMELL);
        rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("1min"));
        rule.createParam("maximum")
                .setName("maximum")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("150")
                .setDescription("Maximum allowed line length.");
        rule.createParam("tab-width")
                .setName("tab-width")
                .setType(RuleParamType.INTEGER)
                .setDefaultValue("4")
                .setDescription("The number of characters a tab character counts as.");

        repository.done();
    }

}