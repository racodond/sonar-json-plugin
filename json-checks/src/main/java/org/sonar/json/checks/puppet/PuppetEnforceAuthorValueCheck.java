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
package org.sonar.json.checks.puppet;

import com.google.common.annotations.VisibleForTesting;
import com.sonar.sslr.api.AstNode;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.json.checks.CheckUtils;
import org.sonar.json.checks.Tags;
import org.sonar.json.parser.JSONGrammar;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "puppet-enforce-author-value",
  name = "\"author\" should match the required value in Puppet \"metadata.json\" files",
  priority = Priority.MAJOR,
  tags = {Tags.CONVENTION, Tags.PUPPET})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.DATA_RELIABILITY)
@SqaleConstantRemediation("5min")
public class PuppetEnforceAuthorValueCheck extends SquidCheck<LexerlessGrammar> {

  private static final String DEFAULT_AUTHOR = "John Doe";

  @RuleProperty(
    key = "value",
    defaultValue = "" + DEFAULT_AUTHOR,
    description = "Author to be set")
  private String author = DEFAULT_AUTHOR;

  @Override
  public void init() {
    subscribeTo(JSONGrammar.PAIR);
  }

  @Override
  public void visitNode(AstNode node) {
    if (PuppetCheckUtils.isMetadataJsonFile(getContext().getFile())
      && "author".equals(CheckUtils.getKeyNodeValue(node.getFirstChild(JSONGrammar.KEY)))
      && !author.equals(CheckUtils.getValueNodeStringValue(node.getFirstChild(JSONGrammar.VALUE)))) {
      getContext().createLineViolation(this, "Set the author to \"" + author + "\".", node.getFirstChild(JSONGrammar.VALUE));
    }
  }

  @VisibleForTesting
  public void setAuthor(String author) {
    this.author = author;
  }
}
