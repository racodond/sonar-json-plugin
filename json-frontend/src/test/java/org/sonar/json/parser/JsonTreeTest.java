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
package org.sonar.json.parser;

import com.google.common.base.Charsets;
import org.junit.Test;
import org.sonar.plugins.json.api.tree.JsonTree;

import static org.junit.Assert.fail;

public class JsonTreeTest {

  @Test
  public void json() {
    parse("");
    parse("{}");
    parse(" {} ");
    parse(" {   } ");
    parse(" { \"abc\": \"def\" }");
    parse(" { \"abc\": \"def\", \"zzz\" : \"123\" }");
    parse(" { \"abc\": \"def\", \"zzz\" : [] }");
    parse(" { \"abc\": \"def\", \"zzz\" : {} }");
    parse(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : true } }");
    parse(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : null } }");
    parse(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : 123 } , \"zzzz\": [ {}, {\"dd\": -12e+13} ]}");
    parse(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : 123 } ,\n \"z\\\\\\\\z\\\"zz\": [ {}, {\"dd\": \n -12e+13} ]}");
    parse("[]");
    parse(" []");
    parse(" [   ] ");
    parse(" [ true, \"abc\" ] ");
    parse(" [ {}, true ] ");
    parse("null");
    parse(" null ");
    parse("true");
    parse(" true ");
    parse("false");
    parse(" false ");
    parse("\"abc\"");
    parse(" \"abc\" ");
    parse("1.2");
    parse(" 1.2 ");
    parse("\ufeff");
    parse("\ufeff ");
    parse("\ufeff {}");
    parse("\ufeff true");
  }

  @Test
  public void notJson() {
    checkNotParsed("{");
    checkNotParsed("}");
    checkNotParsed("blabla");
    checkNotParsed("\"abc\": 10");
    checkNotParsed("true, false");
  }

  private JsonTree parse(String toParse) {
    return (JsonTree) JSONParserBuilder
      .createTestParser(Charsets.UTF_8, JSONLexicalGrammar.JSON)
      .parse(toParse);
  }

  private void checkNotParsed(String toParse) {
    try {
      parse(toParse);
    } catch (Exception e) {
      return;
    }
    fail("Did not throw a RecognitionException as expected.");
  }

}
