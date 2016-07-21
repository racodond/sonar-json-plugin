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
import org.sonar.plugins.json.api.tree.ObjectTree;
import org.sonar.plugins.json.api.tree.Tree;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ObjectTreeTest {

  @Test
  public void object() {
    ObjectTree tree;

    tree = parse("{}");
    assertThat(tree.leftBrace()).isNotNull();
    assertThat(tree.rightBrace()).isNotNull();
    assertThat(tree.pairSyntaxList()).isNull();
    assertThat(tree.pairs().size()).isEqualTo(0);

    tree = parse(" {}");
    assertThat(tree.pairs().size()).isEqualTo(0);

    tree = parse("  {}");
    assertThat(tree.pairs().size()).isEqualTo(0);

    tree = parse("  { }");
    assertThat(tree.pairs().size()).isEqualTo(0);

    tree = parse("  {  }");
    assertThat(tree.pairs().size()).isEqualTo(0);

    tree = parse("  { \"abc\": 1 }");
    assertThat(tree.pairs().size()).isEqualTo(1);
    assertThat(tree.pairSyntaxList()).isNotNull();
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertThat(tree.pairs().get(0).key().actualText()).isEqualTo("abc");
    assertTrue(tree.pairs().get(0).value().value().is(Tree.Kind.NUMBER));

    tree = parse("  { \"abc\": 1, \"def\": null }");
    assertThat(tree.pairs().size()).isEqualTo(2);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).value().value().is(Tree.Kind.NULL));

    tree = parse(" { \"abc\": \"def\", \"zzz\" : [] }");
    assertThat(tree.pairs().size()).isEqualTo(2);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).value().value().is(Tree.Kind.ARRAY));

    tree = parse(" { \"abc\": \"def\", \"zzz\" : {} }");
    assertThat(tree.pairs().size()).isEqualTo(2);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).value().value().is(Tree.Kind.OBJECT));

    tree = parse(" { \"abc\": \"def\", \"zzz\" : { \"abc\": \"def\" ,\n \"zzz\" : 123 } , \"z\\\\\\\\z\\\"zz\": [ {}, {\"dd\": -12e+13} ]}");
    assertThat(tree.pairs().size()).isEqualTo(3);
    assertTrue(tree.pairs().get(0).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(1).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(2).is(Tree.Kind.PAIR));
    assertTrue(tree.pairs().get(2).value().value().is(Tree.Kind.ARRAY));
  }

  @Test
  public void notObject() {
    checkNotParsed("{");
    checkNotParsed("}");
    checkNotParsed("\"{}\"");
  }

  private ObjectTree parse(String toParse) {
    return (ObjectTree) JSONParserBuilder
      .createTestParser(Charsets.UTF_8, JSONLexicalGrammar.OBJECT)
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
