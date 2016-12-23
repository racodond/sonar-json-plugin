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
package org.sonar.json.checks.puppet;

import org.junit.Test;
import org.sonar.json.checks.CheckTestUtils;
import org.sonar.json.checks.verifier.JSONCheckVerifier;

public class PuppetDeprecatedKeysCheckTest {

  @Test
  public void should_find_some_deprecated_keys_and_raise_some_issues() {
    JSONCheckVerifier.verify(new PuppetDeprecatedKeysCheck(), CheckTestUtils.getTestFile("puppet/deprecated-keys/metadata.json"))
      .next().startAtLine(4).startAtColumn(3).endAtLine(4).endAtColumn(16).withMessage("Replace this deprecated \"description\" key by the \"summary\" key.")
      .next().startAtLine(5).startAtColumn(3).endAtLine(5).endAtColumn(10).withMessage("Remove this deprecated \"types\" key.")
      .next().startAtLine(6).startAtColumn(3).endAtLine(6).endAtColumn(14).withMessage("Remove this deprecated \"checksums\" key.")
      .noMore();
  }

  @Test
  public void should_not_raise_any_issues_because_it_is_not_a_metadata_json_file() {
    JSONCheckVerifier.verify(new PuppetDeprecatedKeysCheck(), CheckTestUtils.getTestFile("puppet/deprecated-keys/notmetadata.json"))
      .noMore();
  }

}
