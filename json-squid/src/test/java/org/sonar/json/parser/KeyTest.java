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

public class KeyTest extends TestBase {

  private LexerlessGrammar b = JSONGrammar.createGrammar();

  @Test
  public void should_match_KEY() {
    assertThat(b.rule(JSONGrammar.KEY))
      .matches("\"\"")
      .matches("\"abc\"")
      .matches("\"\\\\\"")
      .matches("\"aa\\\\aa\"")
      .matches("\"aa\\\"aa\"")
      .matches("\"aa\\\"a\na\"")
      .matches("\"aa\\\"a\na\"");
  }

  @Test
  public void should_not_match_KEY() {
    assertThat(b.rule(JSONGrammar.KEY))
      .notMatches("123")
       .notMatches("12\\3")
      .notMatches("12\"3")
      .notMatches("\"12\"3\"");
   //   .notMatches("\"12\\3\"");
  }

}
