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

import com.sonar.sslr.api.typed.GrammarBuilder;
import org.sonar.json.tree.impl.InternalSyntaxToken;
import org.sonar.json.tree.impl.SyntaxList;
import org.sonar.plugins.json.api.tree.*;

public class JSONGrammar {

  private final GrammarBuilder<InternalSyntaxToken> b;
  private final TreeFactory f;

  public JSONGrammar(GrammarBuilder<InternalSyntaxToken> b, TreeFactory f) {
    this.b = b;
    this.f = f;
  }

  public JsonTree JSON() {
    return b.<JsonTree>nonterminal(JSONLexicalGrammar.JSON).is(
      f.json(
        b.optional(b.token(JSONLexicalGrammar.BOM)),
        b.optional(VALUE()),
        b.token(JSONLexicalGrammar.EOF)));
  }

  public ObjectTree OBJECT() {
    return b.<ObjectTree>nonterminal(JSONLexicalGrammar.OBJECT).is(
      f.object(
        b.token(JSONLexicalGrammar.LEFT_BRACE),
        b.optional(PAIR_LIST()),
        b.token(JSONLexicalGrammar.RIGHT_BRACE)));
  }

  public ArrayTree ARRAY() {
    return b.<ArrayTree>nonterminal(JSONLexicalGrammar.ARRAY).is(
      f.array(
        b.token(JSONLexicalGrammar.LEFT_BRACKET),
        b.optional(VALUE_LIST()),
        b.token(JSONLexicalGrammar.RIGHT_BRACKET)));
  }

  public SyntaxList<ValueTree> VALUE_LIST() {
    return b.<SyntaxList<ValueTree>>nonterminal().is(
      b.firstOf(
        f.valueList(VALUE(), b.token(JSONLexicalGrammar.COMMA), VALUE_LIST()),
        f.valueList(VALUE())));
  }

  public SyntaxList<PairTree> PAIR_LIST() {
    return b.<SyntaxList<PairTree>>nonterminal().is(
      b.firstOf(
        f.pairList(PAIR(), b.token(JSONLexicalGrammar.COMMA), PAIR_LIST()),
        f.pairList(PAIR())));
  }

  public PairTree PAIR() {
    return b.<PairTree>nonterminal(JSONLexicalGrammar.PAIR).is(
      f.pair(
        KEY(),
        b.token(JSONLexicalGrammar.COLON),
        VALUE()));
  }

  public KeyTree KEY() {
    return b.<KeyTree>nonterminal(JSONLexicalGrammar.KEY).is(
      f.key(b.token(JSONLexicalGrammar.STRING)));
  }

  public ValueTree VALUE() {
    return b.<ValueTree>nonterminal(JSONLexicalGrammar.VALUE).is(
      f.value(
        b.firstOf(
          OBJECT(),
          ARRAY(),
          TRUE(),
          FALSE(),
          NULL(),
          NUMBER(),
          STRING())));
  }

  public StringTree STRING() {
    return b.<StringTree>nonterminal().is(
      f.string(b.token(JSONLexicalGrammar.STRING)));
  }

  public LiteralTree NUMBER() {
    return b.<LiteralTree>nonterminal().is(
      f.number(b.token(JSONLexicalGrammar.NUMBER)));
  }

  public LiteralTree FALSE() {
    return b.<LiteralTree>nonterminal().is(
      f.falsee(b.token(JSONLexicalGrammar.FALSE)));
  }

  public LiteralTree TRUE() {
    return b.<LiteralTree>nonterminal().is(
      f.truee(b.token(JSONLexicalGrammar.TRUE)));
  }

  public LiteralTree NULL() {
    return b.<LiteralTree>nonterminal().is(
      f.nulle(b.token(JSONLexicalGrammar.NULL)));
  }

}
