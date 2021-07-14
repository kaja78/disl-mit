package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Defines check constraint.
 * */
@Retention(RetentionPolicy.RUNTIME)
@interface Check {
    /**
     * Check condition SQL expression.
     * */
    String value()

    /**
     * Name of check constraint.
     */
    String name() default ''
}
