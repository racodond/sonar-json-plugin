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
package org.sonar.json.tree.impl;

import java.util.Iterator;

import org.sonar.plugins.json.api.tree.SyntaxToken;
import org.sonar.plugins.json.api.tree.Tree;

public abstract class JSONTree implements Tree {

  public int getLine() {
    return getFirstToken().line();
  }

  @Override
  public final boolean is(Tree.Kind... kind) {
    if (getKind() != null) {
      for (Kind kindIter : kind) {
        if (getKind() == kindIter) {
          return true;
        }
      }
    }
    return false;
  }

  public abstract Kind getKind();

  /**
   * Creates iterator for children of this node.
   * Note that iterator may contain {@code null} elements.
   *
   * @throws UnsupportedOperationException if {@link #isLeaf()} returns {@code true}
   */
  public abstract Iterator<Tree> childrenIterator();

  public boolean isLeaf() {
    return false;
  }

  public SyntaxToken getLastToken() {
    SyntaxToken lastToken = null;
    Iterator<Tree> childrenIterator = childrenIterator();
    while (childrenIterator.hasNext()) {
      JSONTree child = (JSONTree) childrenIterator.next();
      if (child != null) {
        SyntaxToken childLastToken = child.getLastToken();
        if (childLastToken != null) {
          lastToken = childLastToken;
        }
      }
    }
    return lastToken;
  }

  public SyntaxToken getFirstToken() {
    Iterator<Tree> childrenIterator = childrenIterator();
    Tree child;
    do {
      if (childrenIterator.hasNext()) {
        child = childrenIterator.next();
      } else {
        throw new IllegalStateException("Tree has no non-null children " + getKind());
      }
    } while (child == null);
    return ((JSONTree) child).getFirstToken();
  }
}
