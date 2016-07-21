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

import org.sonar.plugins.json.api.tree.ArrayTree;
import org.sonar.plugins.json.api.tree.SyntaxToken;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.tree.ValueTree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitor;

public class ArrayTreeImpl extends JSONTree implements ArrayTree {

  private final SyntaxToken leftBracket;
  private final SyntaxList<ValueTree> elementSyntaxList;
  private final List<ValueTree> elements;
  private final List<Tree> allTrees;
  private final SyntaxToken rightBracket;

  public ArrayTreeImpl(SyntaxToken leftBracket, @Nullable SyntaxList<ValueTree> elementSyntaxList, SyntaxToken rightBracket) {
    this.leftBracket = leftBracket;
    this.elementSyntaxList = elementSyntaxList;
    this.elements = buildElementList();
    this.allTrees = buildAllTreeList();
    this.rightBracket = rightBracket;
  }

  @Override
  public Kind getKind() {
    return Kind.ARRAY;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      Iterators.forArray(leftBracket, rightBracket),
      allTrees.iterator());
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitArray(this);
  }

  @Override
  public SyntaxToken leftBracket() {
    return leftBracket;
  }

  @Nullable
  @Override
  public SyntaxList<ValueTree> elementSyntaxList() {
    return elementSyntaxList;
  }

  @Override
  public SyntaxToken rightBracket() {
    return rightBracket;
  }

  @Override
  public List<ValueTree> elements() {
    return elements;
  }

  private List<ValueTree> buildElementList() {
    List<ValueTree> elementList = new ArrayList<>();

    if (elementSyntaxList != null) {
      elementList.add(elementSyntaxList.element());

      SyntaxList<ValueTree> nextSyntaxList = elementSyntaxList.next();
      while (nextSyntaxList != null) {
        elementList.add(nextSyntaxList.element());
        nextSyntaxList = nextSyntaxList.next();
      }
    }

    return elementList;
  }

  private List<Tree> buildAllTreeList() {
    List<Tree> all = new ArrayList<>();

    if (elementSyntaxList != null) {
      all.add(elementSyntaxList.element());
      if (elementSyntaxList.commaToken() != null) {
        all.add(elementSyntaxList.commaToken());
      }

      SyntaxList<ValueTree> nextSyntaxList = elementSyntaxList.next();
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
