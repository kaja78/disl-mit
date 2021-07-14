package org.disl.meta;

import groovy.transform.CompileStatic

/**
 * Holds index metadata in DISL data model.
 * */
@CompileStatic
class IndexMeta {
    List<String> columnNames

    public static initIndexes(IndexOwner object) {
        Index index = object.getClass().getAnnotation(Index)
        if (index != null) {
            initIndex(index, object)
        }

        Indexes indexes = object.getClass().getAnnotation(Indexes)
        if (indexes != null) {
            indexes.value().each { initIndex((Index) it, object) }
        }
    }

    protected static void initIndex(Index index, IndexOwner object) {
        IndexMeta indexMeta = new IndexMeta(columnNames: Arrays.asList(index.columns()))
        object.indexes.add(indexMeta)
    }
}