package org.disl.meta

import org.disl.pattern.generic.CreateOrReplaceTablePattern
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class TestTable {

    @Indexes(@Index(columns = ["A", "B"]))
    @CheckConstraints(
            @Check(name = 'CHECK_1', value = '1=1'))
    @ForeignKeys([
            @ForeignKey(name = 'PARENT1_FK', targetTable = TESTING_TABLE, sourceColumn = 'PARENT1_B,PARENT1_C'),
            @ForeignKey(name = 'PARENT2_FK', targetTable = TESTING_TABLE, sourceColumn = 'PARENT2_B,PARENT2_C', targetColumn = ('B,C'))])
    @Description('Testing table.')
    static class TESTING_TABLE extends Table {


        CreateOrReplaceTablePattern pattern

        @Description("Column A.")
        @DataType("VARCHAR(255)")
        @DefaultValue("'A'")
        Column A

        @PrimaryKey
        @DataType("VARCHAR(255)")
        Column B

        @PrimaryKey
        @DataType("VARCHAR(255)")
        Column C

        @DataType("VARCHAR(255)")
        Column PARENT1_B

        @DataType("VARCHAR(255)")
        Column PARENT1_C

        @Check("PARENT2_B<>'XNA'")
        @DefaultValue("'XUN'")
        @NotNull
        @DataType("VARCHAR(255)")
        Column PARENT2_B

        @DataType("VARCHAR(255)")
        Column PARENT2_C

        @DataType("VARCHAR(255)")
        @ForeignKey(targetTable = TESTING_TABLE, targetColumn = 'A')
        Column PARENT3_A

        def DUMMY

    }

    TESTING_TABLE table

    @Before
    void init() {
        Context.setContextName("disl-test")
        table = MetaFactory.create(TESTING_TABLE)
    }

    @Test
    void testGetColumns() {
        assert table.A == table.getColumns().get(0)
        assert table.A.dataType == "VARCHAR(255)"
        assert table.A.defaultValue == "'A'"
        assert table.B == table.getColumns().get(1)
        assert "Column A." == table.A.description
        assert table.A.parent == table
        assert table.PARENT2_B.check == "PARENT2_B<>'XNA'"
    }

    @Test
    void testGetIndexes() {
        Collection indexes = table.getIndexes()
        assert indexes != null
        assert indexes[0].columnNames[0] == "A"
        assert indexes[0].columnNames[1] == "B"
    }

    @Test
    void testGetCheckConstraints() {
        CheckMeta check = table.getCheckConstraints().get(0)
        assertEquals('CHECK_1', check.name)
        assertEquals('1=1', check.check)
    }

    @Test
    void testGetPrimaryKeyColumns() {
        assertEquals([table.B, table.C], table.getPrimaryKeyColumns())
    }

    @Test
    void testGetColumnDefinition() {
        assertEquals("PARENT2_B VARCHAR(255) DEFAULT 'XUN' NOT NULL CHECK (PARENT2_B<>'XNA')", table.PARENT2_B.getColumnDefinition())
    }

    @Test
    void testGetForeignKeys() {
        ForeignKeyMeta f

        f = table.getForeignKeys()[0]
        assertEquals('', f.getName())
        assertEquals('TESTING_TABLE', f.getTargetTable().getName())
        assertEquals('A', f.getTargetColumn())
        assertEquals('PARENT3_A', f.getSourceColumn())

        f = table.getForeignKeys()[1]
        assertEquals('PARENT1_FK', f.getName())
        assertEquals('TESTING_TABLE', f.getTargetTable().getName())
        assertEquals('B,C', f.getTargetColumn())
        assertEquals('PARENT1_B,PARENT1_C', f.getSourceColumn())

        f = table.getForeignKeys()[2]
        assertEquals('PARENT2_FK', f.getName())
        assertEquals('TESTING_TABLE', f.getTargetTable().getName())
        assertEquals('B,C', f.getTargetColumn())
        assertEquals('PARENT2_B,PARENT2_C', f.getSourceColumn())
    }

    @Test
    void testGetRefferenceColumnList() {
        assertEquals(table.getRefferenceColumnList(), "A,B,C,PARENT1_B,PARENT1_C,PARENT2_B,PARENT2_C,PARENT3_A")
    }


}
