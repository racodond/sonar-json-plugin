/*
 * SonarQube JSON Plugin
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

public class PuppetVersionCheckTest {

  @Test
  public void should_define_some_invalid_versions_and_raise_some_issues() {
    String message = "Define the version as a semantic version on 3 digits separated by dots: ^\\d+\\.\\d+\\.\\d+$";

    JSONCheckVerifier.verify(
        new PuppetVersionCheck(),
        CheckTestUtils.getTestFile("puppet/version/metadata.json"))
      .next().startAtLine(4).startAtColumn(14).endAtLine(4).endAtColumn(19).withMessage(message)
      .next().startAtLine(5).startAtColumn(14).endAtLine(5).endAtColumn(26).withMessage(message)
      .next().startAtLine(6).startAtColumn(14).endAtLine(6).endAtColumn(19).withMessage(message)
      .next().startAtLine(7).startAtColumn(14).endAtLine(7).endAtColumn(23).withMessage(message)
      .noMore();
  }

  @Test
  public void should_not_raise_any_issues_because_it_is_not_a_metadata_json_file() {
    JSONCheckVerifier.verify(
        new PuppetVersionCheck(),
        CheckTestUtils.getTestFile("puppet/version/notmetadata.json"))
      .noMore();
  }

}
