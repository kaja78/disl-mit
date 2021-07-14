package org.disl.test

import org.disl.db.ChangedContextTest
import org.disl.meta.Context
import org.disl.meta.MetaFactory
import org.disl.meta.TestTable.TESTING_TABLE
import org.disl.util.test.AbstractDislTestCase
import org.junit.Before

class DislTestCase extends AbstractDislTestCase {

    static {
        Context.setContextName("disl-test")
        def sql = Context.getSql("default")
        sql.execute("CREATE TABLE DUAL (dummy char(1))")
        sql.execute("INSERT INTO DUAL VALUES ('X')")
        MetaFactory.create(TESTING_TABLE).execute()
    }

    @Before
    void init() {
        ChangedContextTest.setContext('disl-test')
    }

}
