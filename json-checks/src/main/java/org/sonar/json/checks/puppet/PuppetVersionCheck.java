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
package org.sonar.json.checks.puppet;

import java.util.regex.Pattern;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.json.checks.Tags;
import org.sonar.plugins.json.api.tree.PairTree;
import org.sonar.plugins.json.api.tree.StringTree;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.tree.ValueTree;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "puppet-version",
  name = "\"version\" should be a semantic version in Puppet \"metadata.json\" files",
  priority = Priority.MAJOR,
  tags = {Tags.CONVENTION, Tags.PUPPET})
@SqaleConstantRemediation("5min")
public class PuppetVersionCheck extends AbstractPuppetCheck {

  private static final Pattern SEMANTIC_VERSION_PATTERN = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");

  @Override
  public void visitPair(PairTree pair) {
    if ("version".equals(pair.key().actualText())) {
      if (!pair.value().value().is(Tree.Kind.STRING)) {
        createIssue(pair.value());
      } else {
        if (!SEMANTIC_VERSION_PATTERN.matcher(((StringTree) pair.value().value()).actualText()).matches()) {
          createIssue(pair.value());
        }
      }
    }
    super.visitPair(pair);
  }

  private void createIssue(ValueTree tree) {
    addPreciseIssue(tree, "Define the version as a semantic version on 3 digits separated by dots: ^\\d+\\.\\d+\\.\\d+$");
  }

}
