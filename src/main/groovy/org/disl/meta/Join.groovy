package org.disl.meta;

import groovy.transform.CompileStatic

/**
 * Holds metadata for joining MappingSources in Mapping.
 * */
@CompileStatic
public abstract class Join {

    /**
     * Join MappingSource.
     * */
    MappingSource source

    /**
     * Join condition.
     * */
    String condition

    /**
     * Generate part of SQL query from clause for given MappingSource.
     * */
    public abstract String getFromClause();

    public static class NONE extends Join {
        public String getFromClause() {
            "$source.refference"
        }
    }

    public static class INNER extends Join {
        public String getFromClause() {
            "INNER JOIN $source.refference  ON ($condition)"
        }
    }

    public static class LEFT extends Join {
        public String getFromClause() {
            "LEFT OUTER JOIN $source.refference  ON ($condition)"
        }
    }

    public static class RIGHT extends Join {
        public String getFromClause() {
            "RIGHT OUTER JOIN $source.refference  ON ($condition)"
        }
    }

    public static class FULL extends Join {
        public String getFromClause() {
            "FULL OUTER JOIN $source.refference  ON ($condition)"
        }
    }

    public static class CARTESIAN extends Join {
        public String getFromClause() {
            "CROSS JOIN $source.refference"
        }
    }
}
