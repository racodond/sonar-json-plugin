/*
 * SonarQube JSON Analyzer
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
package org.sonar.json.visitors;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.plugins.json.api.tree.KeyTree;
import org.sonar.plugins.json.api.tree.LiteralTree;
import org.sonar.plugins.json.api.tree.SyntaxToken;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.visitors.SubscriptionVisitor;

public class SyntaxHighlighterVisitor extends SubscriptionVisitor {

  private final SensorContext sensorContext;
  private final FileSystem fileSystem;
  private NewHighlighting highlighting;

  public SyntaxHighlighterVisitor(SensorContext sensorContext) {
    this.sensorContext = sensorContext;
    fileSystem = sensorContext.fileSystem();
  }

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.<Tree.Kind>builder()
      .add(Tree.Kind.KEY)
      .add(Tree.Kind.STRING)
      .add(Tree.Kind.NUMBER)
      .add(Tree.Kind.TRUE)
      .add(Tree.Kind.FALSE)
      .add(Tree.Kind.NULL)
      .build();
  }

  @Override
  public void visitFile(Tree tree) {
    highlighting = sensorContext.newHighlighting().onFile(fileSystem.inputFile(fileSystem.predicates().is(getContext().getFile())));
  }

  @Override
  public void leaveFile(Tree scriptTree) {
    highlighting.save();
  }

  @Override
  public void visitNode(Tree tree) {
    SyntaxToken token = null;
    TypeOfText code = null;

    if (tree.is(Tree.Kind.KEY)) {
      token = ((KeyTree) tree).value();
      code = TypeOfText.KEYWORD;

    } else if (tree.is(Tree.Kind.STRING)) {
      token = ((LiteralTree) tree).value();
      code = TypeOfText.STRING;

    } else if (tree.is(Tree.Kind.FALSE, Tree.Kind.TRUE, Tree.Kind.NULL, Tree.Kind.NUMBER)) {
      token = ((LiteralTree) tree).value();
      code = TypeOfText.CONSTANT;
    }

    if (token != null) {
      highlight(token, code);
    }
  }

  private void highlight(SyntaxToken token, TypeOfText type) {
    highlighting.highlight(token.line(), token.column(), token.endLine(), token.endColumn(), type);
  }

}
