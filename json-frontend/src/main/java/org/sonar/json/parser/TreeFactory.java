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

import com.google.common.collect.Lists;
import com.sonar.sslr.api.typed.Optional;
import org.sonar.json.tree.impl.*;
import org.sonar.plugins.json.api.tree.*;

import java.util.List;

public class TreeFactory {

  public JsonTree json(Optional<SyntaxToken> byteOrderMark, Optional<ValueTree> value, SyntaxToken eof) {
    return new JsonTreeImpl(byteOrderMark.orNull(), value.orNull(), eof);
  }

  public ObjectTree object(InternalSyntaxToken leftBrace, Optional<SeparatedList<PairTree>> pairs, InternalSyntaxToken rightBrace) {
    return new ObjectTreeImpl(leftBrace, pairs.orNull(), rightBrace);
  }

  public ArrayTree array(InternalSyntaxToken leftBracket, Optional<SeparatedList<ValueTree>> values, InternalSyntaxToken rightBracket) {
    return new ArrayTreeImpl(leftBracket, values.orNull(), rightBracket);
  }

  public PairTree pair(KeyTree key, SyntaxToken colon, ValueTree value) {
    return new PairTreeImpl(key, colon, value);
  }

  public KeyTree key(SyntaxToken key) {
    return new KeyTreeImpl(key);
  }

  public ValueTree value(Tree value) {
    return new ValueTreeImpl(value);
  }

  public SeparatedList<ValueTree> valueList(ValueTree value, Optional<List<Tuple<InternalSyntaxToken, ValueTree>>> subsequentValues) {
    List<ValueTree> values = Lists.newArrayList();
    List<InternalSyntaxToken> commas = Lists.newArrayList();

    values.add(value);

    if (subsequentValues.isPresent()) {
      for (Tuple<InternalSyntaxToken, ValueTree> t : subsequentValues.get()) {
        commas.add(t.first());
        values.add(t.second());
      }
    }

    return new SeparatedList<>(values, commas);
  }

  public SeparatedList<PairTree> pairList(PairTree pair, Optional<List<Tuple<InternalSyntaxToken, PairTree>>> subsequentPairs) {
    List<PairTree> pairs = Lists.newArrayList();
    List<InternalSyntaxToken> commas = Lists.newArrayList();

    pairs.add(pair);

    if (subsequentPairs.isPresent()) {
      for (Tuple<InternalSyntaxToken, PairTree> t : subsequentPairs.get()) {
        commas.add(t.first());
        pairs.add(t.second());
      }
    }

    return new SeparatedList<>(pairs, commas);
  }

  public StringTree string(SyntaxToken token) {
    return new StringTreeImpl(token);
  }

  public LiteralTree number(SyntaxToken token) {
    return new NumberTreeImpl(token);
  }

  public LiteralTree falsee(SyntaxToken token) {
    return new FalseTreeImpl(token);
  }

  public LiteralTree truee(SyntaxToken token) {
    return new TrueTreeImpl(token);
  }

  public LiteralTree nul(SyntaxToken token) {
    return new NullTreeImpl(token);
  }

  public static class Tuple<T, U> {

    private final T first;
    private final U second;

    public Tuple(T first, U second) {
      super();

      this.first = first;
      this.second = second;
    }

    public T first() {
      return first;
    }

    public U second() {
      return second;
    }
  }

  private static <T, U> Tuple<T, U> newTuple(T first, U second) {
    return new Tuple<>(first, second);
  }

  public <T, U> Tuple<T, U> newTuple1(T first, U second) {
    return newTuple(first, second);
  }

  public <T, U> Tuple<T, U> newTuple2(T first, U second) {
    return newTuple(first, second);
  }

}
