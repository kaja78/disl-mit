package org.disl.db.oracle


import org.disl.meta.Context
import org.disl.meta.PhysicalSchema
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class TestOracleSchema extends OracleTest {

    PhysicalSchema s

    @Override
    void init() {
        super.init()
        s = Context.getContext().getPhysicalSchema('default')
    }

    @Test
    void testJdbcUrl() {
        PhysicalSchema testOverrideByUrlSchema = Context.getContext().getPhysicalSchema('testOverrideByUrl')
        Assert.assertEquals('jdbc:oracle:thin:@myhost:1521:mysid', testOverrideByUrlSchema.getJdbcUrl())
    }

    @Test
    @Ignore
    void testEvaluateExpression() {
        Assert.assertEquals(3, s.evaluateExpression('1+2'), 0)
    }

    @Test
    @Ignore
    void testEvaluateAggregateExpression() {
        Assert.assertEquals(3, s.evaluateAggregateExpression("sum(a)", [[a: 1], [a: 2]]), 0)
    }

    @Test
    @Ignore
    void testEvaluateExpressionOnProxy() {
        PhysicalSchema proxySchema = Context.getContext().getPhysicalSchema('oracleProxy')
        String proxyUser = Context.getContextProperty('default.user')
        String targetSchema = Context.getContextProperty('oracleProxy.schema')
        Assert.assertEquals("$proxyUser[$targetSchema]".toString(), proxySchema.getUser())
        Assert.assertEquals(3, proxySchema.evaluateExpression('1+2'), 0)
    }
}