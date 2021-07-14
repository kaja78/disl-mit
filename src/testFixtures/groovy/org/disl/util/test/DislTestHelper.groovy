package org.disl.util.test

import groovy.sql.Sql
import org.disl.db.reverseEngineering.ReverseEngineeringService
import org.disl.meta.Column
import org.disl.meta.Context
import org.disl.meta.Mapping
import org.disl.meta.Table
import org.junit.Assert

import java.sql.SQLException

class DislTestHelper {

    /**
     * Validate SQL query. Throw exception for invalid query
     * */
    void validateQuery(Mapping mapping,Sql sql=getSql(mapping.getSchema())) throws AssertionError {
        try {
            sql.rows(getValidationQuery(mapping))
        } catch (SQLException e) {
            throw new AssertionError("Validation failed with message: ${e.getMessage()} for query:\n${getValidationQuery(mapping)}")
        }
    }

    protected String getValidationQuery(Mapping mapping) {
        "select * from (${mapping.getSQLQuery()}) as s where 1=2"
    }

    void validateTableDeployment(Table table, String tableType = 'TABLE', Sql sql=getSql(table.schema)) {
        List<Table> tables = getReverseEngineeredTables(Context.getContext().getPhysicalSchema(table.schema).getReverseEngineeringService(),table.physicalSchema, table.name, tableType, sql)
        if (!tables || tables.size() == 0) {
            throw new AssertionError("Table ${table.getRefference()} not deployed in database.")
        } else if (tables.size() > 1) {
            throw new AssertionError("Multiple tables matching ${table.refference} deployed in database.")
        }
        Assert.assertEquals("Table comment of deployed ${table.refference} does not match to model.", table.description ?: '', tables[0].description ?: '')
        validateTableColumns(tables[0], table)
    }

    protected Sql getSql(String logicalSchemaName) {
        Context.getSql(logicalSchemaName)
    }

    List<Table> getReverseEngineeredTables(ReverseEngineeringService reverseEngineeringService,String tableSchema, String tableName, String tableType, Sql sql) {
        return reverseEngineeringService.reverseEngineerTables(sql, tableName, tableType, tableSchema)
    }

    void validateViewDeployment(Mapping mapping, Sql sql) {
        List<Table> tables = getReverseEngineeredTables(Context.getPhysicalSchemaName(mapping.schema), mapping.name, 'VIEW', sql)
        if (!tables || tables.size() == 0) {
            throw new AssertionError("Table [${getSchema()}].[${mapping.getName()}] not deployed in database.")
        } else if (tables.size() > 1) {
            throw new AssertionError("Multiple tables matching ${mapping.refference} deployed in database.")
        }
        validateMappingColumns(tables[0], mapping)
    }

    protected void validateTableColumns(Table reversedTable, Table modelTable) {

        String reversedColumns = reversedTable.getColumns().collect({ toString(it) }).join('')
        String modelColumns = modelTable.getColumns().collect({ toString(it) }).join('')
        Assert.assertEquals("Column definition of deployed ${modelTable.refference} does not match to model.", modelColumns, reversedColumns)
    }

    protected void validateMappingColumns(Table reversedTable, Mapping mapping) {
        String reversedColumns = reversedTable.getColumns().collect({ "$it.name" }).join(',\n')
        String modelColumns = mapping.getColumns().collect({ "$it.alias" }).join(',\n')
        Assert.assertEquals("Column definition of deployed ${mapping.refference} does not match to model.", modelColumns, reversedColumns)
    }

    protected String toString(Column c) {
        "${c.name.padRight(30)} ${c.dataType.padRight(30)} ${c.description ?: ''}\n"
    }
}
