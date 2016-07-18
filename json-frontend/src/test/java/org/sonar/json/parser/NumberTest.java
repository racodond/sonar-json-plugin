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
package org.sonar.json.parser;

import org.junit.Test;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class NumberTest extends TestBase {

  private LexerlessGrammar b = JSONGrammar.createGrammar();

  @Test
  public void should_match_NUMBER() {
    assertThat(b.rule(JSONGrammar.NUMBER))
      .matches("0")
      .matches("1")
      .matches("123")
      .matches("0.5")
      .matches("1.4")
      .matches("10.99")
      .matches("-1")
      .matches("-123")
      .matches("-0.5")
      .matches("-1.4")
      .matches("-10.99")
      .matches("1e10")
      .matches("1E10")
      .matches("1e-10")
      .matches("1E-10")
      .matches("1e+10")
      .matches("1E+10")
      .matches("-1e10")
      .matches("-1E10")
      .matches("-1e-10")
      .matches("-1E-10")
      .matches("-1e+10")
      .matches("-1E+10")
      .matches("10.5e10")
      .matches("10.5E10")
      .matches("10.5e-10")
      .matches("10.5E-10")
      .matches("10.5e+10")
      .matches("10.5E+10")
      .matches("-10.5e10")
      .matches("-10.5E10")
      .matches("-10.5e-10")
      .matches("-10.5E-10")
      .matches("-10.5e+10")
      .matches("-10.5E+10");
  }

  @Test
  public void should_not_match_NUMBER() {
    assertThat(b.rule(JSONGrammar.NUMBER))
      .notMatches("abc")
      .notMatches("+1")
      .notMatches("+1.5.")
      .notMatches("0.")
      .notMatches("1.");
  }

}
