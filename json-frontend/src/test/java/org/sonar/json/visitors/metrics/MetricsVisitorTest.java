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
package org.sonar.json.visitors.metrics;

import com.google.common.base.Charsets;

import java.io.File;

import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.json.parser.JSONParserBuilder;
import org.sonar.plugins.json.api.tree.JsonTree;
import org.sonar.plugins.json.api.visitors.TreeVisitorContext;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetricsVisitorTest {

  @Test
  public void test() {
    File moduleBaseDir = new File("src/test/resources/");
    SensorContextTester context = SensorContextTester.create(moduleBaseDir);

    DefaultInputFile inputFile = new DefaultInputFile("moduleKey", "metrics.json")
      .setModuleBaseDir(moduleBaseDir.toPath())
      .setLanguage("json")
      .setType(InputFile.Type.MAIN);

    context.fileSystem().add(inputFile);

    MetricsVisitor metricsVisitor = new MetricsVisitor(context);

    TreeVisitorContext treeVisitorContext = mock(TreeVisitorContext.class);
    when(treeVisitorContext.getFile()).thenReturn(inputFile.file());
    when(treeVisitorContext.getTopTree()).thenReturn((JsonTree) JSONParserBuilder.createParser(Charsets.UTF_8).parse(inputFile.file()));

    metricsVisitor.scanTree(treeVisitorContext);

    String componentKey = "moduleKey:metrics.json";
    assertThat(context.measure(componentKey, CoreMetrics.NCLOC).value()).isEqualTo(6);
    assertThat(context.measure(componentKey, CoreMetrics.STATEMENTS).value()).isEqualTo(7);
    assertThat(context.measure(componentKey, CoreMetrics.CLASSES).value()).isEqualTo(3);
  }

}
