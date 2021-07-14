package org.disl.db.oracle

import org.disl.meta.Table;

/**
 * Abstract parent for Oracle table. Add support for Oracle partitioning annotations.
 * @see PartitionBy and PartitionByRangeInterval.
 * */
abstract class OracleTable extends Table {

    PartitionByMeta partitionByMeta

    @Override
    public void init() {
        partitionByMeta = new PartitionByMetaImpl()
        PartitionByMetaImpl.initPartitionBy(this)
        PartitionByRangeIntervalMeta.initPartitionBy(this)
        super.init();
    }


    static class PartitionByMetaImpl implements PartitionByMeta {
        String partitionByClause = ''

        public static void initPartitionBy(def owner) {
            PartitionBy anotation = owner.getClass().getAnnotation(PartitionBy)
            if (anotation == null) {
                return;
            }
            owner.partitionByMeta = new PartitionByMetaImpl(
                    partitionByClause: anotation.value())
        }
    }

    static class PartitionByRangeIntervalMeta implements PartitionByMeta {
        String columnName
        String interval
        String defaultLessThan
        String subpartitioningClause


        String getPartitionByClause() {
            """\
PARTITION BY RANGE ($columnName) INTERVAL ($interval)
${subpartitioningClause}
    (PARTITION P_DEFAULT VALUES LESS THAN ($defaultLessThan))"""
        }

        public static void initPartitionBy(def owner) {
            PartitionByRangeInterval anotation = owner.getClass().getAnnotation(PartitionByRangeInterval)
            if (anotation == null) {
                return;
            }
            owner.partitionByMeta = new PartitionByRangeIntervalMeta(
                    columnName: anotation.columnName(),
                    interval: anotation.interval(),
                    defaultLessThan: anotation.defaultLessThan(),
                    subpartitioningClause: anotation.subpartitioningClasue())
        }
    }
}
