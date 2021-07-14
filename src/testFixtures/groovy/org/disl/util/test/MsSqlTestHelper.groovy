package org.disl.util.test

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.disl.meta.Mapping
import org.junit.Assert

class MsSqlTestHelper extends DislTestHelper {

    void validateViewDeployment(Mapping mapping, Sql sql) {
        super.validateViewDeployment(mapping, sql);

        String dataDictionaryQuery = "select VIEW_DEFINITION from ${getDatabaseName()}.INFORMATION_SCHEMA.VIEWS where TABLE_NAME='${mapping.name}' and TABLE_SCHEMA='${getSchema()}' and TABLE_CATALOG='${databaseName}'"
        GroovyRowResult result = sql.firstRow(dataDictionaryQuery)
        if (!result) {
            throw new AssertionError("View definition not found in data dictionary. Check ${dataDictionaryQuery}.")
        }
        String expected = """CREATE VIEW ${schema}.${mapping.name} AS
${mapping.getSQLQuery()}
"""
        String actual = result[0]

        Assert.assertEquals("View definition of deployed ${mapping.refference} does not match to model.", expected, actual)
    }
}
