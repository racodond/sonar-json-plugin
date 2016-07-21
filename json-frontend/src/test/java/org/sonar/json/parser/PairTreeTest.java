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
import org.sonar.plugins.json.api.tree.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PairTreeTest {

  @Test
  public void pair() {
    PairTree tree;

    tree = parse("\"key\":\"value\"");
    assertThat(tree.key()).isNotNull();
    assertThat(tree.colon()).isNotNull();
    assertThat(tree.value()).isNotNull();
    assertThat(tree.key().actualText()).isEqualTo("key");
    assertTrue(tree.value().value().is(Tree.Kind.STRING));
    assertThat(((StringTree) tree.value().value()).text()).isEqualTo("\"value\"");
    assertThat(((StringTree) tree.value().value()).actualText()).isEqualTo("value");

    tree = parse(" \"key\" : \"value\"");
    assertThat(tree.key()).isNotNull();
    assertThat(tree.colon()).isNotNull();
    assertThat(tree.value()).isNotNull();
    assertThat(tree.key().actualText()).isEqualTo("key");
    assertTrue(tree.value().value().is(Tree.Kind.STRING));
    assertThat(((StringTree) tree.value().value()).text()).isEqualTo("\"value\"");
    assertThat(((StringTree) tree.value().value()).actualText()).isEqualTo("value");

    tree = parse("\"key\" : {\"abc\": 1, \"def\": 2}");
    assertThat(tree.key()).isNotNull();
    assertThat(tree.colon()).isNotNull();
    assertThat(tree.value()).isNotNull();
    assertThat(tree.key().actualText()).isEqualTo("key");
    assertTrue(tree.value().value().is(Tree.Kind.OBJECT));
    assertThat(((ObjectTree) tree.value().value()).pairs().size()).isEqualTo(2);

    tree = parse("\"key\" : [\"abc\", \"def\"]");
    assertThat(tree.key()).isNotNull();
    assertThat(tree.colon()).isNotNull();
    assertThat(tree.value()).isNotNull();
    assertThat(tree.key().actualText()).isEqualTo("key");
    assertTrue(tree.value().value().is(Tree.Kind.ARRAY));
    assertThat(((ArrayTree) tree.value().value()).elements().size()).isEqualTo(2);

    tree = parse("\"key\" : null");
    assertTrue(tree.value().value().is(Tree.Kind.NULL));

    tree = parse("\"key\" : false");
    assertTrue(tree.value().value().is(Tree.Kind.FALSE));

    tree = parse("\"key\" : true");
    assertTrue(tree.value().value().is(Tree.Kind.TRUE));

    tree = parse("\"key\" : 1");
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

  private PairTree parse(String toParse) {
    return (PairTree) JSONParserBuilder
      .createTestParser(Charsets.UTF_8, JSONLexicalGrammar.PAIR)
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
