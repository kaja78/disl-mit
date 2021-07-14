package org.disl.db.hsqldb.pattern

import org.disl.meta.*
import org.disl.pattern.generic.CreateOrReplaceTablePattern
import org.disl.pattern.generic.TruncateInsertPattern
import org.disl.test.DislTestCase
import org.junit.Test

class TestTruncateInsertMapping extends DislTestCase {


    static class TestMapping extends Mapping implements TableMapping {
        TruncateInsertPattern pattern

        TEST_TABLE target
        TEST_TABLE src

        ColumnMapping A = e 1
        ColumnMapping B = e "'2'"

        @Override
        public void initMapping() {
            from src
        }
    }

    static class TEST_TABLE extends Table {

        CreateOrReplaceTablePattern pattern

        Column A
        Column B
    }

    @Test
    void testSimulate() {
        def tt = new TEST_TABLE()
        def m = new TestMapping()
        TestMapping t = MetaFactory.create(TestMapping)
        t.simulate()
    }


}
