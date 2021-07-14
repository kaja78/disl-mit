package org.disl.pattern

import groovy.sql.Sql
import groovy.transform.CompileStatic

import java.sql.SQLException

@CompileStatic
abstract class ExecuteSQLScriptStep extends Step {

    public static final String BACKSLASH_NEW_LINE = "\\\\\n"

    String commandSeparator = ";";

    abstract Sql getSql();

    public int executeInternal() {
        try {
            int processedRows = 0
            getCommands().each {
                processedRows = processedRows + executeSqlStatement(it)
            }
            getSql().commit()
            return processedRows
        } catch (Exception e) {
            getSql().rollback()
            throw e
        }
    }

    protected Collection<String> getCommands() {
        return Arrays.asList(code.split(getCommandSeparator()))
    }

    protected int executeSqlStatement(String sqlCommand) {
        if (''.equals(sqlCommand.trim())) {
            return 0
        }
        try {
            return executeSqlStatementInternal(sqlCommand)
        } catch (SQLException e) {
            throw new RuntimeException("${e.errorCode}: $e.message executing ${this}. SQL statement: $sqlCommand", e)
        } catch (Exception e) {
            throw new RuntimeException("${e.class.name}: $e.message executing ${this}. SQL statement: $sqlCommand", e)
        }
    }

    protected int executeSqlStatementInternal(String sqlCommand) {
        return getSql().executeUpdate(sqlCommand)
    }
}
