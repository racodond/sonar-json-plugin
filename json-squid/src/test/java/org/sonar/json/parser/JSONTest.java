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

public class JSONTest extends TestBase {

  private LexerlessGrammar b = JSONGrammar.createGrammar();

  @Test
  public void should_match_JSON_file() {
    assertThat(b.rule(JSONGrammar.JSON))
      .matches("")
      .matches("{}")
      .matches(" {} ")
      .matches(" {   } ")
      .matches(" { \"abc\": \"def\" }")
      .matches(" { \"abc\": \"def\", \"zzz\" : \"123\" }")
      .matches(" { \"abc\": \"def\", \"zzz\" : [] }")
      .matches(" { \"abc\": \"def\", \"zzz\" : {} }")
      .matches(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : true } }")
      .matches(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : null } }")
      .matches(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : 123 } , \"zzzz\": [ {}, {\"dd\": -12e+13} ]}")
      .matches(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : 123 } , \"z\\\\\\\\z\\\"zz\": [ {}, {\"dd\": -12e+13} ]}")
      .matches("[]")
      .matches(" []")
      .matches(" [   ] ")
      .matches(" [ true, \"abc\" ] ")
      .matches(" [ {}, true ] ");
  }

  @Test
  public void should_not_match_JSON_file() {
    assertThat(b.rule(JSONGrammar.JSON))
      .notMatches("abc");
  }

}
