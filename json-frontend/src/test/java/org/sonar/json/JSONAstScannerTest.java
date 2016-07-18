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
package org.sonar.json;

import java.io.File;

import org.junit.Test;
import org.sonar.json.api.JSONMetric;
import org.sonar.squidbridge.api.SourceFile;

import static org.fest.assertions.Assertions.assertThat;

public class JSONAstScannerTest {

  private final static String PATH = "src/test/resources/sample.json";

  @Test
  public void lines() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File(PATH));
    assertThat(file.getInt(JSONMetric.LINES)).isEqualTo(9);
  }

  @Test
  public void lines_of_code() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File(PATH));
    assertThat(file.getInt(JSONMetric.LINES_OF_CODE)).isEqualTo(6);
  }

  @Test
  public void statements() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File(PATH));
    assertThat(file.getInt(JSONMetric.STATEMENTS)).isEqualTo(7);
  }

  @Test
  public void comments() {
    SourceFile file = JSONAstScanner.scanSingleFile(new File(PATH));
    assertThat(file.getInt(JSONMetric.CLASSES)).isEqualTo(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void file_not_found() {
    JSONAstScanner.scanSingleFile(new File("blabla"));
  }

}
