/*
 * SonarQube JSON Analyzer
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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.json.checks.Tags;
import org.sonar.plugins.json.api.tree.JsonTree;
import org.sonar.plugins.json.api.tree.KeyTree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "puppet-required-keys",
  name = "Puppet \"metadata.json\" files should define all the required keys",
  priority = Priority.MAJOR,
  tags = {Tags.CONVENTION, Tags.PUPPET})
@SqaleConstantRemediation("10min")
public class PuppetRequiredKeysCheck extends DoubleDispatchVisitorCheck {

  private static final List<String> REQUIRED_KEYS = ImmutableList.of("name", "version", "author", "license", "summary", "source", "dependencies");
  private final List<String> definedKeys = new ArrayList<>();
  private final List<String> missingKeys = new ArrayList<>();

  @Override
  public void visitJson(JsonTree tree) {
    definedKeys.clear();
    missingKeys.clear();

    if ("metadata.json".equals(getContext().getFile().getName())) {
      super.visitJson(tree);

      for (String requiredKey : REQUIRED_KEYS) {
        if (!definedKeys.contains(requiredKey)) {
          missingKeys.add(requiredKey);
        }
      }

      if (!missingKeys.isEmpty()) {
        addFileIssue("Add the following keys that are required: " + Joiner.on(", ").join(missingKeys) + ".");
      }
    }
  }

  @Override
  public void visitKey(KeyTree key) {
    definedKeys.add(key.actualText());
    super.visitKey(key);
  }

}
