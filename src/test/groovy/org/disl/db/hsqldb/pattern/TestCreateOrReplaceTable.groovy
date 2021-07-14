package org.disl.db.hsqldb.pattern


import org.disl.meta.MetaFactory
import org.disl.meta.TestTable.TESTING_TABLE
import org.disl.test.DislTestCase
import org.junit.Test

class TestCreateOrReplaceTable extends DislTestCase {

    TESTING_TABLE t = MetaFactory.create(TESTING_TABLE)

    @Test
    public void testSimulate() {
        t.simulate()
    }

    @Test
    public void testExecute() {
        t.execute()
        t.execute()
    }
}
