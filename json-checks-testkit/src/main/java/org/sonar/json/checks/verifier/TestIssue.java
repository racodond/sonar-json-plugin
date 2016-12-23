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
package org.sonar.json.checks.verifier;

import java.util.ArrayList;
import java.util.List;

public class TestIssue {

  private Integer startLine;
  private Integer startColumn;
  private Integer endLine;
  private Integer endColumn;
  private Double cost;
  private String message;
  private List<Integer> secondaryLines;

  public TestIssue(String message) {
    this.message = message;
    secondaryLines = new ArrayList<>();
  }

  public String message() {
    return message;
  }

  public Integer starLine() {
    return startLine;
  }

  public TestIssue starLine(int starLine) {
    this.startLine = starLine;
    return this;
  }

  public Integer endLine() {
    return endLine;
  }

  public TestIssue endLine(int endLine) {
    this.endLine = endLine;
    return this;
  }

  public Integer startColumn() {
    return startColumn;
  }

  public TestIssue startColumn(int startColumn) {
    this.startColumn = startColumn;
    return this;
  }

  public Integer endColumn() {
    return endColumn;
  }

  public TestIssue endColumn(int endColumn) {
    this.endColumn = endColumn;
    return this;
  }

  public TestIssue cost(double cost) {
    this.cost = cost;
    return this;
  }

  public Double cost() {
    return cost;
  }

  public List<Integer> secondaryLines() {
    return secondaryLines;
  }

  public TestIssue addSecondaryLine(int line) {
    secondaryLines.add(line);
    return this;
  }

}
