package org.disl.meta

import groovy.transform.CompileStatic;

/**
 * Defines logical column. 
 * Logical columns are used in data source (L0) model to enable to extract data from database functions or expression, which are only valid in data source database.
 * Logical columns should be used  with caution, because its usage hides data tranformation logic. 
 * */
@CompileStatic
class FunctionalColumn extends Column {
    String defaultMapping

    String getMapping() {
        if (defaultMapping != null) {
            return defaultMapping + ' ' + toString()
        }
        toString()
    }
}
