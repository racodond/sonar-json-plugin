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
package org.sonar.json.checks;

import com.google.common.collect.ImmutableList;

import java.util.Collection;

import org.sonar.json.checks.puppet.*;

public final class CheckList {

  public static final String REPOSITORY_NAME = "SonarQube";

  private CheckList() {
  }

  @SuppressWarnings("rawtypes")
  public static Collection<Class> getChecks() {
    return ImmutableList.<Class>of(
      FileNameCheck.class,
      MissingNewLineAtEndOfFileCheck.class,
      ParsingErrorCheck.class,
      PuppetDeprecatedKeysCheck.class,
      PuppetEnforceAuthorValueCheck.class,
      PuppetEnforceLicenseValueCheck.class,
      PuppetLicenseCheck.class,
      PuppetMetadataFilePresentCheck.class,
      PuppetRequiredKeysCheck.class,
      PuppetVersionCheck.class,
      TabCharacterCheck.class
      );
  }
}
