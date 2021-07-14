package org.disl.meta

import org.disl.meta.TestTable.TESTING_TABLE
import org.disl.test.DislTestCase
import org.junit.Test

import static org.disl.meta.TestLibrary.repeat
import static org.junit.Assert.assertEquals


class TestMapping extends DislTestCase {
    @Description('Testing mapping.')
    static class TestingMapping extends Mapping {

        TESTING_TABLE s1
        TESTING_TABLE s2
        TESTING_TABLE s3
        TESTING_TABLE s4
        TESTING_TABLE s5
        TESTING_TABLE s6

        ColumnMapping A = e s1.A
        ColumnMapping c = e "C"
        ColumnMapping B = e repeat(s2.B, 3)

        SqlExpression CONSTANT = constant 1


        void initMapping() {
            from s1
            innerJoin s2 on(s1.A, s2.A)
            leftOuterJoin s3 on "$s2.A=$s3.A"
            rightOuterJoin s4 on "$s2.A=$s4.A"
            fullOuterJoin s5 on "$s2.A=$s5.A"
            cartesianJoin s6
            where "$s1.A=$s1.A and 1=${bind('p1', 'NUMBER', 1)}"
            groupBy()
        }

    }

    static class EmptyMapping extends Mapping {
        TESTING_TABLE s1

        @Override
        void initMapping() {
            from s1
        }
    }

    @Test
    void testGetImplicitMapping() {
        EmptyMapping m = MetaFactory.create(EmptyMapping)
        m.traceInitialColumnMapping()
    }


    @Test
    void testGetSQLQuery() {
        TestingMapping mapping = MetaFactory.create(TestingMapping)
        assertEquals("""	/*Mapping TestingMapping*/
		SELECT
			s1.A as A,
			C as c,
			REPEAT(s2.B,3) as B
		FROM
			PUBLIC.TESTING_TABLE s1
			INNER JOIN PUBLIC.TESTING_TABLE s2  ON (s1.A=s2.A)
			LEFT OUTER JOIN PUBLIC.TESTING_TABLE s3  ON (s2.A=s3.A)
			RIGHT OUTER JOIN PUBLIC.TESTING_TABLE s4  ON (s2.A=s4.A)
			FULL OUTER JOIN PUBLIC.TESTING_TABLE s5  ON (s2.A=s5.A)
			CROSS JOIN PUBLIC.TESTING_TABLE s6
		WHERE
			s1.A=s1.A and 1=/*BIND*/p1
		GROUP BY
			s1.A,C,REPEAT(s2.B,3)
	/*End of mapping TestingMapping*/""".toString(), mapping.getSQLQuery())
    }

    @Test
    void testGetClipboardQuery() {
        TestingMapping mapping = MetaFactory.create(TestingMapping)
        assertEquals("""	/*Mapping TestingMapping*/
		SELECT
			s1.A as A,
			C as c,
			REPEAT(s2.B,3) as B
		FROM
			PUBLIC.TESTING_TABLE s1
			INNER JOIN PUBLIC.TESTING_TABLE s2  ON (s1.A=s2.A)
			LEFT OUTER JOIN PUBLIC.TESTING_TABLE s3  ON (s2.A=s3.A)
			RIGHT OUTER JOIN PUBLIC.TESTING_TABLE s4  ON (s2.A=s4.A)
			FULL OUTER JOIN PUBLIC.TESTING_TABLE s5  ON (s2.A=s5.A)
			CROSS JOIN PUBLIC.TESTING_TABLE s6
		WHERE
			s1.A=s1.A and 1=/*p1*/1
		GROUP BY
			s1.A,C,REPEAT(s2.B,3)
	/*End of mapping TestingMapping*/""".toString(), mapping.getClipboardQuery())
    }

    @Test
    void testConstant() {
        TestingMapping mapping = MetaFactory.create(TestingMapping)
        assertEquals("1", (String) mapping.CONSTANT)
        assertEquals("1+1", (String) (mapping.CONSTANT + 1))
    }

    @Test
    void testDescrption() {
        TestingMapping mapping = MetaFactory.create(TestingMapping)
        assertEquals('Testing mapping.', mapping.description)

    }

    @Test
    void testGroupBy() {
        TestingMapping mapping = MetaFactory.create(TestingMapping)
        String value = "value"
        mapping.groupBy value
        assertEquals(value, mapping.getGroupBy())
        mapping.groupBy value, value
        assertEquals("value,value", mapping.getGroupBy())
        mapping.groupBy()
        assertEquals("s1.A,C,REPEAT(s2.B,3)", mapping.getGroupBy())

    }
}
