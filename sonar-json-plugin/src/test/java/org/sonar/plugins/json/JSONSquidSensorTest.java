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
package org.sonar.plugins.json;

import com.google.common.base.Charsets;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.json.checks.CheckList;
import org.sonar.json.checks.generic.MissingNewLineAtEndOfFileCheck;
import org.sonar.json.checks.generic.TabCharacterCheck;

import java.io.File;
import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class JSONSquidSensorTest {

  private final File baseDir = new File("src/test/resources");
  private final SensorContextTester context = SensorContextTester.create(baseDir);
  private CheckFactory checkFactory = new CheckFactory(mock(ActiveRules.class));

  @Test
  public void should_create_a_valid_sensor_descriptor() {
    DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
    createJSONSquidSensor().describe(descriptor);
    assertThat(descriptor.name()).isEqualTo("JSON Squid Sensor");
    assertThat(descriptor.languages()).containsOnly("json");
    assertThat(descriptor.type()).isEqualTo(InputFile.Type.MAIN);
  }

  @Test
  public void should_execute_and_compute_valid_measures_on_UTF8_file_without_BOM() {
    String relativePath = "sampleUTF8WithBOM.json";
    inputFile(relativePath);
    createJSONSquidSensor().execute(context);
    assertMeasures("moduleKey:" + relativePath);
  }

  @Test
  public void should_execute_and_compute_valid_measures_on_UTF8_file_with_BOM() {
    String relativePath = "sample.json";
    inputFile(relativePath);
    createJSONSquidSensor().execute(context);
    assertMeasures("moduleKey:" + relativePath);
  }

  private void assertMeasures(String key) {
    assertThat(context.measure(key, CoreMetrics.NCLOC).value()).isEqualTo(6);
    assertThat(context.measure(key, CoreMetrics.STATEMENTS).value()).isEqualTo(7);
  }

  @Test
  public void should_execute_and_save_issues_on_UTF8_file_with_BOM() {
    should_execute_and_save_issues("sampleUTF8WithBOM.json");
  }

  @Test
  public void should_execute_and_save_issues_on_UTF8_file_without_BOM() {
    should_execute_and_save_issues("sample.json");
  }

  private void should_execute_and_save_issues(String fileName) {
    inputFile(fileName);

    ActiveRules activeRules = (new ActiveRulesBuilder())
      .create(RuleKey.of(CheckList.REPOSITORY_KEY, TabCharacterCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .create(RuleKey.of(CheckList.REPOSITORY_KEY, MissingNewLineAtEndOfFileCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .build();
    checkFactory = new CheckFactory(activeRules);

    createJSONSquidSensor().execute(context);

    assertThat(context.allIssues()).hasSize(2);
  }

  @Test
  public void should_raise_an_issue_because_the_parsing_error_rule_is_activated() {
    inputFile("parsingError.json");

    ActiveRules activeRules = (new ActiveRulesBuilder())
      .create(RuleKey.of(CheckList.REPOSITORY_KEY, "S2260"))
      .activate()
      .build();

    checkFactory = new CheckFactory(activeRules);

    context.setActiveRules(activeRules);
    createJSONSquidSensor().execute(context);
    Collection<Issue> issues = context.allIssues();
    assertThat(issues).hasSize(1);
    Issue issue = issues.iterator().next();
    assertThat(issue.primaryLocation().textRange().start().line()).isEqualTo(1);
  }

  @Test
  public void should_not_raise_any_issue_because_the_parsing_error_rule_is_not_activated() {
    inputFile("parsingError.json");

    ActiveRules activeRules = new ActiveRulesBuilder().build();
    checkFactory = new CheckFactory(activeRules);

    context.setActiveRules(activeRules);
    createJSONSquidSensor().execute(context);
    Collection<Issue> issues = context.allIssues();
    assertThat(issues).hasSize(0);
  }

  private JSONSquidSensor createJSONSquidSensor() {
    return new JSONSquidSensor(context.fileSystem(), checkFactory, null);
  }

  private void inputFile(String relativePath) {
    DefaultInputFile inputFile = new DefaultInputFile("moduleKey", relativePath)
      .setModuleBaseDir(baseDir.toPath())
      .setType(InputFile.Type.MAIN)
      .setLanguage(JSONLanguage.KEY);

    context.fileSystem().setEncoding(Charsets.UTF_8);
    context.fileSystem().add(inputFile);

    inputFile.initMetadata(new FileMetadata().readMetadata(inputFile.file(), Charsets.UTF_8));
  }

}
