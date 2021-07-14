package org.disl.meta;

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Defines detailed description for model element.
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String value();
}
