package org.disl.meta

import org.disl.meta.TestTable.TESTING_TABLE
import org.disl.test.DislTestCase
import org.disl.util.test.DislTestHelper
import org.junit.Test

import static org.junit.Assert.assertEquals

class TestMappingSubquery extends DislTestCase {
    DislTestHelper testHelper=new DislTestHelper()
    TestingMapping t = MetaFactory.create(TestingMapping)


    @Test
    void testGetSQLQuery() {
        assertEquals("""	/*Mapping TestingMapping*/
		SELECT
			t.A as A
		FROM
			PUBLIC.TESTING_TABLE t
		WHERE
			t.A in (
	/*Mapping Subquery*/
		SELECT
			t.B as B
		FROM
			PUBLIC.TESTING_TABLE t
		WHERE
			1=1
		
	/*End of mapping Subquery*/
)
		
	/*End of mapping TestingMapping*/""", t.getSQLQuery())
    }

    @Test
    void testValidate() {
        testHelper.validateQuery(t)
    }

    static class TestingMapping extends Mapping {

        TESTING_TABLE t
        Subquery s

        ColumnMapping A = e t.A

        void initMapping() {
            from t
            where """${t.A} in (
${s.SQLQuery}
)"""
        }

        static class Subquery extends Mapping {

            TESTING_TABLE t

            ColumnMapping B = e t.B

            void initMapping() {
                from t
            }
        }
    }
}
