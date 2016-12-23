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
import org.sonar.plugins.json.api.tree.ObjectTree;
import org.sonar.plugins.json.api.tree.PairTree;
import org.sonar.plugins.json.api.tree.SyntaxToken;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ObjectTreeImpl extends JSONTree implements ObjectTree {

  private final SyntaxToken leftBrace;
  private final SeparatedList<PairTree> pairs;
  private final SyntaxToken rightBrace;

  public ObjectTreeImpl(SyntaxToken leftBrace, @Nullable SeparatedList<PairTree> pairs, SyntaxToken rightBrace) {
    this.leftBrace = leftBrace;
    this.pairs = pairs;
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
      pairs != null ? pairs.elementsAndSeparators(Functions.identity()) : new ArrayList<Tree>().iterator(),
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
    return pairs != null ? pairs : Collections.emptyList();
  }

}
