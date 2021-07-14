package org.disl.pattern

import java.sql.PreparedStatement;

@groovy.transform.CompileStatic
abstract class ExecuteSQLQueryStep extends ExecuteSQLScriptStep {

    @Override
    protected int executeSqlStatementInternal(String sqlCommand) {
        PreparedStatement stmt = getSql().getConnection().prepareStatement(sqlCommand)
        stmt.executeQuery().close()
        stmt.close()
        return 1
    }
}
