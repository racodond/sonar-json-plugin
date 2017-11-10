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

import org.junit.Test;
import org.sonar.json.checks.CheckTestUtils;
import org.sonar.json.checks.verifier.JSONCheckVerifier;

import static org.hamcrest.Matchers.containsString;

public class PuppetDuplicatedDependenciesCheckTest {

  @Test
  public void should_not_define_duplicated_dependencies_and_not_raise_any_issue() {
    JSONCheckVerifier.verify(
      new PuppetDuplicatedDependenciesCheck(),
      CheckTestUtils.getTestFile("puppet/dependencies/valid-dependencies/metadata.json"))
      .noMore();
  }

  @Test
  public void should_not_define_dependencies_and_not_raise_any_issue() {
    JSONCheckVerifier.verify(
      new PuppetDuplicatedDependenciesCheck(),
      CheckTestUtils.getTestFile("puppet/dependencies/no-dependencies/metadata.json"))
      .noMore();
  }

  @Test
  public void should_define_invalid_dependencies_and_raise_an_issue() {
    JSONCheckVerifier.verify(
      new PuppetDuplicatedDependenciesCheck(),
      CheckTestUtils.getTestFile("puppet/dependencies/invalid-dependencies/metadata.json"))
      .next().startAtLine(21).startAtColumn(19).endAtLine(21).endAtColumn(24).withMessage("The \"dependencies\" value is invalid. Define an array instead.")
      .noMore();
  }

  @Test
  public void should_define_duplicated_dependencies_and_raise_an_issue() {
    JSONCheckVerifier.verify(
      new PuppetDuplicatedDependenciesCheck(),
      CheckTestUtils.getTestFile("puppet/dependencies/duplicated-dependencies/metadata.json"))
      .next().startAtLine(22).startAtColumn(15).endAtLine(22).endAtColumn(34).withSecondaryLines(24).withMessageThat(containsString("Merge those duplicated"))
      .next().startAtLine(23).startAtColumn(15).endAtLine(23).endAtColumn(36).withSecondaryLines(25, 26).withMessageThat(containsString("Merge those duplicated"))
      .noMore();
  }

  @Test
  public void should_not_raise_any_issues_because_it_is_not_a_metadata_json_file() {
    JSONCheckVerifier.verify(
      new PuppetDuplicatedDependenciesCheck(),
      CheckTestUtils.getTestFile("puppet/dependencies/not-metadata-json-file/notmetadata.json"))
      .noMore();
  }

}
