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
import org.sonar.plugins.json.api.tree.ArrayTree;
import org.sonar.plugins.json.api.tree.Tree;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ArrayTreeTest extends CommonJsonTreeTest {

  public ArrayTreeTest() {
    super(JSONLexicalGrammar.ARRAY);
  }

  @Test
  public void array() throws IOException {
    ArrayTree tree;

    tree = checkParsed("[]");
    assertThat(tree.elements()).hasSize(0);

    tree = checkParsed(" []");
    assertThat(tree.elements()).hasSize(0);

    tree = checkParsed("  []");
    assertThat(tree.elements()).hasSize(0);

    tree = checkParsed("  [ ]");
    assertThat(tree.elements()).hasSize(0);

    tree = checkParsed("  [  ]");
    assertThat(tree.elements()).hasSize(0);

    tree = checkParsed("  [ \"abc\" ]");
    assertThat(tree.elements()).hasSize(1);
    assertTrue(tree.elements().get(0).value().is(Tree.Kind.STRING));

    tree = checkParsed("  [ \"abc\", \"def\" ]");
    assertThat(tree.elements()).hasSize(2);

    tree = checkParsed("  [ \"abc\", \"def\" , null ]");
    assertThat(tree.elements()).hasSize(3);
    assertTrue(tree.elements().get(2).value().is(Tree.Kind.NULL));

    tree = checkParsed("  [ \"abc\", \"def\", null, true ]");
    assertThat(tree.elements()).hasSize(4);
    assertTrue(tree.elements().get(3).value().is(Tree.Kind.TRUE));

    tree = checkParsed("  [ \"abc\", \"def\", null, true , false ]");
    assertThat(tree.elements()).hasSize(5);
    assertTrue(tree.elements().get(4).value().is(Tree.Kind.FALSE));

    tree = checkParsed("  [1]");
    assertThat(tree.elements()).hasSize(1);
    assertTrue(tree.elements().get(0).value().is(Tree.Kind.NUMBER));

    tree = checkParsed("  [ 1.5 ]");
    assertThat(tree.elements()).hasSize(1);

    tree = checkParsed("  [ 1.5, 1 ]");
    assertThat(tree.elements()).hasSize(2);

    tree = checkParsed("  [ 1.5, 1 , 3 ]");
    assertThat(tree.elements()).hasSize(3);

    tree = checkParsed("  [ 1.5, 1 , \"abc\" ]");
    assertThat(tree.elements()).hasSize(3);

    tree = checkParsed("  [ 1.5, 1 , \"abc\", null ]");
    assertThat(tree.elements()).hasSize(4);

    tree = checkParsed("  [ 1.5, 1 , \"abc\", null, true, false ]");
    assertThat(tree.elements()).hasSize(6);

    tree = checkParsed("  [ 1.5 , {}]");
    assertThat(tree.elements()).hasSize(2);

    tree = checkParsed("  [ 1.5 , { \"abc\" : 123.4, \"def\": {}}]");
    assertThat(tree.elements()).hasSize(2);

    tree = checkParsed(new File("src/test/resources/many-values.json"));
    assertThat(tree.elements()).hasSize(10000);
  }

  @Test
  public void notArray() {
    checkNotParsed("[");
    checkNotParsed("]");
    checkNotParsed("\"[]\"");
  }

  private ArrayTree checkParsed(String toParse) throws IOException {
    ArrayTree tree = (ArrayTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.leftBracket()).isNotNull();
    assertThat(tree.rightBracket()).isNotNull();
    assertThat(tree.elements()).isNotNull();
    return tree;
  }

  private ArrayTree checkParsed(File file) throws IOException {
    return checkParsed(Files.toString(file, Charsets.UTF_8));
  }

}
