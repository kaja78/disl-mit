package org.disl.test

import org.junit.Test

class TestDislTestCase extends DislTestCase {

    @Test
    public void testAssertExpressionTrue() {
        assertExpressionTrue("1=1")
    }

    @Test
    public void testAssertRowCount() {
        assertRowCount(1, "select 1 from dual")
    }

    @Test
    public void testAssertExpressionEquals() {
        assertExpressionEquals("1+1", "2")
    }

}
