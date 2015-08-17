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

import com.sonar.sslr.api.AstNode;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.json.checks.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = PuppetMetadataFilePresentCheck.RULE_KEY,
  name = "\"metadata.json\" file should be present",
  priority = Priority.MAJOR,
  tags = {Tags.CONVENTION, Tags.PUPPET})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.DATA_RELIABILITY)
@SqaleConstantRemediation("5min")
public class PuppetMetadataFilePresentCheck extends SquidCheck<LexerlessGrammar> {

  public static final String RULE_KEY = "puppet-metadata-present";

  private boolean metadataJsonFileFound = false;

  @Override
  public void visitFile(AstNode node) {
    if ("metadata.json".equals(getContext().getFile().getName())) {
      metadataJsonFileFound = true;
    }
  }

  public boolean isMetadataJsonFileFound() {
    return metadataJsonFileFound;
  }

}
