package org.disl.pattern


import groovy.transform.CompileStatic
import org.disl.meta.Table

/**
 * Pattern for Table.
 * */
@CompileStatic
abstract class TablePattern<T extends Table> extends Pattern {
    T table

    final T getTable() {
        table
    }

    void addSqlScriptStep(String name, String code) {
        add(ExecuteSQLScriptTableStep.create(name, code))
    }

    @Override
    public String toString() {
        "${this.getClass().getSimpleName()}(${getTable()})"
    }
}
