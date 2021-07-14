package org.disl.meta

import groovy.transform.CompileStatic

/**
 * Represent SQL expression in DISL model.
 * */
@CompileStatic
class SqlExpression extends AbstractSqlExpression {
    Object expression

    @Override
    public String toString() {
        getValue().toString()
    }

    protected Object getValue() {
        if (expression instanceof Closure) {
            return expression.call()
        }
        return expression
    }


}
