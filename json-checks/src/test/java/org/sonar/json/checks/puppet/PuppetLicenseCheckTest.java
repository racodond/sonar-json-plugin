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

public class PuppetLicenseCheckTest {

  private PuppetLicenseCheck check = new PuppetLicenseCheck();

  @Test
  public void should_define_a_valid_license_listed_by_SPDX_and_not_raise_any_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/valid-spdx/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_define_a_valid_proprietary_license_and_not_raise_any_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/valid-proprietary/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_define_an_invalid_license_and_raise_an_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/invalid/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).next()
      .withMessage("License \"blabla\" is not a valid license. Define a valid license.")
      .noMore();
  }

  @Test
  public void should_define_two_licenses_and_raise_an_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/two-licenses/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).next()
      .withMessage("Several license definitions have been found. Keep only one license definition.")
      .noMore();
  }

}
