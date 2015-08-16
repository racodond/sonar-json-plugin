/*
 * SonarQube JSON Plugin
 * Copyright (C) 2015 David RACODON
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.json;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.sonar.sslr.impl.Parser;

import java.io.File;
import java.util.Collection;
import javax.annotation.Nullable;

import org.sonar.json.api.JSONMetric;
import org.sonar.json.ast.visitors.CharsetAwareVisitor;
import org.sonar.json.ast.visitors.SonarComponents;
import org.sonar.json.ast.visitors.SyntaxHighlighterVisitor;
import org.sonar.json.parser.JSONGrammar;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.squidbridge.metrics.CommentsVisitor;
import org.sonar.squidbridge.metrics.CounterVisitor;
import org.sonar.squidbridge.metrics.LinesOfCodeVisitor;
import org.sonar.squidbridge.metrics.LinesVisitor;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.parser.ParserAdapter;

public final class JSONAstScanner {

  private JSONAstScanner() {
  }

  @VisibleForTesting
  public static SourceFile scanSingleFile(File file, SquidAstVisitor<LexerlessGrammar>... visitors) {
    if (!file.isFile()) {
      throw new IllegalArgumentException("File '" + file + "' not found.");
    }
    AstScanner scanner = create(new JSONConfiguration(Charsets.UTF_8), null, visitors);
    scanner.scanFile(file);
    Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
    if (sources.size() != 1) {
      throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
    }
    return (SourceFile) sources.iterator().next();
  }

  public static AstScanner<LexerlessGrammar> create(JSONConfiguration conf, @Nullable SonarComponents sonarComponents, SquidAstVisitor<LexerlessGrammar>... visitors) {
    final SquidAstVisitorContextImpl<LexerlessGrammar> context = new SquidAstVisitorContextImpl<LexerlessGrammar>(new SourceProject("JSON Project"));
    final Parser<LexerlessGrammar> parser = new ParserAdapter(conf.getCharset(), JSONGrammar.createGrammar());

    AstScanner.Builder<LexerlessGrammar> builder = AstScanner.builder(context).setBaseParser(parser);

    builder.withMetrics(JSONMetric.values());
    builder.setFilesMetric(JSONMetric.FILES);
    builder.withSquidAstVisitor(new LinesVisitor<LexerlessGrammar>(JSONMetric.LINES));
    builder.withSquidAstVisitor(new LinesOfCodeVisitor<LexerlessGrammar>(JSONMetric.LINES_OF_CODE));

    builder.setCommentAnalyser(new JSONCommentAnalyser());
    builder.withSquidAstVisitor(CommentsVisitor.<LexerlessGrammar>builder()
      .withCommentMetric(JSONMetric.COMMENT_LINES).build());

    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
      .setMetricDef(JSONMetric.CLASSES)
      .subscribeTo(JSONGrammar.OBJECT)
      .build());

    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
      .setMetricDef(JSONMetric.STATEMENTS)
      .subscribeTo(JSONGrammar.PAIR)
      .build());

    if (sonarComponents != null) {
      builder.withSquidAstVisitor(new SyntaxHighlighterVisitor(sonarComponents, conf.getCharset()));
    }

    for (SquidAstVisitor<LexerlessGrammar> visitor : visitors) {
      if (visitor instanceof CharsetAwareVisitor) {
        ((CharsetAwareVisitor) visitor).setCharset(conf.getCharset());
      }
      builder.withSquidAstVisitor(visitor);
    }

    return builder.build();
  }
}
