package org.disl.meta

import groovy.transform.CompileStatic

/**
 * Data source of data transformation defined in Mapping.
 * */
@CompileStatic
abstract class MappingSource extends Base {
    String sourceAlias
    boolean sourceWithClause
    Join join = new Join.NONE(source: this)


    public abstract String getRefference();

    public abstract String getWithReference();

    public abstract String getRefferenceColumnList();

    public abstract String getSchema()

    public String toString() {
        return getClass().getSimpleName()
    }

    public String getFromClause() {
        return join.fromClause
    }

    /**
     * Define join condition based SQL expression.
     * */
    public MappingSource on(Object condition) {
        this.join.condition = condition.toString()
        this
    }

    /**
     * Shorthand method for on("${expression1}=${expression2}").
     * */
    public MappingSource on(Object expression1, Object expression2) {
        return on("${expression1}=${expression2}")
    }


}
