package org.disl.db.oracle

import org.disl.meta.*
import org.disl.pattern.TablePattern
import org.junit.Ignore
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * Test Oracle mapping including with clause and bind variables.
 * */
class TestOracleMapping extends OracleTest {

    TestingMapping t = MetaFactory.create(TestingMapping)

    @Test
    @Ignore
    void testValidate() {
        t.validate()
    }

    @Test
    @Ignore
    void testCopyToClipboard() {
        t.copySqlQueryToClipboard()
    }


    @Test
    void testGetSQLQuery() {
        assertEquals("""\t/*Mapping TestingMapping*/
\twith
\ts1 as (\t/*Mapping WithClauseSubquery*/
\t\tSELECT
\t\t\tt.DUMMY as B
\t\tFROM
\t\t\tSYS.DUAL t
\t\tWHERE
\t\t\t1=1
\t\t
\t/*End of mapping WithClauseSubquery*/),
\ts2 as (\t/*Mapping WithClauseSubquery*/
\t\tSELECT
\t\t\tt.DUMMY as B
\t\tFROM
\t\t\tSYS.DUAL t
\t\tWHERE
\t\t\t1=1
\t\t
\t/*End of mapping WithClauseSubquery*/),
\tlookup as (\t/*Lookup TestingWithClauseLookup*/
\tselect * from 
\t\t(select 1 as DUMMY_KEY,'X' as A,2 as B from dual
\t\tunion all
\t\tselect 2 as DUMMY_KEY,'X' as A,4 as B from dual) SRC
\twhere 1=1 AND SRC.DUMMY_KEY=SRC.DUMMY_KEY
\t/*End of lookup TestingWithClauseLookup*/)
\t\tSELECT
\t\t\tt.DUMMY as C1,
\t\t\ts1.B as C2,
\t\t\ts2.B as C3,
\t\t\tlookup.A as C4,
\t\t\tlookup.B as C5
\t\tFROM
\t\t\tSYS.DUAL t
\t\t\tINNER JOIN s1  ON (t.DUMMY=s1.B and 1=/*BIND*/p1)
\t\t\tINNER JOIN s2  ON (s1.B=s2.B)
\t\t\tINNER JOIN lookup  ON (t.DUMMY=lookup.A)
\t\tWHERE
\t\t\t1=1
\t\t
\t/*End of mapping TestingMapping*/""", t.getSQLQuery())
    }

    static class DUAL extends Table {
        TablePattern pattern = null
        String schema = 'sys'

        Column DUMMY
    }

    static class TestingMapping extends Mapping {


        WithClauseSubquery s1
        WithClauseSubquery s2
        TestingWithClauseLookup lookup
        DUAL t

        ColumnMapping C1 = e t.DUMMY
        ColumnMapping C2 = e s1.B
        ColumnMapping C3 = e s2.B
        ColumnMapping C4 = e lookup.A
        ColumnMapping C5 = e lookup.B


        void initMapping() {
            with s1, s2, lookup
            from t
            innerJoin s1 on "$t.DUMMY=$s1.B and 1=${bind('p1', 'NUMBER', 1)}"
            innerJoin s2 on "$s1.B=$s2.B"
            innerJoin lookup on "$t.DUMMY=$lookup.A"
        }

        static class WithClauseSubquery extends Mapping {

            DUAL t

            ColumnMapping B = e t.DUMMY

            void initMapping() {
                from t
            }
        }

        static class TestingWithClauseLookup extends Lookup {

            Column A
            Column B

            List<List> records = [["'X'", 2], ["'X'", 4]]


        }
    }
}
