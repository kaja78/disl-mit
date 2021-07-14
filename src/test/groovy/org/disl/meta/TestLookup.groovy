package org.disl.meta

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class TestLookup {

    TestingLookup l
    TestingLookupMapping m

    @Before
    public void initTest() {
        Context.setContextName('disl-test')
        l = MetaFactory.create(TestingLookup)
        m = MetaFactory.create(TestingLookupMapping)
    }

    @Test
    public void testMapToQuery() {
        assertEquals("select 1 as DUMMY_KEY,1 as A,2 as B from (VALUES(0))", l.getPhysicalSchema().mapToQuery(["A.A": 1, "B": 2], "A", 1, true))
        assertEquals("select 2 as DUMMY_KEY,1 as A from (VALUES(0))", l.getPhysicalSchema().mapToQuery(["A.A": 1, "B": 2], "A", 2, false))
    }

    @Test
    public void testGetQuery() {
        assertEquals("""\t/*Mapping TestingLookupMapping*/
\t\tSELECT
\t\t\tl.A+l.B as C
\t\tFROM
\t\t\t(
\t/*Lookup TestingLookup*/
\tselect * from 
\t\t(select 1 as DUMMY_KEY,1 as A,2 as B from (VALUES(0))
\t\tunion all
\t\tselect 2 as DUMMY_KEY,2 as A,4 as B from (VALUES(0))) SRC
\twhere 1=1 AND SRC.DUMMY_KEY=SRC.DUMMY_KEY
\t/*End of lookup TestingLookup*/) l
\t\tWHERE
\t\t\t1=1
\t\t
\t/*End of mapping TestingLookupMapping*/""", m.getSQLQuery())
    }

    @Test
    public void testGetRefference() {
        assertEquals("""(\t/*Lookup TestingLookup*/
\tselect * from 
\t\t(select 1 as DUMMY_KEY,1 as A,2 as B from (VALUES(0))
\t\tunion all
\t\tselect 2 as DUMMY_KEY,2 as A,4 as B from (VALUES(0))) SRC
\twhere 1=1 AND SRC.DUMMY_KEY=SRC.DUMMY_KEY
\t/*End of lookup TestingLookup*/)""", l.getRefference())
    }

    @Test
    public void testGetRefferenceColumnList() {
        assertEquals("A,B", l.getRefferenceColumnList())
    }

    static class TestingLookup extends Lookup {

        Column A
        Column B

        List<List> records = [[1, 2], [2, 4]]


    }

    static class TestingLookupMapping extends Mapping {

        TestingLookup L

        ColumnMapping C = e L.A + L.B

        @Override
        public void initMapping() {
            from L
        }

    }
}
