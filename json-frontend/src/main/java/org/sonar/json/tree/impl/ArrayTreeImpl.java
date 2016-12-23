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

import com.google.common.base.Functions;
import com.google.common.collect.Iterators;
import org.sonar.plugins.json.api.tree.ArrayTree;
import org.sonar.plugins.json.api.tree.SyntaxToken;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.tree.ValueTree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ArrayTreeImpl extends JSONTree implements ArrayTree {

  private final SyntaxToken leftBracket;
  private final SeparatedList<ValueTree> values;
  private final SyntaxToken rightBracket;

  public ArrayTreeImpl(SyntaxToken leftBracket, @Nullable SeparatedList<ValueTree> values, SyntaxToken rightBracket) {
    this.leftBracket = leftBracket;
    this.values = values;
    this.rightBracket = rightBracket;
  }

  @Override
  public Kind getKind() {
    return Kind.ARRAY;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      Iterators.singletonIterator(leftBracket),
      values != null ? values.elementsAndSeparators(Functions.identity()) : new ArrayList<Tree>().iterator(),
      Iterators.singletonIterator(rightBracket));
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitArray(this);
  }

  @Override
  public SyntaxToken leftBracket() {
    return leftBracket;
  }

  @Override
  public SyntaxToken rightBracket() {
    return rightBracket;
  }

  @Override
  public List<ValueTree> elements() {
    return values != null ? values : Collections.emptyList();
  }

}
