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
package org.sonar.json.parser;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.RecognitionException;
import org.junit.Test;
import org.sonar.plugins.json.api.tree.SyntaxToken;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class LeftBraceTreeTest {

  @Test
  public void leftBrace() {
    checkParsed("{");
    checkParsed(" {");
    checkParsed("  {");
  }

  @Test
  public void notLeftBrace() {
    checkNotParsed("[");
    checkNotParsed("}");
  }

  private SyntaxToken parse(String toParse) {
    return (SyntaxToken) JSONParserBuilder
      .createTestParser(Charsets.UTF_8, JSONLexicalGrammar.LEFT_BRACE)
      .parse(toParse);
  }

  private void checkParsed(String toParse) {
    SyntaxToken tree = parse(toParse);
    assertThat(tree.text()).isEqualTo("{");
  }

  private void checkNotParsed(String toParse) {
    try {
      parse(toParse);
    } catch (RecognitionException e) {
      return;
    }
    fail("Did not throw a RecognitionException as expected.");
  }

}
