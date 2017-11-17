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

import com.google.common.collect.ImmutableList;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.plugins.json.api.tree.Tree;
import org.sonar.plugins.json.api.visitors.SubscriptionVisitor;

import java.io.Serializable;
import java.util.List;

public class MetricsVisitor extends SubscriptionVisitor {

  private final SensorContext sensorContext;
  private final FileSystem fileSystem;
  private InputFile inputFile;

  public MetricsVisitor(SensorContext sensorContext) {
    this.sensorContext = sensorContext;
    this.fileSystem = sensorContext.fileSystem();
  }

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.JSON);
  }

  @Override
  public void visitFile(Tree tree) {
    this.inputFile = fileSystem.inputFile(fileSystem.predicates().is(getContext().getFile()));
  }

  @Override
  public void leaveFile(Tree tree) {
    LinesOfCodeVisitor linesOfCodeVisitor = new LinesOfCodeVisitor(tree);
    saveMetricOnFile(CoreMetrics.NCLOC, linesOfCodeVisitor.getNumberOfLinesOfCode());

    StatementsVisitor statementsVisitor = new StatementsVisitor(tree);
    saveMetricOnFile(CoreMetrics.STATEMENTS, statementsVisitor.getNumberOfStatements());
  }

  private <T extends Serializable> void saveMetricOnFile(Metric metric, T value) {
    sensorContext.<T>newMeasure()
      .withValue(value)
      .forMetric(metric)
      .on(inputFile)
      .save();
  }

}
