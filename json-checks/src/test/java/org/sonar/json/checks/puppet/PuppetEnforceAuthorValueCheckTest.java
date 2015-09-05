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

public class PuppetEnforceAuthorValueCheckTest {

  @Test
  public void should_match_the_default_required_value_and_not_raise_any_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/author/default/metadata.json"), new PuppetEnforceAuthorValueCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_match_the_required_custom_value_and_not_raise_any_issue() {
    PuppetEnforceAuthorValueCheck check = new PuppetEnforceAuthorValueCheck();
    check.setAuthor("Pat");
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/author/custom/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_not_match_the_required_default_value_and_raise_an_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/author/custom/metadata.json"), new PuppetEnforceAuthorValueCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(4).withMessage("Set the author to \"John Doe\".").
      noMore();
  }

  @Test
  public void should_not_match_the_required_custom_value_and_raise_an_issue() {
    PuppetEnforceAuthorValueCheck check = new PuppetEnforceAuthorValueCheck();
    check.setAuthor("Smith");
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/author/custom/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(4).withMessage("Set the author to \"Smith\".").
      noMore();
  }

  @Test
  public void should_not_be_triggered_when_no_author_is_defined() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/required-keys/missing-keys/metadata.json"), new PuppetEnforceAuthorValueCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_not_raise_any_issues_because_it_is_not_a_metadata_json_file() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/author/not-metadata-json-file/notmetadata.json"),
      new PuppetEnforceAuthorValueCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

}
