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
import org.junit.Test;
import org.sonar.plugins.json.api.tree.SyntaxToken;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NumberTreeTest extends CommonSyntaxTokenTreeTest {

  public NumberTreeTest() {
    super(JSONLexicalGrammar.NUMBER);
  }

  @Test
  public void number() {
    checkParsed("0");
    checkParsed(" 0", "0");
    checkParsed("  0", "0");
    checkParsed("1");
    checkParsed("123");
    checkParsed("0.5");
    checkParsed("1.4");
    checkParsed("10.99");
    checkParsed("-1");
    checkParsed("-123");
    checkParsed("-0.5");
    checkParsed("-1.4");
    checkParsed("-10.99");
    checkParsed("1e10");
    checkParsed("1E10");
    checkParsed("1e-10");
    checkParsed("1E-10");
    checkParsed("1e+10");
    checkParsed("1E+10");
    checkParsed("-1e10");
    checkParsed("-1E10");
    checkParsed("-1e-10");
    checkParsed("-1E-10");
    checkParsed("-1e+10");
    checkParsed("-1E+10");
    checkParsed("10.5e10");
    checkParsed("10.5E10");
    checkParsed("10.5e-10");
    checkParsed("10.5E-10");
    checkParsed("10.5e+10");
    checkParsed("10.5E+10");
    checkParsed("-10.5e10");
    checkParsed("-10.5E10");
    checkParsed("-10.5e-10");
    checkParsed("-10.5E-10");
    checkParsed("-10.5e+10");
    checkParsed("-10.5E+10");
  }

  @Test
  public void notNumber() {
    checkNotParsed("abc");
    checkNotParsed("+1");
    checkNotParsed("+1.5.");
    checkNotParsed("0.");
    checkNotParsed("1.");
  }

  @Override
  public void checkNotParsed(String toParse) {
    try {
      SyntaxToken tree = (SyntaxToken) JSONParserBuilder.createTestParser(Charsets.UTF_8, JSONLexicalGrammar.NUMBER);
      if (!tree.text().equals(toParse)) {
        assertTrue(true);
        return;
      }
    } catch (Exception e) {
      return;
    }
    fail("Did not throw a RecognitionException as expected.");
  }

}
