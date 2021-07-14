package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Container for unique key definitions.
 * */
@Retention(RetentionPolicy.RUNTIME)
@interface UniqueKeys {
    UniqueKey[] value()
}
