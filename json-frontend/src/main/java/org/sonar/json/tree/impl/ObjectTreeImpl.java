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
package org.sonar.json.tree.impl;

import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import org.sonar.plugins.json.api.tree.*;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitor;

public class ObjectTreeImpl extends JSONTree implements ObjectTree {

  private final SyntaxToken leftBrace;
  private final SyntaxList<PairTree> pairSyntaxList;
  private final List<PairTree> pairs;
  private final List<Tree> allTrees;
  private final SyntaxToken rightBrace;

  public ObjectTreeImpl(SyntaxToken leftBrace, @Nullable SyntaxList<PairTree> pairSyntaxList, SyntaxToken rightBrace) {
    this.leftBrace = leftBrace;
    this.pairSyntaxList = pairSyntaxList;
    this.pairs = buildPairList();
    this.allTrees = buildAllTreeList();
    this.rightBrace = rightBrace;
  }

  @Override
  public Kind getKind() {
    return Kind.OBJECT;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      Iterators.singletonIterator(leftBrace),
      allTrees.iterator(),
      Iterators.singletonIterator(rightBrace));
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitObject(this);
  }

  @Override
  public SyntaxToken leftBrace() {
    return leftBrace;
  }

  @Override
  public SyntaxToken rightBrace() {
    return rightBrace;
  }

  @Override
  public List<PairTree> pairs() {
    return pairs;
  }

  private List<PairTree> buildPairList() {
    List<PairTree> pairList = new ArrayList<>();

    if (pairSyntaxList != null) {
      pairList.add(pairSyntaxList.element());

      SyntaxList<PairTree> nextSyntaxList = pairSyntaxList.next();
      while (nextSyntaxList != null) {
        pairList.add(nextSyntaxList.element());
        nextSyntaxList = nextSyntaxList.next();
      }
    }

    return pairList;
  }

  private List<Tree> buildAllTreeList() {
    List<Tree> all = new ArrayList<>();

    if (pairSyntaxList != null) {
      all.add(pairSyntaxList.element());
      if (pairSyntaxList.commaToken() != null) {
        all.add(pairSyntaxList.commaToken());
      }

      SyntaxList<ValueTree> nextSyntaxList = pairSyntaxList.next();
      while (nextSyntaxList != null) {
        all.add(nextSyntaxList.element());
        if (nextSyntaxList.commaToken() != null) {
          all.add(nextSyntaxList.commaToken());
        }
        nextSyntaxList = nextSyntaxList.next();
      }
    }

    return all;
  }

}
