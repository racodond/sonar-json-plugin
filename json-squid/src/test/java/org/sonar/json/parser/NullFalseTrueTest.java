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

public class NullFalseTrueTest extends TestBase {

  private LexerlessGrammar b = JSONGrammar.createGrammar();

  @Test
  public void should_match_NULL() {
    assertThat(b.rule(JSONGrammar.NULL))
      .matches("null");
  }

  @Test
  public void should_match_FALSE() {
    assertThat(b.rule(JSONGrammar.FALSE))
      .matches("false");
  }

  @Test
  public void should_match_TRUE() {
    assertThat(b.rule(JSONGrammar.TRUE))
      .matches("true");
  }

  @Test
  public void should_not_match_NULL() {
    assertThat(b.rule(JSONGrammar.NULL))
      .notMatches("\"null\"");
  }

  @Test
  public void should_not_match_FALSE() {
    assertThat(b.rule(JSONGrammar.FALSE))
      .notMatches("\"false\"");
  }

  @Test
  public void should_not_match_TRUE() {
    assertThat(b.rule(JSONGrammar.TRUE))
      .notMatches("\"true\"");
  }

}
