package org.disl.db.reverseEngineering


import org.disl.test.DislTestCase
import org.junit.Test

import static org.junit.Assert.assertEquals

class TestReverseEngineeringService extends DislTestCase {

    ReverseEngineeringService s = new ReverseEngineeringService(logicalSchemaName: "default")

    @Test
    public void testReverseSchemaTables() {
        s.reverseSchemaTables("l2", "%DUAL%", null, new File("build/test"))
        assert new File("build/test/l2/DUAL.groovy").exists()
    }

    @Test
    public void testTraceColumnMappings() {
        s.traceColumnMappings('select 1 one, 2 two from dual')
    }

    @Test
    public void testGetAbstractParentTableFileName() {
        assertEquals("AbstractL0Table", s.getAbstractParentTableClassSimpleName("org.disl.l0"))
    }

    @Test
    public void testGetAbstractParentTableSourceCode() {
        String packageName = "org.disl.l0"
        String expectedCode = """\
package org.disl.l0

import org.disl.meta.Table

public abstract class AbstractL0Table  extends Table {
		String schema="L0"
}
"""
        s.logicalSchemaName = "L0"
        assertEquals(expectedCode, s.getAbstractParentTableSourceCode('org.disl.l0'))
    }
}
