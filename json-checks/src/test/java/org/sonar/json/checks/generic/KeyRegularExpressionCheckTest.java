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

import static org.fest.assertions.Assertions.assertThat;

public class KeyRegularExpressionCheckTest {

  @Test
  public void should_find_keys_matching_the_custom_regex_and_raise_issues() {
    KeyRegularExpressionCheck check = new KeyRegularExpressionCheck();
    check.setRegularExpression(".*mykey.*");
    check.setMessage("blabla...");

    JSONCheckVerifier.issues(check, CheckTestUtils.getTestFile("keyRegularExpression.json"))
      .next().atLine(2).withMessage("blabla...")
      .next().atLine(4).withMessage("blabla...")
      .next().atLine(6).withMessage("blabla...")
      .noMore();
  }

  @Test
  public void should_throw_an_illegal_state_exception_as_the_regular_expression_parameter_is_not_valid() {
    try {
      KeyRegularExpressionCheck check = new KeyRegularExpressionCheck();
      check.setRegularExpression("(");
      JSONCheckVerifier.issues(check, CheckTestUtils.getTestFile("keyRegularExpression.json")).noMore();
    } catch (IllegalStateException e) {
      assertThat(e.getMessage()).isEqualTo("Check json:key-regular-expression (Regular expression on key): " +
        "regularExpression parameter \"(\" is not a valid regular expression.");
    }
  }

}
