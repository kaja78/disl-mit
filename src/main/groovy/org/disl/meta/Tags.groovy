package org.disl.meta

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Set of tags related to DISL model element.
 */
@Retention(RetentionPolicy.RUNTIME)
@interface Tags {
    String[] value()
}
