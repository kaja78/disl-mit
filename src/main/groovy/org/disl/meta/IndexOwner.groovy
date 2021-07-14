package org.disl.meta
/**
 * Data model element which is capable of defining indexes (table, materialized view ...).
 * */
interface IndexOwner {
    List<IndexMeta> getIndexes();
}