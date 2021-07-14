package org.disl.db.mssql


import groovy.sql.Sql
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.disl.meta.PhysicalSchema

/**
 * Implementation of MS SQL Server PhysicalSchema based on JTDS JDBC driver (groupId: net.sourceforge.jtds, artifactId: jtds). 
 * */
@Slf4j
@CompileStatic
class MssqlSchema extends PhysicalSchema {
    String host
    int port
    String instance

    String jdbcDriver = "net.sourceforge.jtds.jdbc.Driver"

    @Override
    public void init() {
        super.init();
        if (!getJdbcUrl()) {
            host = getSchemaProperty('host')
            port = Integer.parseInt(getSchemaProperty('port', '1433'))
            databaseName = getSchemaProperty('databaseName')
            instance = getSchemaProperty('instance')
            if (getInstance() == null) {
                setJdbcUrl("jdbc:jtds:sqlserver://${getHost()}:${getPort()}/${getDatabaseName()};user=${getUser()};password=${getPassword()};".toString())
            } else {
                setJdbcUrl("jdbc:jtds:sqlserver://${getHost()}:${getPort()}/${getDatabaseName()};instance=${getInstance()};user=${getUser()};password=${getPassword()};".toString())
            }
        }
    }

    @Override
    protected Sql createSql() {
        Sql sql
        try {
            //Pass username & password to jdbcUrl to prevent SSO error when ntlmauth.dll is not on path.
            sql = Sql.newInstance(getJdbcUrl(), getJdbcDriver());
        } catch (Exception e) {
            throw new RuntimeException("Unable to create database connection for jdbcUrl: ${getJdbcUrl()}, user: ${getUser()}, password: ${getPassword() ? '*'.multiply(getPassword().length()) : null}.", e)
        }
        sql.getConnection().setAutoCommit(false)
        log.info("${name} - Created new jdbcConnection for url: ${getJdbcUrl()}, user: ${getUser()}.")
        return sql
    }

    @Override
    public String evaluateExpressionQuery(String expression) {
        "SELECT ${expression}"
    }

    @Override
    public String evaluateConditionQuery(String expression) {
        "select 1 where ${expression}"
    }

    @Override
    public String getRecordQuery(int index, String expressions) {
        "select ${index} as DUMMY_KEY,${expressions}\n"
    }


}
