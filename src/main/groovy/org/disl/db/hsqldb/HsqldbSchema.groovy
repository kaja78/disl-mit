package org.disl.db.hsqldb

import org.disl.meta.PhysicalSchema

/**
 * Implementation of Hsqldb PhysicalSchema.
 * */
class HsqldbSchema extends PhysicalSchema {

    String jdbcDriver = "org.hsqldb.jdbcDriver"

    HsqldbSchema() {
        user = "sa"
    }

    @Override
    public void init() {
        super.init();
        if (!getJdbcUrl()) {
            databaseName = getSchemaProperty("databaseName", "mem:inmemoryDb")
            setJdbcUrl("jdbc:hsqldb:${databaseName}")
        }

    }

    @Override
    public String evaluateExpressionQuery(String expression) {
        "SELECT ${expression} FROM (VALUES (0))"
    }

    @Override
    public String evaluateConditionQuery(String expression) {
        "select 1 from (VALUES (0)) where ${expression}"
    }

    @Override
    public String getRecordQuery(int index, String expressions) {
        "select ${index} as DUMMY_KEY,${expressions} from (VALUES(0))"
    }

}
