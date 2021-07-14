package org.disl.meta

import org.disl.test.DislTestCase
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class TestContext extends DislTestCase {
    @Test
    void testGetContext() {
        Context context = Context.getContext()
        assert context.getName().equals('disl-test')
        assert context.getConfig()["default"] == "Hsqldb"
        assert context.getPhysicalSchema("default").schema == "PUBLIC"
    }

    @Test
    void testGetSql() {
        Context.getSql("default").execute("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES")
    }

    @Test
    void testGetContextProperty() {
        Assert.assertEquals(System.getenv('PATH'), Context.getContextProperty('env.PATH'))
    }

    @Test
    void testGetGlobalProperties() {
        Assert.assertEquals('globalValue', Context.getContextProperty('global.value'))
        Assert.assertEquals('disl-test', Context.getContextProperty('global.overridenValue'))
    }

    @Test
    @Ignore
    void testDislHomeProperties() {
        Assert.assertEquals('disl', Context.getContextProperty('disl.test.global'))
        Assert.assertEquals('disl-test', Context.getContextProperty('disl.test.user'))
    }

}
