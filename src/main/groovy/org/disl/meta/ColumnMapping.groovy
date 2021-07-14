package org.disl.meta

import groovy.transform.CompileStatic;

/**
 * ColumnMapping defines data transformation for single column in Mapping.
 * */
@CompileStatic
abstract class ColumnMapping extends AbstractSqlExpression {

    Mapping parent

    /**
     * Sql expression defining data transformation.
     * */
    String expression

    /**
     * Name of column in ResultSet.
     * */
    String alias

    String description

    String toString() {
        if (parent.getSourceAlias() == null) {
            return getAlias();
        }
        "${parent.getSourceAlias()}.${getAlias()}"
    }

    String getAliasedMappingExpression() {
        "$expression as $alias"
    }

}
