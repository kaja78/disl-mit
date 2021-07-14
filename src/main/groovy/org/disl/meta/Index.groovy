package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Defines database table indes.
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
    String[] columns()
}
