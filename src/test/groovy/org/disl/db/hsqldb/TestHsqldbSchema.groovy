package org.disl.db.hsqldb

import org.disl.meta.Context
import org.disl.test.DislTestCase
import org.junit.Test

import static org.junit.Assert.assertEquals

class TestHsqldbSchema extends DislTestCase {

    @Test
    void testSql() {
        Context.setContextName('disl-test')
        physicalSchema.getSql().execute("SELECT 1 FROM (VALUES (0))")
    }

    @Test
    public void testEvaluate() {
        assertEquals(2, physicalSchema.evaluateExpression("1+1"))
        assertEquals(11, physicalSchema.evaluateAggregateExpression("sum(A)", [["A": 6], ["A": 5]]))
        assertEquals(11, physicalSchema.evaluateAggregateExpression("sum(A)", [["A": 6, "B.B": 1], ["A": 5, "B.B": 1]]))
        assertEquals(2, physicalSchema.evaluateAggregateExpression("sum(B.B)", [["A.A": 6, "B.B": 1], ["A.A": 5, "B.B": 1]]))
    }

    @Test
    public void testMapToSubQuery() {
        assertEquals '''\
(select 1 as DUMMY_KEY,1 as a,2 as b from (VALUES(0))
\t\tunion all
\t\tselect 2 as DUMMY_KEY,2 as a,4 as b from (VALUES(0))) SRC
\twhere 1=1 AND SRC.DUMMY_KEY=SRC.DUMMY_KEY''', physicalSchema.recordsToSubquery([["a": 1, "b": 2], ["a": 2, "b": 4]])
        assertEquals '''\
(select 1 as DUMMY_KEY,1 as a from (VALUES(0))
\t\tunion all
\t\tselect 2 as DUMMY_KEY,2 as a from (VALUES(0))) A,
\t\t(select 1 as DUMMY_KEY,2 as b from (VALUES(0))
\t\tunion all
\t\tselect 2 as DUMMY_KEY,4 as b from (VALUES(0))) B
\twhere 1=1 AND A.DUMMY_KEY=A.DUMMY_KEY
\t\tAND B.DUMMY_KEY=A.DUMMY_KEY''', physicalSchema.recordsToSubquery([["A.a": 1, "B.b": 2], ["A.a": 2, "B.b": 4]])
    }

}
