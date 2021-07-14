package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Name of DISL model element. Value of this anotation overrides default DISL element name (which is typically class simple name).
 */
@Retention(RetentionPolicy.RUNTIME)
@interface Name {
    String value()
}
