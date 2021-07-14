package org.disl.meta

import groovy.transform.CompileStatic


/**
 * Holds unique key metadata in DISL data model.
 * */
@CompileStatic
class UniqueKeyMeta {

    String[] columns = []

    public static void initUniqueKeys(Table table) {
        UniqueKeys uniqueKeys = table.getClass().getAnnotation(UniqueKeys)
        if (uniqueKeys != null) {
            uniqueKeys.value().each { UniqueKeyMeta.initUniqueKey(table, (UniqueKey) it) }
        }

        UniqueKey uniqueKey = table.getClass().getAnnotation(UniqueKey)
        if (uniqueKey != null) {
            UniqueKeyMeta.initUniqueKey(table, uniqueKey)
        }


    }

    private static void initUniqueKey(Table owner, UniqueKey uniqueKey) {
        owner.uniqueKeys.add(new UniqueKeyMeta(columns: uniqueKey.columns()))
    }
}
