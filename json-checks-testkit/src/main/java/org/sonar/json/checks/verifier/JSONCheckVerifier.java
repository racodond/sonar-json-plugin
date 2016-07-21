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
package org.sonar.json.checks.verifier;

import com.google.common.base.Charsets;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.ce.measure.test.TestIssue;
import org.sonar.json.visitors.CharsetAwareVisitor;
import org.sonar.plugins.json.api.JSONCheck;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

/**
 * To unit test checks.
 */
public class JSONCheckVerifier {

  private final List<TestIssue> expectedIssues = new ArrayList<>();

  /**
   * Check issues
   * @param check Check to test
   * @param file File to test
   *
   * Example:
   * <pre>
   * JSONCheckVerifier.issues(new MyCheck(), myFile, Charsets.UTF_8))
   *    .next().atLine(2).withMessage("This is message for line 2.")
   *    .next().atLine(3).withMessage("This is message for line 3.").withCost(2.)
   *    .next().atLine(8)
   *    .noMore();
   * </pre>
   */
  public static CheckMessagesVerifier issues(JSONCheck check, File file) {
    return issues(check, file, Charsets.UTF_8);
  }

  /**
   * See {@link JSONCheckVerifier#issues(JSONCheck, File)}
   * @param charset Charset of the file to test.
   */
  public static CheckMessagesVerifier issues(JSONCheck check, File file, Charset charset) {
    if (check instanceof CharsetAwareVisitor) {
      ((CharsetAwareVisitor) check).setCharset(charset);
    }
    return CheckMessagesVerifier.verify(TreeCheckTest.getIssues(file.getAbsolutePath(), check, charset));
  }

}
