package org.disl.meta;

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Defines mapping expression for logical columns. @see LogicalColumn.
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultMapping {
    String value();
}
