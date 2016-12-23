/*
 * SonarQube JSON Analyzer
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
package org.sonar.plugins.json;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.json.api.CustomJSONRulesDefinition;
import org.sonar.plugins.json.api.JSONCheck;
import org.sonar.plugins.json.api.visitors.TreeVisitor;

public class JSONChecks {

  private final CheckFactory checkFactory;
  private Set<Checks<JSONCheck>> checksByRepository = Sets.newHashSet();

  private JSONChecks(CheckFactory checkFactory) {
    this.checkFactory = checkFactory;
  }

  public static JSONChecks createJSONCheck(CheckFactory checkFactory) {
    return new JSONChecks(checkFactory);
  }

  public JSONChecks addChecks(String repositoryKey, Iterable<Class> checkClass) {
    checksByRepository.add(checkFactory
      .<JSONCheck>create(repositoryKey)
      .addAnnotatedChecks(checkClass));

    return this;
  }

  public JSONChecks addCustomChecks(@Nullable CustomJSONRulesDefinition[] customRulesDefinitions) {
    if (customRulesDefinitions != null) {

      for (CustomJSONRulesDefinition rulesDefinition : customRulesDefinitions) {
        addChecks(rulesDefinition.repositoryKey(), Lists.newArrayList(rulesDefinition.checkClasses()));
      }
    }
    return this;
  }

  public List<JSONCheck> all() {
    List<JSONCheck> allVisitors = Lists.newArrayList();

    for (Checks<JSONCheck> checks : checksByRepository) {
      allVisitors.addAll(checks.all());
    }

    return allVisitors;
  }

  public List<TreeVisitor> visitorChecks() {
    List<TreeVisitor> checks = new ArrayList<>();
    for (JSONCheck check : all()) {
      if (check instanceof TreeVisitor) {
        checks.add((TreeVisitor) check);
      }
    }

    return checks;
  }

  @Nullable
  public RuleKey ruleKeyFor(JSONCheck check) {
    RuleKey ruleKey;

    for (Checks<JSONCheck> checks : checksByRepository) {
      ruleKey = checks.ruleKey(check);

      if (ruleKey != null) {
        return ruleKey;
      }
    }
    return null;
  }

}
