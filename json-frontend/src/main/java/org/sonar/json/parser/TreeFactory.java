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

import com.sonar.sslr.api.typed.Optional;
import org.sonar.json.tree.impl.*;
import org.sonar.plugins.json.api.tree.*;

public class TreeFactory {

  public JsonTree json(Optional<SyntaxToken> byteOrderMark, Optional<ValueTree> value, SyntaxToken eof) {
    return new JsonTreeImpl(byteOrderMark.orNull(), value.orNull(), eof);
  }

  public ObjectTree object(InternalSyntaxToken leftBrace, Optional<SyntaxList<PairTree>> pairSyntaxList, InternalSyntaxToken rightBrace) {
    return new ObjectTreeImpl(leftBrace, pairSyntaxList.orNull(), rightBrace);
  }

  public ArrayTree array(InternalSyntaxToken leftBracket, Optional<SyntaxList<ValueTree>> elementSyntaxList, InternalSyntaxToken rightBracket) {
    return new ArrayTreeImpl(leftBracket, elementSyntaxList.orNull(), rightBracket);
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

  public SyntaxList<ValueTree> valueList(ValueTree value) {
    return new SyntaxList<>(value, null, null);
  }

  public SyntaxList<ValueTree> valueList(ValueTree value, InternalSyntaxToken commaToken, SyntaxList<ValueTree> next) {
    return new SyntaxList<>(value, commaToken, next);
  }

  public SyntaxList<PairTree> pairList(PairTree pair) {
    return new SyntaxList<>(pair, null, null);
  }

  public SyntaxList<PairTree> pairList(PairTree pair, InternalSyntaxToken commaToken, SyntaxList<PairTree> next) {
    return new SyntaxList<>(pair, commaToken, next);
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

  public LiteralTree nulle(SyntaxToken token) {
    return new NullTreeImpl(token);
  }

}
