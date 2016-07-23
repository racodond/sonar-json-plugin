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
package org.sonar.json.checks.verifier;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;

import java.util.*;

import org.hamcrest.Matcher;

import static org.junit.Assert.assertThat;

public class CheckVerifier {

    private final Iterator<TestIssue> iterator;
    private TestIssue current;

    private static final Comparator<TestIssue> ORDERING = new Comparator<TestIssue>() {
        @Override
        public int compare(TestIssue left, TestIssue right) {
            if (Objects.equal(left.starLine(), right.starLine())) {
                return left.startColumn().compareTo(right.startColumn());
            } else if (left.starLine() == null) {
                return -1;
            } else if (right.starLine() == null) {
                return 1;
            } else {
                return left.starLine().compareTo(right.starLine());
            }
        }
    };

    private CheckVerifier(Collection<TestIssue> testIssues) {
        iterator = Ordering.from(ORDERING).sortedCopy(testIssues).iterator();
    }

    public static CheckVerifier verify(Collection<TestIssue> testIssues) {
        return new CheckVerifier(testIssues);
    }

    public CheckVerifier next() {
        if (!iterator.hasNext()) {
            throw new AssertionError("\nExpected issue");
        }
        current = iterator.next();
        return this;
    }

    public void noMore() {
        if (iterator.hasNext()) {
            TestIssue next = iterator.next();
            throw new AssertionError("\nNo more issues expected\ngot: at line " + next.starLine());
        }
    }

    private void checkStateOfCurrent() {
        if (current == null) {
            throw new IllegalStateException("Prior to this method you should call next()");
        }
    }

    public CheckVerifier startAtLine(Integer expectedLine) {
        checkStateOfCurrent();
        if (!Objects.equal(expectedLine, current.starLine())) {
            throw assertionError(expectedLine, current.starLine());
        }
        return this;
    }

    public CheckVerifier startAtColumn(Integer expectedColumn) {
        checkStateOfCurrent();
        if (!Objects.equal(expectedColumn, current.startColumn())) {
            throw assertionError(expectedColumn, current.startColumn());
        }
        return this;
    }

    public CheckVerifier endAtLine(Integer expectedLine) {
        checkStateOfCurrent();
        if (!Objects.equal(expectedLine, current.endLine())) {
            throw assertionError(expectedLine, current.endLine());
        }
        return this;
    }

    public CheckVerifier endAtColumn(Integer expectedColumn) {
        checkStateOfCurrent();
        if (!Objects.equal(expectedColumn, current.endColumn())) {
            throw assertionError(expectedColumn, current.endColumn());
        }
        return this;
    }

    public CheckVerifier withMessage(String expectedMessage) {
        checkStateOfCurrent();
        String actual = current.message();
        if (!actual.equals(expectedMessage)) {
            throw assertionError("\"" + expectedMessage + "\"", "\"" + actual + "\"");
        }
        return this;
    }

    public CheckVerifier withMessageThat(Matcher<String> matcher) {
        checkStateOfCurrent();
        String actual = current.message();
        assertThat(actual, matcher);
        return this;
    }

    public CheckVerifier withCost(Double expectedCost) {
        checkStateOfCurrent();
        if (!Objects.equal(expectedCost, current.cost())) {
            throw assertionError(expectedCost, current.cost());
        }
        return this;
    }

    public CheckVerifier withSecondaryLines(int... expectedLines) {
        checkStateOfCurrent();

        Collections.sort(current.secondaryLines());
        Arrays.sort(expectedLines);

        int i = 0;
        for (i = 0; i < expectedLines.length; i++) {
            if (current.secondaryLines().size() <= i) {
                throw new AssertionError("\nMissing secondary location at line " + expectedLines[i]);
            } else if (expectedLines[i] != current.secondaryLines().get(i)) {
                throw assertionError(expectedLines[i], current.secondaryLines().get(i));
            }
        }

        if (i < current.secondaryLines().size()) {
            throw new AssertionError("\nNo more secondary location expected\ngot: at line " + current.secondaryLines().get(i));
        }

        return this;
    }

    private static AssertionError assertionError(Object expected, Object actual) {
        return new AssertionError("\nExpected: " + expected + "\ngot: " + actual);
    }

}
