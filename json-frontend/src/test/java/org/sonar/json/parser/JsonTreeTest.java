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
import com.google.common.io.Files;
import org.junit.Test;
import org.sonar.plugins.json.api.tree.JsonTree;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class JsonTreeTest extends CommonJsonTreeTest {

  public JsonTreeTest() {
    super(JSONLexicalGrammar.JSON);
  }

  @Test
  public void json() throws IOException {
    checkParsed("");
    checkParsed("{}");
    checkParsed(" {} ");
    checkParsed(" {   } ");
    checkParsed(" { \"abc\": \"def\" }");
    checkParsed(" { \"abc\": \"def\", \"zzz\" : \"123\" }");
    checkParsed(" { \"abc\": \"def\", \"zzz\" : [] }");
    checkParsed(" { \"abc\": \"def\", \"zzz\" : {} }");
    checkParsed(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : true } }");
    checkParsed(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : null } }");
    checkParsed(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : 123 } , \"zzzz\": [ {}, {\"dd\": -12e+13} ]}");
    checkParsed(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" , \"zzz\" : 123 } ,\n \"z\\\\\\\\z\\\"zz\": [ {}, {\"dd\": \n -12e+13} ]}");
    checkParsed("[]");
    checkParsed(" []");
    checkParsed(" [   ] ");
    checkParsed(" [ true, \"abc\" ] ");
    checkParsed(" [ {}, true ] ");
    checkParsed("null");
    checkParsed(" null ");
    checkParsed("true");
    checkParsed(" true ");
    checkParsed("false");
    checkParsed(" false ");
    checkParsed("\"abc\"");
    checkParsed(" \"abc\" ");
    checkParsed("1.2");
    checkParsed(" 1.2 ");
    checkParsed("\ufeff");
    checkParsed("\ufeff ");
    checkParsed("\ufeff {}");
    checkParsed("\ufeff true");

    checkParsed(new File("src/test/resources/many-pairs.json"));
    checkParsed(new File("src/test/resources/many-values.json"));
  }

  @Test
  public void notJson() {
    checkNotParsed("{");
    checkNotParsed("}");
    checkNotParsed("blabla");
    checkNotParsed("\"abc\": 10");
    checkNotParsed("true, false");
  }

  private void checkParsed(String toParse) throws IOException {
    JsonTree tree = (JsonTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
  }

  private void checkParsed(File file) throws IOException {
    checkParsed(Files.toString(file, Charsets.UTF_8));
  }

}
