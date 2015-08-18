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

public class PuppetRequiredKeysCheckTest {

  private PuppetRequiredKeysCheck check = new PuppetRequiredKeysCheck();

  @Test
  public void should_define_all_the_keys_and_not_raise_any_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/required-keys/no-missing-keys/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_not_define_some_keys_and_raise_an_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/required-keys/missing-keys/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).next()
      .withMessage("Add the following keys that are required: author, license.")
      .noMore();
  }

  @Test
  public void should_be_an_empty_file_and_raise_an_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/required-keys/empty-file/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).next()
      .withMessage("Add the following keys that are required: name, version, author, license, summary, source, dependencies.")
      .noMore();
  }

  @Test
  public void should_not_raise_issue_because_it_is_not_a_metadata_json_file() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/required-keys/different-name/blabla.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

}
