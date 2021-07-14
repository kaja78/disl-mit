package org.disl.workflow

import org.disl.meta.MetaFactory
import org.disl.test.DislTestCase
import org.junit.Assert
import org.junit.Test

class TestDislScript extends DislTestCase {

    TestingScript s = MetaFactory.create(TestingScript)

    @Test
    void testExecute() {
        s.execute()
    }

    @Test
    void testEncode() {
        Assert.assertNotEquals('A', DislScript.encode('A'))
        Assert.assertEquals('A', DislScript.decode(s.encode('A')))
    }

}
