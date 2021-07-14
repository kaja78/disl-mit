package org.disl.meta

import groovy.transform.CompileStatic

/**
 * Defines set operation to be performed on Mapping's sources.
 * */
@CompileStatic
public abstract class SetOperation {

    MappingSource source

    public abstract String getSetOperationClause();

    protected String getSourceQuery() {
        "select ${source.getRefferenceColumnList()} from ${source.getRefference()}"
    }

    static class UNION extends SetOperation {
        @Override
        public String getSetOperationClause() {
            "UNION ${sourceQuery}"
        }
    }

    static class UNION_ALL extends SetOperation {
        @Override
        public String getSetOperationClause() {
            "UNION ALL ${sourceQuery}"
        }
    }

    static class INTERSECT extends SetOperation {
        @Override
        public String getSetOperationClause() {
            "INTERSECT ${sourceQuery}"
        }
    }

    static class MINUS extends SetOperation {
        @Override
        public String getSetOperationClause() {
            "MINUS ${sourceQuery}"
        }
    }


}
