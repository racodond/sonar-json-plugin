/*
 * SonarQube JSON Analyzer
 * Copyright (C) 2015-2017 David RACODON
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
import org.sonar.plugins.json.api.tree.ObjectTree;
import org.sonar.plugins.json.api.tree.Tree;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ObjectTreeTest extends CommonJsonTreeTest {

  public ObjectTreeTest() {
    super(JSONLexicalGrammar.OBJECT);
  }

  @Test
  public void object() throws IOException {
    ObjectTree tree;

    tree = checkParsed("{}");
    assertThat(tree.pairs()).hasSize(0);

    tree = checkParsed(" {}");
    assertThat(tree.pairs()).hasSize(0);

    tree = checkParsed("  {}");
    assertThat(tree.pairs()).hasSize(0);

    tree = checkParsed("  { }");
    assertThat(tree.pairs()).hasSize(0);

    tree = checkParsed("  {  }");
    assertThat(tree.pairs()).hasSize(0);

    tree = checkParsed("  { \"abc\": 1 }");
    assertThat(tree.pairs()).hasSize(1);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertThat(tree.pairs().get(0).key().actualText()).isEqualTo("abc");
    assertTrue(tree.pairs().get(0).value().value().is(Tree.Kind.NUMBER));

    tree = checkParsed("  { \"abc\": 1, \"def\": null }");
    assertThat(tree.pairs()).hasSize(2);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).value().value().is(Tree.Kind.NULL));

    tree = checkParsed(" { \"abc\": \"def\", \"zzz\" : [] }");
    assertThat(tree.pairs()).hasSize(2);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).value().value().is(Tree.Kind.ARRAY));

    tree = checkParsed(" { \"abc\": \"def\", \"zzz\" : {} }");
    assertThat(tree.pairs()).hasSize(2);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).value().value().is(Tree.Kind.OBJECT));

    tree = checkParsed(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" ,\n \"zzz\" : 123 } , \"z\\\\\\\\z\\\"zz\": [ {}, {\"dd\": -12e+13} ]}");
    assertThat(tree.pairs()).hasSize(3);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(2).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(2).value().value().is(Tree.Kind.ARRAY));

    tree = checkParsed(new File("src/test/resources/many-pairs.json"));
    assertThat(tree.pairs()).hasSize(10000);
  }

  @Test
  public void notObject() {
    checkNotParsed("{");
    checkNotParsed("}");
    checkNotParsed("\"{}\"");
  }

  private ObjectTree checkParsed(String toParse) throws IOException {
    ObjectTree tree = (ObjectTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.leftBrace()).isNotNull();
    assertThat(tree.rightBrace()).isNotNull();
    return tree;
  }

  private ObjectTree checkParsed(File file) throws IOException {
    return checkParsed(Files.toString(file, Charsets.UTF_8));
  }

}
