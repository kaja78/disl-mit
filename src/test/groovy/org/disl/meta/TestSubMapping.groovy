package org.disl.meta

import org.disl.meta.TestMapping.TestingMapping
import org.disl.test.DislTestCase
import org.junit.Assert
import org.junit.Test

class TestSubMapping extends DislTestCase {

    static class TestingSubMapping extends Mapping {

        ColumnMapping A = e "${subquery.A}"
        ColumnMapping B = e "1"

        TestingMapping subquery

        @Override
        public void initMapping() {
            from subquery
        }
    }


    @Test
    void testGetSQLQuery() {
        TestingSubMapping m = MetaFactory.create(TestingSubMapping)
        Assert.assertEquals("""\
	/*Mapping TestingSubMapping*/
		SELECT
			subquery.A as A,
			1 as B
		FROM
			(
	/*Mapping TestingMapping*/
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
	/*End of mapping TestingMapping*/) subquery
		WHERE
			1=1
		
	/*End of mapping TestingSubMapping*/""", m.getSQLQuery())
    }
}
