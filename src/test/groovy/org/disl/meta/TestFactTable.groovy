package org.disl.meta

import org.disl.pattern.TablePattern;


@Description("Testing fact table.")
public class TestFactTable extends Table {

    @Override
    TablePattern getPattern() {
        return null
    }

    @PrimaryKey
    Column ID

    @ForeignKey(targetTable = TestDimensionTable, targetColumn = "KEY")
    Column DIMENSION_KEY

    @Description("Amount measure.")
    Column AMOUNT;

}
