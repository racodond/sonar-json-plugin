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
package org.sonar.json;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.SonarScanner;
import com.sonar.orchestrator.locator.FileLocation;

import java.io.File;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  CustomRulesTest.class,
  MetricsTest.class,
  IssuesTest.class,
})
public class Tests {

  @ClassRule
  public static final Orchestrator ORCHESTRATOR = Orchestrator.builderEnv()
    .addPlugin(FileLocation.byWildcardFilename(new File("../../../sonar-json-plugin/target"), "sonar-json-plugin-*-SNAPSHOT.jar"))
    .addPlugin(FileLocation.byWildcardFilename(new File("../plugins/json-custom-rules-plugin/target"), "json-custom-rules-plugin-*-SNAPSHOT.jar"))
    .restoreProfileAtStartup(FileLocation.ofClasspath("/org/sonar/json/profiles/file-naming-convention-only-profile.xml"))
    .restoreProfileAtStartup(FileLocation.ofClasspath("/org/sonar/json/profiles/empty-profile.xml"))
    .restoreProfileAtStartup(FileLocation.ofClasspath("/org/sonar/json/profiles/json-custom-rules-profile.xml"))
    .build();

  public static SonarScanner createSonarScannerBuild() {
    SonarScanner build = SonarScanner.create();
    build.setProjectVersion("1.0");
    build.setSourceEncoding("UTF-8");
    build.setSourceDirs("src");
    return build;
  }

  public static void setProfile(String profileName, String projectKey) {
    ORCHESTRATOR.getServer().associateProjectToQualityProfile(projectKey, "json", profileName);
  }

}
