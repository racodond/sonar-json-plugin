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
package org.sonar.json.checks.generic;

import org.junit.Test;
import org.sonar.json.checks.CheckTestUtils;
import org.sonar.json.checks.verifier.JSONCheckVerifier;

public class FileNameCheckTest {

  @Test
  public void should_follow_the_default_naming_convention_and_not_raise_an_issue() {
    JSONCheckVerifier.issues(new FileNameCheck(), CheckTestUtils.getTestFile("tabCharacter.json"))
      .noMore();
  }

  @Test
  public void should_not_follow_the_default_naming_convention_and_raise_an_issue() {
    JSONCheckVerifier.issues(new FileNameCheck(), CheckTestUtils.getTestFile("file-name.ko.json"))
      .next().withMessage("Rename this file to match this regular expression: ^[A-Za-z][-_A-Za-z0-9]*\\.json$")
      .noMore();
  }

  @Test
  public void should_follow_a_custom_naming_convention_and_not_raise_an_issue() {
    FileNameCheck check = new FileNameCheck();
    check.setFormat("^[a-z][-a-z]+\\.json$");

    JSONCheckVerifier.issues(check, CheckTestUtils.getTestFile("sample.json"))
      .noMore();
  }

  @Test
  public void should_not_follow_a_custom_naming_convention_and_raise_an_issue() {
    FileNameCheck check = new FileNameCheck();
    check.setFormat("^[a-z]+\\.json$");

    JSONCheckVerifier.issues(check, CheckTestUtils.getTestFile("file-name.ko.json"))
      .next().withMessage("Rename this file to match this regular expression: ^[a-z]+\\.json$")
      .noMore();
  }

}
