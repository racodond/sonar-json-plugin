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
package org.sonar.json.checks.generic;

import com.google.common.base.Charsets;
import org.junit.Test;
import org.sonar.json.checks.CheckTestUtils;
import org.sonar.json.checks.verifier.JSONCheckVerifier;

public class BOMCheckTest {

  @Test
  public void should_find_a_BOM_in_UTF8_file_and_raise_an_issue() {
    JSONCheckVerifier.verify(new BOMCheck(), CheckTestUtils.getTestFile("utf8WithBOM.json"))
      .next().withMessage("Remove the BOM.")
      .noMore();
  }

  @Test
  public void should_find_a_BOM_in_UTF16_files_but_not_raise_any_issue() {
    JSONCheckVerifier.verify(new BOMCheck(), CheckTestUtils.getTestFile("utf16BE.json"), Charsets.UTF_16BE)
      .noMore();

    JSONCheckVerifier.verify(new BOMCheck(), CheckTestUtils.getTestFile("utf16LE.json"), Charsets.UTF_16LE)
      .noMore();
  }

  @Test
  public void should_not_find_a_BOM_in_UTF8_file_and_not_raise_any_issue() {
    JSONCheckVerifier.verify(new BOMCheck(), CheckTestUtils.getTestFile("sample.json"), Charsets.UTF_8)
      .noMore();
  }

}
