package org.disl.meta

import groovy.transform.CompileStatic

/**
 * Interface for Mappings defining load of target table.
 * The mapping column names are expected to exactly match target table column names.
 * */
@CompileStatic
interface TableMapping<T extends Table> {

    T getTarget()

    List<ColumnMapping> getColumns()

    String getInitialMapping(String columnName)

    default void checkUnmappedTargetColumns() {
        if (getUnmappedTargetColumns().size() > 0) {
            traceUnmappedColumns()
            throw new AssertionError("Found unmapped columns: ${getUnmappedTargetColumns().collect({ it.name }).join(',')}", null)
        }
    }

    default void checkColumnsMissingInTarget() {
        if (getColumnsMissingInTarget().size() > 0) {
            throw new AssertionError("Found columns missing in target: ${getColumnsMissingInTarget().join(',')}", null)
        }
    }

    default void traceUnmappedColumns() {
        getUnmappedTargetColumns().each { println getInitialMapping(it.name) }
    }

    default Collection<Column> getUnmappedTargetColumns() {
        return getTarget().getColumns().findAll {
            Column targetColumn = it
            boolean unmapped = true
            columns.each {
                if (it.getAlias().equals(targetColumn.name)) {
                    unmapped = false
                    return
                }
            }
            return unmapped
        }
    }

    default Collection<ColumnMapping> getColumnsMissingInTarget() {
        return getColumns().findAll {
            ColumnMapping columnMapping = it
            boolean missing = true
            target.columns.each {
                if (it.name.equals(columnMapping.alias)) {
                    missing = false
                }
            }
            return missing
        }
    }

}
