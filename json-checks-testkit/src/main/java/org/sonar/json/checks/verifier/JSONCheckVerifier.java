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
package org.sonar.json.checks.verifier;

import com.google.common.base.Charsets;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sonar.json.parser.JSONParserBuilder;
import org.sonar.json.visitors.CharsetAwareVisitor;
import org.sonar.json.visitors.JSONVisitorContext;
import org.sonar.plugins.json.api.JSONCheck;
import org.sonar.plugins.json.api.tree.JsonTree;
import org.sonar.plugins.json.api.visitors.issue.*;

/**
 * To unit test checks.
 */
public class JSONCheckVerifier {

  private JSONCheckVerifier() {
  }

  /**
   * Check verify
   * @param check Check to test
   * @param file File to test
   *
   * Example:
   * <pre>
   * JSONCheckVerifier.verify(new MyCheck(), myFile, Charsets.UTF_8))
   *    .next().startAtLine(2).withMessage("This is message for line 2.")
   *    .next().startAtLine(3).withMessage("This is message for line 3.").withCost(2.)
   *    .next().startAtLine(8).startAtColumn(4)
   *    .noMore();
   * </pre>
   */
  public static CheckVerifier verify(JSONCheck check, File file) {
    return verify(check, file, Charsets.UTF_8);
  }

  /**
   * See {@link JSONCheckVerifier#verify(JSONCheck, File)}
   * @param charset Charset of the file to test.
   */
  public static CheckVerifier verify(JSONCheck check, File file, Charset charset) {
    if (check instanceof CharsetAwareVisitor) {
      ((CharsetAwareVisitor) check).setCharset(charset);
    }
    return CheckVerifier.verify(getTestIssues(file.getAbsolutePath(), check, charset));
  }

  private static Collection<TestIssue> getTestIssues(String relativePath, JSONCheck check, Charset charset) {
    File file = new File(relativePath);

    JsonTree jsonTree = (JsonTree) JSONParserBuilder.createParser(charset).parse(file);
    JSONVisitorContext context = new JSONVisitorContext(jsonTree, file);
    List<Issue> issues = check.scanFile(context);

    List<TestIssue> testIssues = new ArrayList<>();
    for (Issue issue : issues) {
      TestIssue testIssue;

      if (issue instanceof FileIssue) {
        FileIssue fileIssue = (FileIssue) issue;
        testIssue = new TestIssue(fileIssue.message());
        for (IssueLocation issueLocation : fileIssue.secondaryLocations()) {
          testIssue.addSecondaryLine(issueLocation.startLine());
        }

      } else if (issue instanceof LineIssue) {
        LineIssue lineIssue = (LineIssue) issue;
        testIssue = new TestIssue(lineIssue.message())
          .starLine(lineIssue.line());

      } else {
        PreciseIssue preciseIssue = (PreciseIssue) issue;
        testIssue = new TestIssue(preciseIssue.primaryLocation().message())
          .starLine(preciseIssue.primaryLocation().startLine())
          .startColumn(preciseIssue.primaryLocation().startLineOffset() + 1)
          .endLine(preciseIssue.primaryLocation().endLine())
          .endColumn(preciseIssue.primaryLocation().endLineOffset() + 1);
        for (IssueLocation issueLocation : preciseIssue.secondaryLocations()) {
          testIssue.addSecondaryLine(issueLocation.startLine());
        }
      }

      if (issue.cost() != null) {
        testIssue.cost(issue.cost());
      }

      testIssues.add(testIssue);
    }

    return testIssues;
  }

}
