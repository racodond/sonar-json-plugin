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

import com.google.common.collect.Iterators;

import java.util.Iterator;
import javax.annotation.Nullable;

import org.sonar.plugins.json.api.tree.JsonTree;
import org.sonar.plugins.json.api.tree.SyntaxToken;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.tree.ValueTree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitor;

public class JsonTreeImpl extends JSONTree implements JsonTree {

  private final SyntaxToken byteOrderMark;
  private final SyntaxToken eof;
  private final ValueTree value;

  public JsonTreeImpl(@Nullable SyntaxToken byteOrderMark, @Nullable ValueTree value, SyntaxToken eof) {
    this.byteOrderMark = byteOrderMark;
    this.value = value;
    this.eof = eof;
  }

  @Override
  public Kind getKind() {
    return Kind.JSON;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(byteOrderMark, value, eof);
  }

  @Override
  public boolean hasByteOrderMark() {
    return byteOrderMark != null;
  }

  @Override
  public ValueTree value() {
    return value;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitJson(this);
  }

}
