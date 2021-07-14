package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Set of DISL model elements.
 */
@Retention(RetentionPolicy.RUNTIME)
@interface Elements {
    Class<Base>[] value()
}
