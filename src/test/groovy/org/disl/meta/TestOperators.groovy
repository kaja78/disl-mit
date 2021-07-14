package org.disl.meta

import org.disl.pattern.TablePattern
import org.junit.Before
import org.junit.Test

import static groovy.util.GroovyTestCase.assertEquals

class TestOperators {

    SqlExpression one
    Table t
    Column A
    Column B
    Mapping m

    @Before
    void init() {
        one = new SqlExpression(expression: { 1 })
        t = new Table() {
            TablePattern pattern

            String getSchema() {
                ""
            };
        }
        t.sourceAlias = "T"
        A = new Column(name: "A")
        B = new Column(name: "B", parent: t)
        m = new Mapping() {
            ColumnMapping M = e "TO_NUMBER('1')"
            ColumnMapping N = e "TO_NUMBER('1')"

            void initMapping() {}

            String getSchema() {
                ""
            };
        }
        m.sourceAlias = "M"
        m.init()
    }

    @Test
    void testToString() {
        def value = "value"
        SqlExpression e = new SqlExpression(expression: { "${value}" })
        assertEquals("value", e.toString())
        value = "changedValue"
        assertEquals("changedValue", e.toString())
    }

    @Test
    void testPlus() {
        assertEquals('1+1', (one + one).toString())
        assertEquals('1+1', (one + 1).toString())
        assertEquals('1+A', (one + A).toString())
        assertEquals('1+T.B', (one + B).toString())
        assertEquals('A+1+T.B', (A + one + B).toString())
        assertEquals("M.M+A+1+T.B+M.N", (m.M + A + one + B + m.N).toString())
    }

    @Test
    void testConcat() {
        assertEquals('1||1', (one.concat(one)).toString())
        assertEquals('1||A||T.B||M.M', (one.concat(A, B, m.M)).toString())
    }

    @Test
    void testIsEqual() {
        assertEquals('1=A', (one.isEqual(A)).toString())
    }
}
