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

import java.io.File;

import org.junit.Test;
import org.sonar.json.JSONAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import static org.hamcrest.Matchers.containsString;

public class PuppetDuplicatedDependenciesCheckTest {

  @Test
  public void should_not_define_duplicated_dependencies_and_not_raise_any_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(
      new File("src/test/resources/checks/puppet/dependencies/valid-dependencies/metadata.json"),
      new PuppetDuplicatedDependenciesCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_not_define_dependencies_and_not_raise_any_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(
      new File("src/test/resources/checks/puppet/dependencies/no-dependencies/metadata.json"),
      new PuppetDuplicatedDependenciesCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_define_invalid_dependencies_and_raise_an_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(
      new File("src/test/resources/checks/puppet/dependencies/invalid-dependencies/metadata.json"),
      new PuppetDuplicatedDependenciesCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(21).withMessage("The \"dependencies\" value is invalid. Define an array instead.").
      noMore();
  }

  @Test
  public void should_define_duplicated_dependencies_and_raise_an_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(
      new File("src/test/resources/checks/puppet/dependencies/duplicated-dependencies/metadata.json"),
      new PuppetDuplicatedDependenciesCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(21).withMessageThat(containsString("Remove the duplicated dependencies:"))
      .noMore();
  }

  @Test
  public void should_not_raise_any_issues_because_it_is_not_a_metadata_json_file() {
    SourceFile file = JSONAstScanner.scanSingleFile(
      new File("src/test/resources/checks/puppet/dependencies/not-metadata-json-file/notmetadata.json"),
      new PuppetDuplicatedDependenciesCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }
}
