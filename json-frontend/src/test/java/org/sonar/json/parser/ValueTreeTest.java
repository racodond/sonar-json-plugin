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

import org.junit.Test;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.tree.ValueTree;

import static org.fest.assertions.Assertions.assertThat;

public class ValueTreeTest extends CommonJsonTreeTest {

  public ValueTreeTest() {
    super(JSONLexicalGrammar.VALUE);
  }

  @Test
  public void value() {
    checkParsed("[]", Tree.Kind.ARRAY);
    checkParsed(" []", Tree.Kind.ARRAY);
    checkParsed(" [ ]", Tree.Kind.ARRAY);
    checkParsed("{}", Tree.Kind.OBJECT);
    checkParsed(" {}", Tree.Kind.OBJECT);
    checkParsed(" { }", Tree.Kind.OBJECT);
    checkParsed("null", Tree.Kind.NULL);
    checkParsed(" null", Tree.Kind.NULL);
    checkParsed("true", Tree.Kind.TRUE);
    checkParsed(" true", Tree.Kind.TRUE);
    checkParsed("false", Tree.Kind.FALSE);
    checkParsed("\"abc\"", Tree.Kind.STRING);
    checkParsed(" \"abc\"", Tree.Kind.STRING);
    checkParsed("1", Tree.Kind.NUMBER);
    checkParsed("1.5", Tree.Kind.NUMBER);
    checkParsed(" 1", Tree.Kind.NUMBER);
    checkParsed(" 1.5", Tree.Kind.NUMBER);
    checkParsed("-1.5", Tree.Kind.NUMBER);
    checkParsed(" -1.5", Tree.Kind.NUMBER);
  }

  @Test
  public void notValue() {
    checkNotParsed("abc");
  }

  private void checkParsed(String toParse, Tree.Kind kind) {
    ValueTree tree = (ValueTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.value()).isNotNull();
    assertThat(tree.value().is(kind)).isTrue();
  }

}
