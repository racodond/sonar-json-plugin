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
package org.sonar.json.checks.generic;

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.json.checks.Tags;
import org.sonar.json.visitors.CharsetAwareVisitor;
import org.sonar.plugins.json.api.tree.JsonTree;
import org.sonar.plugins.json.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "bom-utf8-files",
  name = "BOM should not be used for UTF-8 files",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("5min")
@ActivatedByDefault
public class BOMCheck extends DoubleDispatchVisitorCheck implements CharsetAwareVisitor {

  private Charset charset;

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  @Override
  public void visitJson(JsonTree tree) {
    if (charset.equals(Charsets.UTF_8) && tree.hasByteOrderMark()) {
      addFileIssue("Remove the BOM.");
    }
    super.visitJson(tree);
  }

}
