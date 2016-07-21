/*
 * SonarQube JSON Plugin
 * Copyright (C) 2015-2016 David RACODON
 * david.racodon@gmail.com
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
package org.sonar.json.checks.generic;

import com.google.common.annotations.VisibleForTesting;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.json.checks.CheckUtils;
import org.sonar.plugins.json.api.tree.KeyTree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.annotations.RuleTemplate;

@Rule(
  key = "key-regular-expression",
  name = "Regular expression on key",
  priority = Priority.MAJOR)
@RuleTemplate
@NoSqale
public class KeyRegularExpressionCheck extends DoubleDispatchVisitorCheck {

  private static final String DEFAULT_REGULAR_EXPRESSION = ".*";
  private static final String DEFAULT_MESSAGE = "The regular expression matches this key.";

  @RuleProperty(
    key = "regularExpression",
    description = "The regular expression. See " + CheckUtils.LINK_TO_JAVA_REGEX_PATTERN_DOC + " for detailed regular expression syntax.",
    defaultValue = DEFAULT_REGULAR_EXPRESSION)
  private String regularExpression = DEFAULT_REGULAR_EXPRESSION;

  @RuleProperty(
    key = "message",
    description = "The issue message",
    defaultValue = DEFAULT_MESSAGE)
  private String message = DEFAULT_MESSAGE;

  @Override
  public void visitKey(KeyTree tree) {
    if (Pattern.compile(regularExpression).matcher(tree.actualText()).matches()) {
      addPreciseIssue(tree, message);
    }
    super.visitKey(tree);
  }

  @Override
  public void validateParameters() {
    try {
      Pattern.compile(regularExpression);
    } catch (PatternSyntaxException exception) {
      throw new IllegalStateException(
        CheckUtils.paramsErrorMessage(
          this.getClass(),
          "regularExpression parameter \"" + regularExpression + "\" is not a valid regular expression."),
        exception);
    }
  }

  @VisibleForTesting
  public void setRegularExpression(String regularExpression) {
    this.regularExpression = regularExpression;
  }

  @VisibleForTesting
  public void setMessage(String message) {
    this.message = message;
  }

}
