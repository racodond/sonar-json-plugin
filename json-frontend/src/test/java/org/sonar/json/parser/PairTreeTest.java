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
import org.sonar.plugins.json.api.tree.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class PairTreeTest extends CommonJsonTreeTest {

  public PairTreeTest() {
    super(JSONLexicalGrammar.PAIR);
  }

  @Test
  public void pair() {
    PairTree tree;

    tree = checkParsed("\"key\":\"value\"");
    assertThat(tree.key().actualText()).isEqualTo("key");
    assertTrue(tree.value().value().is(Tree.Kind.STRING));
    assertThat(((StringTree) tree.value().value()).text()).isEqualTo("\"value\"");
    assertThat(((StringTree) tree.value().value()).actualText()).isEqualTo("value");

    tree = checkParsed(" \"key\" : \"value\"");
    assertThat(tree.key().actualText()).isEqualTo("key");
    assertTrue(tree.value().value().is(Tree.Kind.STRING));
    assertThat(((StringTree) tree.value().value()).text()).isEqualTo("\"value\"");
    assertThat(((StringTree) tree.value().value()).actualText()).isEqualTo("value");

    tree = checkParsed("\"key\" : {\"abc\": 1, \"def\": 2}");
    assertThat(tree.key().actualText()).isEqualTo("key");
    assertTrue(tree.value().value().is(Tree.Kind.OBJECT));
    assertThat(((ObjectTree) tree.value().value()).pairs().size()).isEqualTo(2);

    tree = checkParsed("\"key\" : [\"abc\", \"def\"]");
    assertThat(tree.key().actualText()).isEqualTo("key");
    assertTrue(tree.value().value().is(Tree.Kind.ARRAY));
    assertThat(((ArrayTree) tree.value().value()).elements().size()).isEqualTo(2);

    tree = checkParsed("\"key\" : null");
    assertTrue(tree.value().value().is(Tree.Kind.NULL));

    tree = checkParsed("\"key\" : false");
    assertTrue(tree.value().value().is(Tree.Kind.FALSE));

    tree = checkParsed("\"key\" : true");
    assertTrue(tree.value().value().is(Tree.Kind.TRUE));

    tree = checkParsed("\"key\" : 1");
    assertTrue(tree.value().value().is(Tree.Kind.NUMBER));
  }

  @Test
  public void notPair() {
    checkNotParsed("123");
    checkNotParsed("\"abc\"");
    checkNotParsed("\"abc\":");
    checkNotParsed(":");
    checkNotParsed(": \"ab\"");
  }

  private PairTree checkParsed(String toParse) {
    PairTree tree = (PairTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.key()).isNotNull();
    assertThat(tree.colon()).isNotNull();
    assertThat(tree.value()).isNotNull();
    return tree;
  }

}
