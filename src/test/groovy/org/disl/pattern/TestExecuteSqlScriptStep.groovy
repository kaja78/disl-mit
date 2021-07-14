package org.disl.pattern

import groovy.sql.Sql
import org.disl.test.DislTestCase
import org.junit.Test

import static org.junit.Assert.assertEquals

class TestExecuteSqlScriptStep extends DislTestCase {

    @Test
    void testGetCommands() {
        def p = new TestingExecuteSQLScriptStep(commandSeparator: ExecuteSQLScriptStep.BACKSLASH_NEW_LINE, code: "A;B")
        assertEquals(1, p.getCommands().size())
        assertEquals("A;B", p.getCommands()[0])
        p = new TestingExecuteSQLScriptStep(code: "A;B");
        assertEquals(2, p.getCommands().size())
        assertEquals("A", p.getCommands()[0])
        assertEquals("B", p.getCommands()[1])
        p = new TestingExecuteSQLScriptStep(commandSeparator: ExecuteSQLScriptStep.BACKSLASH_NEW_LINE, code: """\
/*1st block*/
begin
	null;
end;
\\
/*2nd block*/
begin
	null;
end;
""")
        def commands = p.getCommands()
        assertEquals(2, p.getCommands().size())
        assertEquals("""\
/*1st block*/
begin
	null;
end;
""", p.getCommands()[0])
        assertEquals("""\
/*2nd block*/
begin
	null;
end;
""", p.getCommands()[1])


    }

    static class TestingExecuteSQLScriptStep extends ExecuteSQLScriptStep {
        String code
        Sql sql
    }
}
