/*
 * SonarQube JSON Analyzer
 * Copyright (C) 2015-2017 David RACODON
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
package org.sonar.json.checks.puppet;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.json.checks.Tags;
import org.sonar.plugins.json.api.tree.*;
import org.sonar.plugins.json.api.visitors.issue.PreciseIssue;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "puppet-duplicated-dependencies",
  name = "Duplicated dependencies should be removed from Puppet \"metadata.json\" files",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL, Tags.PUPPET})
@SqaleConstantRemediation("5min")
public class PuppetDuplicatedDependenciesCheck extends AbstractPuppetCheck {

  @Override
  public void visitPair(PairTree pair) {
    if ("dependencies".equals(pair.key().actualText())) {
      if (!pair.value().value().is(Tree.Kind.ARRAY)) {
        addPreciseIssue(pair.value(), "The \"dependencies\" value is invalid. Define an array instead.");
      } else {
        checkDuplicatedDependencies(((ArrayTree) pair.value().value()).elements());
      }
    }
    super.visitPair(pair);
  }

  private void checkDuplicatedDependencies(List<ValueTree> dependencies) {
    Map<String, List<ValueTree>> dependenciesMap = buildDependenciesMap(dependencies);
    createIssues(dependenciesMap);
  }

  private Map<String, List<ValueTree>> buildDependenciesMap(List<ValueTree> dependencies) {
    Map<String, List<ValueTree>> dependenciesMap = new HashMap<>();
    for (ValueTree dependency : dependencies) {
      if (dependency.value().is(Tree.Kind.OBJECT)) {
        for (PairTree pair : ((ObjectTree) dependency.value()).pairs()) {
          if ("name".equals(pair.key().actualText()) && pair.value().value().is(Tree.Kind.STRING)) {
            String actualValue = ((StringTree) pair.value().value()).actualText();
            if (dependenciesMap.containsKey(actualValue)) {
              dependenciesMap.get(actualValue).add(pair.value());
            } else {
              dependenciesMap.put(actualValue, Lists.newArrayList(pair.value()));
            }
          }
        }
      }
    }
    return dependenciesMap;
  }

  private void createIssues(Map<String, List<ValueTree>> dependenciesMap) {
    for (Map.Entry<String, List<ValueTree>> entry : dependenciesMap.entrySet()) {
      if (entry.getValue().size() > 1) {
        PreciseIssue issue = addPreciseIssue(entry.getValue().get(0), "Merge those duplicated \"" + entry.getKey() + "\" dependencies.");
        for (int i = 1; i < entry.getValue().size(); i++) {
          issue.secondary(entry.getValue().get(i), "Duplicated dependency");
        }
      }
    }
  }

}
