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

public class PuppetDeprecatedKeysCheckTest {

  @Test
  public void should_find_some_deprecated_keys_and_raise_some_issues() {
    SourceFile file = JSONAstScanner.scanSingleFile(
      new File("src/test/resources/checks/puppet/deprecated-keys/metadata.json"),
      new PuppetDeprecatedKeysCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(4).withMessage("Replace this deprecated \"description\" key by the \"summary\" key.")
      .next().atLine(5).withMessage("Remove this deprecated \"types\" key.")
      .next().atLine(6).withMessage("Remove this deprecated \"checksums\" key.")
      .noMore();
  }

  @Test
  public void should_not_raise_any_issues_because_it_is_not_a_metadata_json_file() {
    SourceFile file = JSONAstScanner.scanSingleFile(
      new File("src/test/resources/checks/puppet/deprecated-keys/notmetadata.json"),
      new PuppetDeprecatedKeysCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

}
