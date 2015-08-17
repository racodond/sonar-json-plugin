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

public class PuppetEnforceLicenseValueCheckTest {

  @Test
  public void should_match_the_default_required_value_and_not_raise_any_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/valid-spdx/metadata.json"), new PuppetEnforceLicenseValueCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_match_the_required_custom_value_and_not_raise_any_issue() {
    PuppetEnforceLicenseValueCheck check = new PuppetEnforceLicenseValueCheck();
    check.setLicense("blabla");
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/match-required-value/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_not_match_the_required_default_value_and_raise_an_issue() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/match-required-value/metadata.json"), new PuppetEnforceLicenseValueCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(5).withMessage("Set the license to \"LGPL-3.0\".").
      noMore();
  }

  @Test
  public void should_not_match_the_required_custom_value_and_raise_an_issue() {
    PuppetEnforceLicenseValueCheck check = new PuppetEnforceLicenseValueCheck();
    check.setLicense("blabla");
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/valid-spdx/metadata.json"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(5).withMessage("Set the license to \"blabla\".").
      noMore();
  }

  @Test
  public void should_not_be_triggered_when_no_license_is_defined() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/no-license/metadata.json"), new PuppetEnforceLicenseValueCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
  }

  @Test
  public void should_raise_two_issues() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File("src/test/resources/checks/puppet/license/two-licenses/metadata.json"), new PuppetEnforceLicenseValueCheck());
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(5).withMessage("Set the license to \"LGPL-3.0\".")
      .next().atLine(6).withMessage("Set the license to \"LGPL-3.0\".")
      .noMore();
  }
}
