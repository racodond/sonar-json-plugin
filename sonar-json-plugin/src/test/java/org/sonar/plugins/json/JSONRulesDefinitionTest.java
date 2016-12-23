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

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Rule;
import org.sonar.json.checks.CheckList;
import org.sonar.json.checks.generic.FileNameCheck;

import static org.fest.assertions.Assertions.assertThat;

public class JSONRulesDefinitionTest {

  @Test
  public void test() {
    JSONRulesDefinition rulesDefinition = new JSONRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository("json");

    assertThat(repository.name()).isEqualTo("SonarQube");
    assertThat(repository.language()).isEqualTo("json");
    assertThat(repository.rules()).hasSize(CheckList.getChecks().size());

    RulesDefinition.Rule fileNameRule = repository.rule(FileNameCheck.class.getAnnotation(Rule.class).key());
    assertThat(fileNameRule).isNotNull();
    assertThat(fileNameRule.name()).isEqualTo(FileNameCheck.class.getAnnotation(Rule.class).name());
  }

}
