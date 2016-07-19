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
import org.sonar.plugins.json.api.tree.ArrayTree;
import org.sonar.plugins.json.api.tree.Tree;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ArrayTreeTest {

  @Test
  public void array() {
    ArrayTree tree;

    tree = parse("[]");
    assertThat(tree.leftBracket()).isNotNull();
    assertThat(tree.rightBracket()).isNotNull();
    assertThat(tree.elementSyntaxList()).isNull();
    assertThat(tree.elements().size()).isEqualTo(0);

    tree = parse(" []");
    assertThat(tree.elements().size()).isEqualTo(0);

    tree = parse("  []");
    assertThat(tree.elements().size()).isEqualTo(0);

    tree = parse("  [ ]");
    assertThat(tree.elements().size()).isEqualTo(0);

    tree = parse("  [  ]");
    assertThat(tree.elements().size()).isEqualTo(0);

    tree = parse("  [ \"abc\" ]");
    assertThat(tree.elements().size()).isEqualTo(1);
    assertThat(tree.elementSyntaxList()).isNotNull();
    assertTrue(tree.elements().get(0).value().is(Tree.Kind.STRING));

    tree = parse("  [ \"abc\", \"def\" ]");
    assertThat(tree.elements().size()).isEqualTo(2);

    tree = parse("  [ \"abc\", \"def\" , null ]");
    assertThat(tree.elements().size()).isEqualTo(3);
    assertTrue(tree.elements().get(2).value().is(Tree.Kind.NULL));

    tree = parse("  [ \"abc\", \"def\", null, true ]");
    assertThat(tree.elements().size()).isEqualTo(4);
    assertTrue(tree.elements().get(3).value().is(Tree.Kind.TRUE));

    tree = parse("  [ \"abc\", \"def\", null, true , false ]");
    assertThat(tree.elements().size()).isEqualTo(5);
    assertTrue(tree.elements().get(4).value().is(Tree.Kind.FALSE));

    tree = parse("  [1]");
    assertThat(tree.elements().size()).isEqualTo(1);
    assertTrue(tree.elements().get(0).value().is(Tree.Kind.NUMBER));

    tree = parse("  [ 1.5 ]");
    assertThat(tree.elements().size()).isEqualTo(1);

    tree = parse("  [ 1.5, 1 ]");
    assertThat(tree.elements().size()).isEqualTo(2);

    tree = parse("  [ 1.5, 1 , 3 ]");
    assertThat(tree.elements().size()).isEqualTo(3);

    tree = parse("  [ 1.5, 1 , \"abc\" ]");
    assertThat(tree.elements().size()).isEqualTo(3);

    tree = parse("  [ 1.5, 1 , \"abc\", null ]");
    assertThat(tree.elements().size()).isEqualTo(4);

    tree = parse("  [ 1.5, 1 , \"abc\", null, true, false ]");
    assertThat(tree.elements().size()).isEqualTo(6);

    tree = parse("  [ 1.5 , {}]");
    assertThat(tree.elements().size()).isEqualTo(2);

    tree = parse("  [ 1.5 , { \"abc\" : 123.4, \"def\": {}}]");
    assertThat(tree.elements().size()).isEqualTo(2);
  }

  @Test
  public void notArray() {
    checkNotParsed("[");
    checkNotParsed("]");
    checkNotParsed("\"[]\"");
  }

  private ArrayTree parse(String toParse) {
    return (ArrayTree) JSONParserBuilder
      .createTestParser(Charsets.UTF_8, JSONLexicalGrammar.ARRAY)
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
