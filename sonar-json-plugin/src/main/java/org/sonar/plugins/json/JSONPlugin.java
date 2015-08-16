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
package org.sonar.plugins.json;

import com.google.common.collect.ImmutableList;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import org.sonar.json.ast.visitors.SonarComponents;

@Properties({
  @Property(
    key = JSONPlugin.FILE_SUFFIXES_KEY,
    defaultValue = JSONPlugin.FILE_SUFFIXES_DEFAULT_VALUE,
    name = "File Suffixes",
    description = "Comma-separated list of suffixes for files to analyze. To not filter, leave the list empty.",
    global = true,
    project = true)
})
public class JSONPlugin extends SonarPlugin {

  public static final String FILE_SUFFIXES_KEY = "sonar.json.file.suffixes";
  public static final String FILE_SUFFIXES_DEFAULT_VALUE = ".json";

  @Override
  public ImmutableList getExtensions() {
    return ImmutableList.of(
      JSON.class,
      SonarComponents.class,
      JSONSquidSensor.class,
      JSONProfile.class,
      JSONRulesDefinition.class
      );
  }

}
