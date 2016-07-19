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

import org.sonar.plugins.json.api.tree.KeyTree;
import org.sonar.plugins.json.api.tree.SyntaxToken;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitor;

public class KeyTreeImpl extends JSONTree implements KeyTree {

  private final SyntaxToken key;

  public KeyTreeImpl(SyntaxToken key) {
    this.key = key;
  }

  @Override
  public Tree.Kind getKind() {
    return Tree.Kind.KEY;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(key);
  }

  @Override
  public String actualText() {
    return key.text().substring(1, key.text().length() - 1);
  }

  @Override
  public SyntaxToken value() {
    return key;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitKey(this);
  }

}
