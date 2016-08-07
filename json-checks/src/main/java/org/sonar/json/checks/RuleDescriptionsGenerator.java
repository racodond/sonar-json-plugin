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
package org.sonar.json.checks;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.sonar.api.internal.apachecommons.io.FileUtils;
import org.sonar.json.checks.puppet.PuppetLicenseCheck;

public class RuleDescriptionsGenerator {

  private final Map<String, String> tags = ImmutableMap.<String, String>builder()
    .put("[[allLicenses]]", generateHtmlTable(PuppetLicenseCheck.AVAILABLE_LICENSES))
    .build();

  public void generateHtmlRuleDescription(String templatePath, String outputPath) throws IOException {
    try (OutputStream fileOutputStream = new FileOutputStream(outputPath)) {
      Writer writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Charsets.UTF_8));
      writer.write(replaceTags(FileUtils.readFileToString(new File(templatePath), Charsets.UTF_8)));
      writer.flush();
      writer.close();
    } catch (IOException e) {
      throw new IllegalStateException("Could not generate the HTML description.", e);
    }
  }

  private String generateHtmlTable(List<String> elements) {
    StringBuilder html = new StringBuilder("<table style=\"border: 0;\">");
    List<List<String>> subLists = Lists.partition(elements, 3);
    for (List<String> subList : subLists) {
      html.append("<tr>");
      for (String element : subList) {
        html.append("<td style=\"border: 0;\">").append(element).append("</td>");
      }
      html.append("</tr>");
    }
    html.append("</table>");
    return html.toString();
  }

  private String replaceTags(String rawDescription) {
    String description = rawDescription;
    for (Map.Entry<String, String> tag : tags.entrySet()) {
      description = description.replace(tag.getKey(), tag.getValue());
    }
    return description;
  }

}
