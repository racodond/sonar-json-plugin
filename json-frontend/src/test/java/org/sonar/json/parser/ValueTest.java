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

public class ValueTest extends TestBase {

  private LexerlessGrammar b = JSONGrammar.createGrammar();

  @Test
  public void should_match_VALUE() {
    assertThat(b.rule(JSONGrammar.VALUE))
      .matches("[]")
      .matches(" []")
      .matches(" [ ]")
      .matches("{}")
      .matches(" {}")
      .matches(" { }")
      .matches("null")
      .matches(" null")
      .matches("true")
      .matches(" true")
      .matches("false")
      .matches("\"abc\"")
      .matches(" \"abc\"")
      .matches("1")
      .matches("1.5")
      .matches(" 1")
      .matches(" 1.5")
      .matches("-1.5")
      .matches(" -1.5");
  }

  @Test
  public void should_not_match_VALUE() {
    assertThat(b.rule(JSONGrammar.VALUE))
      .notMatches("[")
      .notMatches("}");
  }

}
