package org.disl.meta

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Container for check constraint definitions.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface CheckConstraints {
    Check[] value()
}
