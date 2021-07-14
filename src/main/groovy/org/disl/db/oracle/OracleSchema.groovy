package org.disl.db.oracle

import org.disl.db.reverseEngineering.ReverseEngineeringService
import org.disl.meta.PhysicalSchema

/**
 * Oracle physical schema implementation.
 * */
class OracleSchema extends PhysicalSchema {
    String host
    String port = 1521
    String jdbcDriver = "oracle.jdbc.OracleDriver"

    @Override
    public void init() {
        super.init();
        if (!getJdbcUrl()) {
            host = getSchemaProperty("host", host)
            port = getSchemaProperty("port", port)
            setSid(getSchemaProperty("sid"))
            setServiceName(getSchemaProperty("serviceName"))
            setJdbcUrl("jdbc:oracle:thin:@${getHost()}:${getPort()}${getDatabaseName()}")
        }
    }

    public void setSid(String sid) {
        if (sid != null) {
            setDatabaseName(":${sid}")
        }
    }

    public void setServiceName(String serviceName) {
        if (serviceName != null) {
            setDatabaseName("/${serviceName}")
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
        "select ${index} as DUMMY_KEY,${expressions} from dual"
    }

    @Override
    protected String getEvaluateRowCountQuery(String sqlQuery) {
        "select count(1) from (${sqlQuery})"
    }

    @Override
    public ReverseEngineeringService getReverseEngineeringService() {
        return new OracleReverseEngineeringService();
    }
}
