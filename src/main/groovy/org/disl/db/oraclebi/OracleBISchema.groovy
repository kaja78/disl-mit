package org.disl.db.oraclebi

import org.disl.meta.PhysicalSchema;

/**
 * Oracle BI server PhysicalSchema implementation.
 * */
class OracleBISchema extends PhysicalSchema {
    String host
    String port = 9703
    String jdbcDriver = "oracle.bi.jdbc.AnaJdbcDriver"


    @Override
    public void init() {
        super.init();
        if (!getJdbcUrl()) {
            host = getSchemaProperty("host", host)
            port = getSchemaProperty("port", port)
            setJdbcUrl("jdbc:oraclebi://${getHost()}:${getPort()}/")
        }
    }

    @Override
    public String evaluateExpressionQuery(String expression) {
        throw new UnsupportedOperationException()
    }

    @Override
    public String evaluateConditionQuery(String expression) {
        throw new UnsupportedOperationException()
    }

    @Override
    public String getRecordQuery(int index, String expressions) {
        throw new UnsupportedOperationException()
    }

}
