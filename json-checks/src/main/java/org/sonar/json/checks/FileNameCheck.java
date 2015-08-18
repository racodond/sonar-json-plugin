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
package org.sonar.json.checks;

import com.google.common.annotations.VisibleForTesting;
import com.sonar.sslr.api.AstNode;

import java.util.regex.Pattern;
import javax.annotation.Nullable;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.json.JSONCheck;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

@Rule(
  key = "S1578",
  name = "File names should comply with a naming convention",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("10min")
@ActivatedByDefault
public class FileNameCheck extends JSONCheck {

  public static final String DEFAULT = "^[A-Za-z][-_A-Za-z0-9]*\\.json$";

  @RuleProperty(
    key = "format",
    defaultValue = DEFAULT,
    description = "Regular expression that file names should match")
  private String format = DEFAULT;

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (!Pattern.compile(format).matcher(getContext().getFile().getName()).matches()) {
      addIssueOnFile(this, "Rename this file to match this regular expression: " + format);
    }
  }

  @VisibleForTesting
  public void setFormat(String format) {
    this.format = format;
  }

}
