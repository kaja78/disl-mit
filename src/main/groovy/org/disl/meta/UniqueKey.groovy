package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * Defines unique key.
 * */
@Retention(RetentionPolicy.RUNTIME)
@interface UniqueKey {
    String[] columns()
}
