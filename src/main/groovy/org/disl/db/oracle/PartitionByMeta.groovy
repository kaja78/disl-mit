package org.disl.db.oracle;

/**
 * Provides Oracle table partitioning meta data.
 * */
interface PartitionByMeta {
    String getPartitionByClause();
}