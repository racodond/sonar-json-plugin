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

public class ArrayTest extends TestBase {

  private LexerlessGrammar b = JSONGrammar.createGrammar();

  @Test
  public void should_match_ARRAY() {
    assertThat(b.rule(JSONGrammar.ARRAY))
      .matches("[]")
      .matches(" []")
      .matches("  []")
      .matches("  [ ]")
      .matches("  [  ]")
      .matches("  [ \"abc\" ]")
      .matches("  [ \"abc\", \"def\" ]")
      .matches("  [ \"abc\", \"def\" , null ]")
      .matches("  [ \"abc\", \"def\", null, true ]")
      .matches("  [ \"abc\", \"def\", null, true , false ]")
      .matches("  [1]")
      .matches("  [ 1.5 ]")
      .matches("  [ 1.5, 1 ]")
      .matches("  [ 1.5, 1 , 3 ]")
      .matches("  [ 1.5, 1 , \"abc\" ]")
      .matches("  [ 1.5, 1 , \"abc\", null ]")
      .matches("  [ 1.5, 1 , \"abc\", null, true, false ]")
      .matches("  [ 1.5 , {}]")
      .matches("  [ 1.5 , { \"abc\" : 123.4, \"def\": {}}]");
  }

}
