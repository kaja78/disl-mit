package org.disl.meta

import groovy.transform.CompileStatic

/**
 * Abstract implementation of basic SQLExpression operators.
 * */
@CompileStatic
abstract class AbstractSqlExpression {

    AbstractSqlExpression plus(Object o) {
        return new SqlExpression(expression: { "${this}+${o}" })
    }

    AbstractSqlExpression minus(Object o) {
        return new SqlExpression(expression: { "${this}-${o}" })
    }

    AbstractSqlExpression multiply(Object o) {
        return new SqlExpression(expression: { "${this}*${o}" })
    }

    AbstractSqlExpression div(Object o) {
        return new SqlExpression(expression: { "${this}/${o}" })
    }

    AbstractSqlExpression mod(Object o) {
        return new SqlExpression(expression: { "${this}%${o}" })
    }

    AbstractSqlExpression or(Object o) {
        return new SqlExpression(expression: { "${this} or ${o}" })
    }

    AbstractSqlExpression and(Object o) {
        return new SqlExpression(expression: { "${this} and ${o}" })
    }

    AbstractSqlExpression concat(Object... o) {
        return new SqlExpression(expression: { "${this}||${o.join('||')}" })
    }

    AbstractSqlExpression IN(Object... o) {
        return new SqlExpression(expression: { "${this} IN (${o.join(',')})" })
    }

    AbstractSqlExpression isEqual(Object o) {
        return new SqlExpression(expression: { "${this.toString()}=${o.toString()}" })
    }

    AbstractSqlExpression isNotEqual(Object o) {
        return new SqlExpression(expression: { "${this.toString()}<>${o.toString()}" })
    }

    AbstractSqlExpression lessThan(Object o) {
        return new SqlExpression(expression: { "${this}<${o}" })
    }

    AbstractSqlExpression greaterThan(Object o) {
        return new SqlExpression(expression: { "${this}>${o}" })
    }

    AbstractSqlExpression lessOrEqualThan(Object o) {
        return new SqlExpression(expression: { "${this}<=${o}" })
    }

    AbstractSqlExpression greaterOrEqualThan(Object o) {
        return new SqlExpression(expression: { "${this}>=${o}" })
    }

    AbstractSqlExpression between(Object o, Object p) {
        return new SqlExpression(expression: { "${this} between ${o} and ${p}" })
    }


}
