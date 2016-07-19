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
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.tree.ValueTree;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ValueTreeTest {

  @Test
  public void value() {
    ValueTree tree;

    tree = parse("[]");
    assertTrue(tree.value().is(Tree.Kind.ARRAY));

    tree = parse(" []");
    assertTrue(tree.value().is(Tree.Kind.ARRAY));

    tree = parse(" [ ]");
    assertTrue(tree.value().is(Tree.Kind.ARRAY));

    tree = parse("{}");
    assertTrue(tree.value().is(Tree.Kind.OBJECT));

    tree = parse(" {}");
    assertTrue(tree.value().is(Tree.Kind.OBJECT));

    tree = parse(" { }");
    assertTrue(tree.value().is(Tree.Kind.OBJECT));

    tree = parse("null");
    assertTrue(tree.value().is(Tree.Kind.NULL));

    tree = parse(" null");
    assertTrue(tree.value().is(Tree.Kind.NULL));

    tree = parse("true");
    assertTrue(tree.value().is(Tree.Kind.TRUE));

    tree = parse(" true");
    assertTrue(tree.value().is(Tree.Kind.TRUE));

    tree = parse("false");
    assertTrue(tree.value().is(Tree.Kind.FALSE));

    tree = parse("\"abc\"");
    assertTrue(tree.value().is(Tree.Kind.STRING));

    tree = parse(" \"abc\"");
    assertTrue(tree.value().is(Tree.Kind.STRING));

    tree = parse("1");
    assertTrue(tree.value().is(Tree.Kind.NUMBER));

    tree = parse("1.5");
    assertTrue(tree.value().is(Tree.Kind.NUMBER));

    tree = parse(" 1");
    assertTrue(tree.value().is(Tree.Kind.NUMBER));

    tree = parse(" 1.5");
    assertTrue(tree.value().is(Tree.Kind.NUMBER));

    tree = parse("-1.5");
    assertTrue(tree.value().is(Tree.Kind.NUMBER));

    tree = parse(" -1.5");
    assertTrue(tree.value().is(Tree.Kind.NUMBER));
  }

  @Test
  public void notValue() {
    checkNotParsed("abc");
  }

  private ValueTree parse(String toParse) {
    return (ValueTree) JSONParserBuilder
      .createTestParser(Charsets.UTF_8, JSONLexicalGrammar.VALUE)
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
