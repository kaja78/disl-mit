package org.disl.meta


import org.disl.pattern.generic.CreateOrReplaceTablePattern
import org.junit.Test

@UniqueKeys([
        @UniqueKey(columns = ["ID"]),
        @UniqueKey(columns = ["NAME"])])

@Description("This is testing dimension.")
class TestDimensionTable extends Table {

    CreateOrReplaceTablePattern pattern

    @PrimaryKey
    @DataType("INTEGER")
    @Description("Surrogate key.")
    Column KEY

    @DataType("INTEGER")
    @Description("Natural key")
    @NotNull
    Column ID

    @DataType("VARCHAR(200)")
    @Description("Dimension name.")
    @NotNull
    Column NAME

    @Test
    void testGetUniqueKeys() {
        Table t = MetaFactory.create(TestDimensionTable)
        List l = t.getUniqueKeys()
        GroovyTestCase.assertEquals("ID", l[0].columns[0])
        GroovyTestCase.assertEquals("NAME", l[1].columns[0])
    }

}
