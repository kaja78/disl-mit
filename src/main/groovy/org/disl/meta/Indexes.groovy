package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Container for database table index definition.
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface Indexes {
    Index[] value()
}
