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

import com.google.common.annotations.VisibleForTesting;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.json.checks.Tags;
import org.sonar.plugins.json.api.tree.PairTree;
import org.sonar.plugins.json.api.tree.StringTree;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.tree.ValueTree;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "puppet-enforce-license-value",
  name = "\"license\" should match the required value in Puppet \"metadata.json\" files",
  priority = Priority.MAJOR,
  tags = {Tags.CONVENTION, Tags.PUPPET})
@SqaleConstantRemediation("5min")
public class PuppetEnforceLicenseValueCheck extends AbstractPuppetCheck {

  private static final String DEFAULT_LICENSE = "LGPL-3.0";

  @RuleProperty(
    key = "value",
    defaultValue = "" + DEFAULT_LICENSE,
    description = "License to be set")
  private String license = DEFAULT_LICENSE;

  @Override
  public void visitPair(PairTree pair) {
    if ("license".equals(pair.key().actualText())) {
      if (!pair.value().value().is(Tree.Kind.STRING)) {
        createIssue(pair.value());
      } else {
        if (!license.equals(((StringTree) pair.value().value()).actualText())) {
          createIssue(pair.value());
        }
      }
    }
    super.visitPair(pair);
  }

  @VisibleForTesting
  public void setLicense(String license) {
    this.license = license;
  }

  private void createIssue(ValueTree tree) {
    addPreciseIssue(tree, "Set the license to \"" + license + "\".");
  }

}
