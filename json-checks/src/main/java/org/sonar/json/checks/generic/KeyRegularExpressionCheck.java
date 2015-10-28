/*
 * SonarQube JSON Plugin
 * Copyright (C) 2015 David RACODON
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.json.checks.generic;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.sonar.sslr.api.AstNode;

import java.util.regex.Pattern;

import org.sonar.api.utils.SonarException;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.json.JSONCheck;
import org.sonar.json.parser.JSONGrammar;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.annotations.RuleTemplate;

@Rule(
  key = "key-regular-expression",
  name = "Regular expression on key",
  priority = Priority.MAJOR)
@RuleTemplate
@NoSqale
public class KeyRegularExpressionCheck extends JSONCheck {

  private static final String DEFAULT_REGULAR_EXPRESSION = ".*";
  private static final String DEFAULT_MESSAGE = "The regular expression matches this key.";
  private Pattern pattern;

  @RuleProperty(
    key = "regularExpression",
    description = "The regular expression",
    defaultValue = DEFAULT_REGULAR_EXPRESSION)
  private String regularExpression = DEFAULT_REGULAR_EXPRESSION;

  @RuleProperty(
    key = "message",
    description = "The issue message",
    defaultValue = DEFAULT_MESSAGE)
  private String message = DEFAULT_MESSAGE;

  @Override
  public void init() {
    subscribeTo(JSONGrammar.KEY);
    if (!Strings.isNullOrEmpty(regularExpression)) {
      try {
        this.pattern = Pattern.compile(regularExpression);
      } catch (RuntimeException e) {
        throw new SonarException("Unable to compile regular expression: " + regularExpression, e);
      }
    }
  }

  @Override
  public void visitNode(AstNode node) {
    if (pattern.matcher(node.getTokenValue()).matches()) {
      addIssue(node, this, message);
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
