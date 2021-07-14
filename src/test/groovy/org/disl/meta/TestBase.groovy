package org.disl.meta

import org.junit.Assert
import org.junit.Test

class TestBase {

    Base base = MetaFactory.create(TestingBaseChild)

    @Test
    void testTags() {
        Assert.assertArrayEquals(['A', 'B'].toArray(), base.getTags().toArray())
    }

    @Test
    void testSourceFileLink() {
        Assert.assertEquals('src/main/groovy/org/disl/meta/TestBase$TestingBaseChild.groovy', base.getSourceFileLink())
    }

    @Tags('A')
    static class TestingBase extends Base {

    }

    @Tags('B')
    static class TestingBaseChild extends TestingBase {

    }
}
