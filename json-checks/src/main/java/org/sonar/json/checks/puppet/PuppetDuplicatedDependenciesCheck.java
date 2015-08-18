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
import com.sonar.sslr.api.AstNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  key = "puppet-duplicated-dependencies",
  name = "Duplicated dependencies should be removed from Puppet \"metadata.json\" files",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL, Tags.PUPPET})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.DATA_RELIABILITY)
@SqaleConstantRemediation("5min")
public class PuppetDuplicatedDependenciesCheck extends JSONCheck {

  @Override
  public void init() {
    subscribeTo(JSONGrammar.PAIR);
  }

  @Override
  public void visitNode(AstNode node) {
    if (PuppetCheckUtils.isMetadataJsonFile(getContext().getFile())) {
      if ("dependencies".equals(CheckUtils.getKeyNodeValue(node.getFirstChild(JSONGrammar.KEY)))) {
        List<String> dependencyList = new ArrayList();
        if (node.getFirstChild(JSONGrammar.VALUE).getFirstChild(JSONGrammar.ARRAY) == null) {
          addIssue(node, this, "The \"dependencies\" value is invalid. Define an array instead.");
        } else {
          checkDuplicatedDependencies(node, dependencyList);
        }
      }
    }
  }

  private void checkDuplicatedDependencies(AstNode node, List<String> dependencyList) {
    for (AstNode arrayValues : node.getFirstChild(JSONGrammar.VALUE).getFirstChild(JSONGrammar.ARRAY).getChildren(JSONGrammar.VALUE)) {
      for (AstNode dependencies : arrayValues.getChildren(JSONGrammar.OBJECT)) {
        for (AstNode dependency : dependencies.getChildren(JSONGrammar.PAIR)) {
          if ("name".equals(CheckUtils.getKeyNodeValue(dependency.getFirstChild(JSONGrammar.KEY)))) {
            dependencyList.add(CheckUtils.getValueNodeStringValue(dependency.getFirstChild(JSONGrammar.VALUE)));
          }
        }
      }
    }
    Set<String> duplicatedDependencies = getDuplicates(dependencyList);
    if (duplicatedDependencies.size() > 0) {
      addIssue(node, this, "Remove the duplicated dependencies: " + Joiner.on(", ").join(duplicatedDependencies) + ".");
    }
  }

  private Set<String> getDuplicates(List<String> dependencyList) {
    Set<String> duplicatedSet = new HashSet();
    Set<String> notDuplicatedSet = new HashSet();

    for (String dependency : dependencyList) {
      if (!notDuplicatedSet.add(dependency)) {
        duplicatedSet.add(dependency);
      }
    }
    return duplicatedSet;
  }

}
