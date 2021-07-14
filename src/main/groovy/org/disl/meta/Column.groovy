package org.disl.meta

import groovy.transform.CompileStatic;

/**
 * Column of database table or view.
 * */
@CompileStatic
class Column extends AbstractSqlExpression {
    String name
    MappingSource parent
    String description
    String dataType
    String defaultValue
    String check
    boolean notNull = false
    boolean primaryKey = false

    Column() {}

    Column(String name, Table parent) {
        this.name = name
        this.parent = parent
    }

    String getColumnDefinition() {
        getPhysicalSchema().getColumnDefinition(this)
    }

    PhysicalSchema getPhysicalSchema() {
        Context.getContext().getPhysicalSchema(getParent().getSchema())
    }


    String toString() {
        if (parent == null || parent.getSourceAlias() == null) {
            return name
        }
        "${parent.getSourceAlias()}.${getName()}"
    }
} 