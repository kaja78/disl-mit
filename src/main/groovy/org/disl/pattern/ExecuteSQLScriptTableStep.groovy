package org.disl.pattern

import groovy.sql.Sql
import groovy.transform.CompileStatic
import org.disl.meta.Context
import org.disl.meta.Table

@CompileStatic
abstract class ExecuteSQLScriptTableStep<T extends Table> extends ExecuteSQLScriptStep {

    TablePattern getPattern() {
        (TablePattern) super.getPattern()
    }

    T getTable() {
        getPattern().getTable()
    }

    @Override
    public Sql getSql() {
        return Context.getSql(getTable().getSchema());
    }

    public static ExecuteSQLScriptTableStep create(final String stepName, String code) {
        ExecuteSQLScriptTableStep step = new ExecuteSQLScriptTableStep() {

            String getCode() {
                code
            }

            Sql getSql() {
                Context.getSql(getPattern().getTable().getSchema())
            }
        }
        step.setName(stepName)
        return step
    }


}
