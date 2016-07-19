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

public class PuppetEnforceAuthorValueCheckTest {

  @Test
  public void should_match_the_default_required_value_and_not_raise_any_issue() {
    JSONCheckVerifier.issues(
      new PuppetEnforceAuthorValueCheck(),
      CheckTestUtils.getTestFile("puppet/author/default/metadata.json"))
      .noMore();
  }

  @Test
  public void should_match_the_required_custom_value_and_not_raise_any_issue() {
    PuppetEnforceAuthorValueCheck check = new PuppetEnforceAuthorValueCheck();
    check.setAuthor("Pat");

    JSONCheckVerifier.issues(
      check,
      CheckTestUtils.getTestFile("puppet/author/custom/metadata.json"))
      .noMore();
  }

  @Test
  public void should_not_match_the_required_default_value_and_raise_an_issue() {
    JSONCheckVerifier.issues(
      new PuppetEnforceAuthorValueCheck(),
      CheckTestUtils.getTestFile("puppet/author/default-issue/metadata.json"))
      .next().atLine(4).withMessage("Set the author to \"John Doe\".")
      .next().atLine(5).withMessage("Set the author to \"John Doe\".")
      .noMore();
  }

  @Test
  public void should_not_match_the_required_custom_value_and_raise_an_issue() {
    PuppetEnforceAuthorValueCheck check = new PuppetEnforceAuthorValueCheck();
    check.setAuthor("Smith");

    JSONCheckVerifier.issues(
      check,
      CheckTestUtils.getTestFile("puppet/author/custom/metadata.json"))
      .next().atLine(4).withMessage("Set the author to \"Smith\".")
      .noMore();
  }

  @Test
  public void should_not_be_triggered_when_no_author_is_defined() {
    JSONCheckVerifier.issues(
      new PuppetEnforceAuthorValueCheck(),
      CheckTestUtils.getTestFile("puppet/required-keys/missing-keys/metadata.json"))
      .noMore();
  }

  @Test
  public void should_not_raise_any_issues_because_it_is_not_a_metadata_json_file() {
    JSONCheckVerifier.issues(
      new PuppetEnforceAuthorValueCheck(),
      CheckTestUtils.getTestFile("puppet/author/not-metadata-json-file/notmetadata.json"))
      .noMore();
  }

}
