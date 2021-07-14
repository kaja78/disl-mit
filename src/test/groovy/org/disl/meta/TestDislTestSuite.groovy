package org.disl.meta

import junit.framework.TestSuite
import org.disl.util.test.AbstractDislTestSuite
import org.junit.Ignore
import org.junit.Test

import static org.junit.Assert.assertNotNull

class TestDislTestSuite {

    @Ignore
    static class TestingSuite extends AbstractDislTestSuite {

    }

    TestingSuite ts = new TestingSuite()

    @Test
    void testGetTestSuite() {
        TestSuite ts = ts.getTestSuite()
        TestSuite mappings = ts.testAt(0)
        TestSuite tables = ts.testAt(1)
        TestSuite dislTestCases = ts.testAt(2)

        assertNotNull mappings.tests().find({ it.toString() == 'org.disl.meta.TestMapping$TestingMapping' })
        assertNotNull tables.tests().find({ it.toString() == 'org.disl.meta.TestDimensionTable' })
        assertNotNull dislTestCases.tests().find({ it.toString() == 'org.disl.meta.TestLibrary' })
    }

}
