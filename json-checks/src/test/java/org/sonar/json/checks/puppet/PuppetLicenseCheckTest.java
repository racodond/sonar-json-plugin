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

public class PuppetLicenseCheckTest {

  @Test
  public void should_define_a_valid_license_listed_by_SPDX_and_not_raise_any_issue() {
    JSONCheckVerifier.verify(
        new PuppetLicenseCheck(),
        CheckTestUtils.getTestFile("puppet/license/valid-spdx/metadata.json"))
      .noMore();
  }

  @Test
  public void should_define_a_valid_proprietary_license_and_not_raise_any_issue() {
    JSONCheckVerifier.verify(
        new PuppetLicenseCheck(),
        CheckTestUtils.getTestFile("puppet/license/valid-proprietary/metadata.json"))
      .noMore();
  }

  @Test
  public void should_define_an_invalid_license_and_raise_an_issue() {
    JSONCheckVerifier.verify(
        new PuppetLicenseCheck(),
        CheckTestUtils.getTestFile("puppet/license/invalid/metadata.json"))
      .next().startAtLine(5).startAtColumn(14).endAtLine(5).endAtColumn(22).withMessage("Define a valid license.")
      .noMore();
  }

  @Test
  public void should_not_raise_any_issues_because_it_is_not_a_metadata_json_file() {
    JSONCheckVerifier.verify(
        new PuppetLicenseCheck(),
        CheckTestUtils.getTestFile("puppet/license/not-metadata-json-file/notmetadata.json"))
      .noMore();
  }

}
