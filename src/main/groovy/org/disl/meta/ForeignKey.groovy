package org.disl.meta

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Defines foreign key for Table or Table column.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.FIELD, ElementType.TYPE])
public @interface ForeignKey {
    Class<Table> targetTable()

    String name() default ''

    String sourceColumn() default ''

    String targetColumn() default ''
}
