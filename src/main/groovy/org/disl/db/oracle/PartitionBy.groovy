package org.disl.db.oracle

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * Defines Oracle table partitioning clause.
 * */
@Retention(RetentionPolicy.RUNTIME)
@interface PartitionBy {
    String value()
}
