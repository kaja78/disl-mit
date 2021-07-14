package org.disl.db.vertica

import org.disl.db.ChangedContextTest
import org.disl.db.reverseEngineering.ReverseEngineeringService
import org.disl.meta.Context
import org.disl.meta.PhysicalSchema
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

/**
 * Unit test for Vertica schema.
 * This test is ignored by default and may be run manually from IDE.
 * To run this test manualy:
 * 1) Copy Oracle JDBC driver jars into lib dir.
 * 2) Create context configuration file name oracle.db.test.context.properties in src/test/resources
 *
 * Template for context config:
 *
 default=Vertica
 default.host=192.168.120.100
 default.port=5433
 default.databaseName=VERTDB
 default.schema=[schema]
 default.user=[user]
 default.password=[password]

 * */
@Ignore
class TestVerticaSchema extends ChangedContextTest {
    PhysicalSchema s

    @Before
    void init() {
        super.init()
        s = Context.getContext().getPhysicalSchema('default')
    }

    @Override
    String getContextName() {
        'vertica.db.test'
    }

    @Test
    void testReverse() {
        ReverseEngineeringService s = Context.getReverseEngineeringService('default')
        new File("build/test/vertica/TEST_DATA.groovy").delete()
        s.reverseSchemaTables('vertica', 'TEST_DATA', 'pdi_test', new File('build/test'))
        assert new File("build/test/vertica/TEST_DATA.groovy").exists()
    }

    @Test
    void testEvaluateExpression() {
        Assert.assertEquals(3, s.evaluateExpression('1+2'))
    }

    @Test
    void testEvaluateAggregateExpression() {
        Assert.assertEquals(3, s.evaluateAggregateExpression("sum(a)", [[a: 1], [a: 2]]))
    }
}
