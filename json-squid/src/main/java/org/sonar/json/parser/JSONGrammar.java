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
package org.sonar.json.parser;

import com.sonar.sslr.api.GenericTokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.LexerlessGrammar;

public enum JSONGrammar implements GrammarRuleKey {

  JSON,
  OBJECT,
  MEMBERS,
  PAIR,
  KEY,
  ARRAY,
  ELEMENTS,
  VALUE,

  STRING,
  NUMBER,
  TRUE,
  FALSE,
  NULL,

  LBRACKET,
  RBRACKET,
  LBRACE,
  RBRACE,
  COLON,
  COMMA,

  WHITESPACES,
  EOF;

  public static LexerlessGrammar createGrammar() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();
    syntax(b);
    b.setRootRule(JSON);
    return b.build();
  }

  private static void syntax(LexerlessGrammarBuilder b) {
    b.rule(JSON).is(OBJECT, EOF);

    b.rule(OBJECT).is(b.optional(WHITESPACES), LBRACE, b.optional(MEMBERS), RBRACE);
    b.rule(MEMBERS).is(PAIR, b.zeroOrMore(COMMA, PAIR)).skip();
    b.rule(PAIR).is(KEY, COLON, VALUE);
    b.rule(KEY).is(STRING);

    b.rule(ARRAY).is(b.optional(WHITESPACES), LBRACKET, b.optional(ELEMENTS), RBRACKET);
    b.rule(ELEMENTS).is(VALUE, b.zeroOrMore(COMMA, VALUE)).skip();

    b.rule(VALUE).is(b.firstOf(TRUE, FALSE, NULL, NUMBER, STRING, OBJECT, ARRAY));
    b.rule(TRUE).is(b.optional(WHITESPACES), "true");
    b.rule(FALSE).is(b.optional(WHITESPACES), "false");
    b.rule(NULL).is(b.optional(WHITESPACES), "null");
    b.rule(NUMBER).is(b.optional(WHITESPACES), b.regexp("[-]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));

    // TODO: Check this regular expression, for example \ should not be allowed by itself
    b.rule(STRING).is(b.optional(WHITESPACES), b.regexp("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""));

    b.rule(COMMA).is(b.optional(WHITESPACES), ",");
    b.rule(COLON).is(b.optional(WHITESPACES), ":");
    b.rule(LBRACE).is(b.optional(WHITESPACES), "{");
    b.rule(RBRACE).is(b.optional(WHITESPACES), "}");
    b.rule(LBRACKET).is(b.optional(WHITESPACES), "[");
    b.rule(RBRACKET).is(b.optional(WHITESPACES), "]");

    b.rule(WHITESPACES).is(b.zeroOrMore(b.skippedTrivia(b.regexp("(?<!\\\\)[\\s]+")))).skip();
    b.rule(EOF).is(b.optional(WHITESPACES), b.token(GenericTokenType.EOF, b.endOfInput())).skip();
  }
}
