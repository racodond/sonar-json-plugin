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
package org.sonar.json.checks.puppet;

import com.sonar.sslr.api.AstNode;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.json.JSONCheck;
import org.sonar.json.checks.CheckUtils;
import org.sonar.json.checks.Tags;
import org.sonar.json.parser.JSONGrammar;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

@Rule(
  key = "puppet-deprecated-keys",
  name = "Deprecated keys should be removed from Puppet \"metadata.json\" files",
  priority = Priority.MAJOR,
  tags = {Tags.OBSOLETE, Tags.PUPPET})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LANGUAGE_RELATED_PORTABILITY)
@SqaleConstantRemediation("5min")
public class PuppetDeprecatedKeysCheck extends JSONCheck {

  @Override
  public void init() {
    subscribeTo(JSONGrammar.KEY);
  }

  @Override
  public void visitNode(AstNode node) {
    if (PuppetCheckUtils.isMetadataJsonFile(getContext().getFile())) {
      if ("types".equals(CheckUtils.getKeyNodeValue(node))) {
        addIssue(node, this, "Remove this deprecated \"types\" key.");
      } else if ("checksums".equals(CheckUtils.getKeyNodeValue(node))) {
        addIssue(node, this, "Remove this deprecated \"checksums\" key.");
      } else if ("description".equals(CheckUtils.getKeyNodeValue(node))) {
        addIssue(node, this, "Replace this deprecated \"description\" key by the \"summary\" key.");
      }
    }
  }

}
