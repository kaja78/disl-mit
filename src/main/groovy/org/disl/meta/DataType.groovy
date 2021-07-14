package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Defines data type of Column.
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataType {
    String value()
}
