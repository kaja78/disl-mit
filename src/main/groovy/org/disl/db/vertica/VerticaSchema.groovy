package org.disl.db.vertica

import org.disl.meta.PhysicalSchema

/**
 * Vertica analytic platform PhysicalSchema implementation.
 * */
class VerticaSchema extends PhysicalSchema {

    String host
    String port = 5433
    String jdbcDriver = "com.vertica.Driver"

    @Override
    public void init() {
        super.init();
        if (!getJdbcUrl()) {
            host = getSchemaProperty("host", host)
            port = getSchemaProperty("port", port)
            setJdbcUrl("jdbc:vertica://${host}:${port}/${databaseName}")
        }
    }

    @Override
    public String evaluateExpressionQuery(String expression) {
        "SELECT ${expression} FROM dual"
    }

    @Override
    public String evaluateConditionQuery(String expression) {
        "select 1 from dual where ${expression}"
    }

    @Override
    public String getRecordQuery(int index, String expressions) {
        "select ${index} as DUMMY_KEY,${expressions} from dual\n"
    }
}
