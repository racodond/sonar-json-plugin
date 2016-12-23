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
package org.sonar.json.parser;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.sonar.json.tree.impl.InternalSyntaxToken;
import org.sonar.json.tree.impl.SeparatedList;

import java.util.Arrays;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;

public class SeparatedListTest {

  @Test
  public void validSeparatorList() {
    String[] elements = {"a", "b", "c"};
    InternalSyntaxToken[] separators = {
      new InternalSyntaxToken(1, 1, ",", false, false),
      new InternalSyntaxToken(2, 1, ",", false, false)
    };
    SeparatedList<String> separatedList = new SeparatedList<>(Arrays.asList(elements), Arrays.asList(separators));

    assertThat(separatedList).isNotEmpty();
    assertThat(separatedList.get(0)).isEqualTo("a");
    assertThat(separatedList.get(1)).isEqualTo("b");
    assertThat(separatedList.get(2)).isEqualTo("c");

    assertThat(separatedList).contains("a");
    assertThat(separatedList).containsOnly("a", "b", "c");
    assertThat(separatedList.containsAll(ImmutableList.of("a", "b", "c"))).isTrue();

    assertThat(separatedList.getSeparators()).hasSize(2);
    assertThat(separatedList.getSeparators().get(0).text()).isEqualTo(",");
    assertThat(separatedList.getSeparators().get(1).text()).isEqualTo(",");
  }

  @Test
  public void emptySeparatedList() {
    SeparatedList<String> separatedList = new SeparatedList<>(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    assertThat(separatedList).isEmpty();
    assertThat(separatedList.getSeparators()).isEmpty();
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidSeparatedList() {
    String[] elements = {"a", "b", "c"};
    InternalSyntaxToken[] separators = {
      new InternalSyntaxToken(1, 1, ",", false, false)
    };
    new SeparatedList<>(Arrays.asList(elements), Arrays.asList(separators));
  }

}
