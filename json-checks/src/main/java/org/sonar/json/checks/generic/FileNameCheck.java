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
import org.sonar.json.checks.Tags;
import org.sonar.plugins.json.api.tree.JsonTree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "S1578",
  name = "File names should comply with a naming convention",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
@SqaleConstantRemediation("10min")
@ActivatedByDefault
public class FileNameCheck extends DoubleDispatchVisitorCheck {

  public static final String DEFAULT = "^[A-Za-z][-_A-Za-z0-9]*\\.json$";

  @RuleProperty(
    key = "format",
    defaultValue = DEFAULT,
    description = "Regular expression that file names should match. See " + CheckUtils.LINK_TO_JAVA_REGEX_PATTERN_DOC + " for detailed regular expression syntax.")
  private String format = DEFAULT;

  @Override
  public void visitJson(JsonTree tree) {
    if (!Pattern.compile(format).matcher(getContext().getFile().getName()).matches()) {
      addFileIssue("Rename this file to match this regular expression: " + format);
    }
  }

  @Override
  public void validateParameters() {
    try {
      Pattern.compile(format);
    } catch (PatternSyntaxException exception) {
      throw new IllegalStateException(
        CheckUtils.paramsErrorMessage(
          this.getClass(),
          "format parameter \"" + format + "\" is not a valid regular expression."),
        exception);
    }
  }

  @VisibleForTesting
  public void setFormat(String format) {
    this.format = format;
  }

}
