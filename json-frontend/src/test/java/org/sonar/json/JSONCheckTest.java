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

import org.junit.Assert;
import org.junit.Test;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;

public class JSONCheckTest {

  @Test
  public void add_issue_at_line() {
    JSONCheck check = new JSONCheck();
    check.setContext(new SquidAstVisitorContextImpl(new SourceProject("testProject")));
    check.getContext().addSourceCode(new SourceFile("abc", "src/abc.json"));
    check.addIssue(10, check, "Remove xxx.");

    Assert.assertNotNull(check.getContext().peekSourceCode().getCheckMessages());
    Assert.assertEquals(check.getContext().peekSourceCode().getCheckMessages().size(), 1);

    for (CheckMessage checkMessage : check.getContext().peekSourceCode().getCheckMessages()) {
      Assert.assertEquals(10, checkMessage.getLine().intValue());
      Assert.assertEquals("Remove xxx.", checkMessage.getDefaultMessage());
      Assert.assertNull(checkMessage.getCost());
      Assert.assertNotNull(checkMessage.getSourceCode());
      Assert.assertEquals("src/abc.json", checkMessage.getSourceCode().getName());
    }
  }

  @Test
  public void add_issue_on_file() {
    JSONCheck check = new JSONCheck();
    check.setContext(new SquidAstVisitorContextImpl(new SourceProject("testProject")));
    check.getContext().addSourceCode(new SourceFile("abc", "src/abc.json"));
    check.addIssueOnFile(check, "Remove xxx.");

    Assert.assertNotNull(check.getContext().peekSourceCode().getCheckMessages());
    Assert.assertEquals(check.getContext().peekSourceCode().getCheckMessages().size(), 1);

    for (CheckMessage checkMessage : check.getContext().peekSourceCode().getCheckMessages()) {
      Assert.assertNull(checkMessage.getLine());
      Assert.assertEquals("Remove xxx.", checkMessage.getDefaultMessage());
      Assert.assertNull(checkMessage.getCost());
      Assert.assertNotNull(checkMessage.getSourceCode());
      Assert.assertEquals("src/abc.json", checkMessage.getSourceCode().getName());
    }
  }

  @Test
  public void add_issue_at_line_with_cost() {
    JSONCheck check = new JSONCheck();
    check.setContext(new SquidAstVisitorContextImpl(new SourceProject("testProject")));
    check.getContext().addSourceCode(new SourceFile("abc", "src/abc.json"));
    check.addIssue(2, check, "Remove xxx.", 10.0);

    Assert.assertNotNull(check.getContext().peekSourceCode().getCheckMessages());
    Assert.assertEquals(check.getContext().peekSourceCode().getCheckMessages().size(), 1);

    for (CheckMessage checkMessage : check.getContext().peekSourceCode().getCheckMessages()) {
      Assert.assertEquals(2, checkMessage.getLine().intValue());
      Assert.assertEquals("Remove xxx.", checkMessage.getDefaultMessage());
      Assert.assertEquals(10.0, checkMessage.getCost().doubleValue(), 0.5);
      Assert.assertNotNull(checkMessage.getSourceCode());
      Assert.assertEquals("src/abc.json", checkMessage.getSourceCode().getName());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void add_issue_on_non_existing_source_code() {
    JSONCheck check = new JSONCheck();
    check.setContext(new SquidAstVisitorContextImpl(new SourceProject("testProject")));
    check.addIssue(2, check, "Remove xxx.");
  }

}
