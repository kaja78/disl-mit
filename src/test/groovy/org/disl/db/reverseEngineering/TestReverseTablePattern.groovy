package org.disl.db.reverseEngineering

import org.disl.meta.ForeignKeyMeta
import org.disl.meta.MetaFactory
import org.disl.meta.TestDimensionTable
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class TestReverseTablePattern {

    ReverseTablePattern pattern


    @Before
    void initTest() {
        TestDimensionTable testTable = MetaFactory.create(TestDimensionTable)
        testTable.getForeignKeys().add(new ForeignKeyMeta(name: 'FK1', targetTableClassName: 'org.disl.test.MyTable', sourceColumn: 'NAME', targetColumn: 'NAME'))
        pattern = new ReverseTablePattern(
                table: testTable,
                outputDir: new File("build/test"),
                parentClassName: "AbstractL2Table",
                packageName: "l2")
        pattern.init()
    }

    @Test
    public void testSimulate() {
        pattern.simulate()
    }

    @Test
    public void testExecute() {
        String expectedContent = """\
package l2

import org.disl.meta.*

@Description(\"""This is testing dimension.\""")
@ForeignKeys([@ForeignKey(name='FK1',targetTable=org.disl.test.MyTable,sourceColumn='NAME',targetColumn=('NAME'))])
@groovy.transform.CompileStatic
class TestDimensionTable extends AbstractL2Table {

		@Description(\"""Surrogate key.\""")
		@DataType(\"""INTEGER\""")
		@PrimaryKey
		Column KEY

		@Description(\"""Natural key\""")
		@DataType(\"""INTEGER\""")
		@NotNull
		Column ID

		@Description(\"""Dimension name.\""")
		@DataType(\"""VARCHAR(200)\""")
		@NotNull
		Column NAME
}"""
        pattern.execute()
        assertEquals(expectedContent, new File("build/test/l2/TestDimensionTable.groovy").getText())
    }
}
