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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.AstNode;

import java.util.ArrayList;
import java.util.List;

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
  key = "puppet-required-keys",
  name = "Puppet \"metadata.json\" files should define all the required keys",
  priority = Priority.MAJOR,
  tags = {Tags.CONVENTION, Tags.PUPPET})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.DATA_RELIABILITY)
@SqaleConstantRemediation("10min")
public class PuppetRequiredKeysCheck extends JSONCheck {

  private static final List<String> requiredKeys = ImmutableList.of("name", "version", "author", "license", "summary", "source", "dependencies");
  private List definedKeys = new ArrayList();
  private List missingKeys = new ArrayList();

  @Override
  public void init() {
    subscribeTo(JSONGrammar.JSON);
  }

  @Override
  public void visitNode(AstNode node) {
    if (PuppetCheckUtils.isMetadataJsonFile(getContext().getFile()) && node.getFirstChild(JSONGrammar.OBJECT) != null) {
      for (AstNode pairNode : node.getFirstChild(JSONGrammar.OBJECT).getChildren(JSONGrammar.PAIR)) {
        definedKeys.add(CheckUtils.getKeyNodeValue(pairNode.getFirstChild(JSONGrammar.KEY)));
      }
    }
  }

  @Override
  public void leaveFile(AstNode node) {
    if (PuppetCheckUtils.isMetadataJsonFile(getContext().getFile())) {
      for (String requiredKey : requiredKeys) {
        if (!definedKeys.contains(requiredKey)) {
          missingKeys.add(requiredKey);
        }
      }
      if (!missingKeys.isEmpty()) {
        addIssueOnFile(this, "Add the following keys that are required: " + Joiner.on(", ").join(missingKeys) + ".");
      }
      definedKeys.clear();
      missingKeys.clear();
    }
  }
}
