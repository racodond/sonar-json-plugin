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

import java.util.Iterator;

import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.tree.ValueTree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitor;

public class ValueTreeImpl extends JSONTree implements ValueTree {

  private final Tree value;

  public ValueTreeImpl(Tree value) {
    this.value = value;
  }

  @Override
  public Kind getKind() {
    return Kind.VALUE;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.singletonIterator(value);
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitValue(this);
  }

  @Override
  public Tree value() {
    return value;
  }

}
