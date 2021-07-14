package org.disl.util.test

import groovy.sql.Sql
import org.disl.meta.Mapping

import java.sql.SQLException

class OracleTestHelper extends DislTestHelper {
    @Override
    void validateQuery(Mapping mapping, Sql sql=getSql(mapping.getSchema())) throws AssertionError {
        try {
            getSql().execute(getValidationQuery(mapping))
        } catch (SQLException e) {
            throw new AssertionError("Validation failed with message: ${e.getMessage()} for query:\n${getValidationQuery(mapping)}")
        }
    }

    @Override
    protected String getValidationQuery(Mapping mapping) {
        """\
declare
${getBindVariablesDeclaration(mapping)} 
\tcursor c is ${mapping.getSQLQuery()}; 
begin 
\tnull; 
end;"""
    }

    static String getBindVariablesDeclaration(Mapping mapping) {
        mapping.getBindVariables().collect({
            """\
\t${it.name} ${it.dataType}:=${it.value};
"""
        }).join()
    }
}
