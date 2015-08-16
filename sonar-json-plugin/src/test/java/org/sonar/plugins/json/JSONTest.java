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

import org.junit.Test;
import org.sonar.api.config.Settings;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class JSONTest {

  @Test
  public void language_key_and_name() {
    JSON JSON = new JSON(mock(Settings.class));
    assertThat(JSON.getKey()).isEqualTo("json");
    assertThat(JSON.getName()).isEqualTo("JSON");
  }

  @Test
  public void default_suffixes() {
    JSON json = new JSON(mock(Settings.class));
    assertThat(json.getFileSuffixes()).containsOnly(".json");
  }

  @Test
  public void custom_suffixes() {
    Settings settings = new Settings();
    settings.setProperty("sonar.json.file.suffixes", ".foo,bar");

    JSON json = new JSON(settings);
    assertThat(json.getFileSuffixes()).containsOnly(".foo", "bar");
  }

}
