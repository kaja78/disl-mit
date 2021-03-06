package org.disl.meta

import groovy.sql.Sql
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.disl.db.reverseEngineering.ReverseEngineeringService
import org.disl.workflow.DislScript

/**
 * Generic physical database schema. 
 * Context is used to map logical schemas used in DISL model to physical schemas.
 * Physical schemas are specific for each execution environment (Context).
 * */
@Slf4j
@CompileStatic
public abstract class PhysicalSchema {

    String name
    String user
    String password
    String schema
    String databaseName
    String jdbcUrl
    int connectionValidationTimeout
    SqlProxy sqlProxy

    abstract void setJdbcDriver(String driver)

    abstract String getJdbcDriver()

    abstract String evaluateExpressionQuery(String expression)

    abstract String evaluateConditionQuery(String expression)

    abstract getRecordQuery(int index, String expressions);

    /**
     * Evaluate value of SQL expression.
     * */
    public Object evaluateExpression(def expression) {
        return getSql().firstRow(evaluateExpressionQuery(expression.toString())).getAt(0)
    }


    /**
     * Evaluate value of SQL agregate expression.
     * */
    public Object evaluateAggregateExpression(def expression, List<Map> records) {
        getSql().firstRow("select ${expression} from ${recordsToSubquery(records)}".toString()).getAt(0)
    }

    /**
     * Evaluate rowcount returned by SQL query.
     * */
    public long evaluateRowCount(String sqlQuery) {
        Long.parseLong(getSql().firstRow(getEvaluateRowCountQuery(sqlQuery)).getAt(0).toString())
    }


    protected String getEvaluateRowCountQuery(String sqlQuery) {
        "select count(1) from (${sqlQuery}) as s"
    }

    public void init() {
        Context context = Context.getContext();
        user = getSchemaProperty("user", user)
        schema = getSchemaProperty("schema", schema)
        databaseName = getSchemaProperty("databaseName", databaseName)
        jdbcUrl = getSchemaProperty('jdbcUrl', jdbcUrl)
        connectionValidationTimeout = Integer.parseInt(getSchemaProperty('connectionValidationTimeout', '5000'))
        initPassword()
    }

    protected String initPassword() {
        password = getSchemaProperty("password", password)
        String encodedPassword = getSchemaProperty("encodedPassword")
        if (encodedPassword) {
            password = DislScript.decode(encodedPassword)
        }
    }

    protected String getSchemaProperty(String key, String defaultValue) {
        Context.getContext().getProperty("${name}.${key}", defaultValue)
    }

    protected String getSchemaProperty(String key) {
        Context.getContext().getProperty("${name}.${key}")
    }

    public Sql getSql() {
        if (sqlProxy == null || (sqlProxy.sql.connection == null || !sqlProxy.sql.connection.isValid(connectionValidationTimeout))) {
            sqlProxy = createSqlProxy()
        }
        sqlProxy.sql
    }

    protected SqlProxy createSqlProxy() {
        def sql = createSql()
        return new SqlProxy(sql: sql)
    }

    protected Sql createSql() {
        Sql sql
        try {
            sql = Sql.newInstance(getJdbcUrl(), getUser(), getPassword(), getJdbcDriver())
        } catch (Exception e) {
            throw new RuntimeException("Unable to create database connection for jdbcUrl: ${getJdbcUrl()}, user: ${getUser()}, password: ${getPassword() ? '*'.multiply(getPassword().length()) : null}.", e)
        }
        sql.getConnection().setAutoCommit(false)
        log.info("${name} - Created new jdbcConnection for url: ${getJdbcUrl()}, user: ${getUser()}.")
        return sql
    }

    public String recordsToSubquery(List<Map> records) {
        String joinCondition = ""
        List aliases = findAliases(records)
        boolean firstSource = true
        String sourceList = aliases.collect {
            String alias = it
            int index = 0
            String innerQuery = records.collect { index++;
                mapToQuery(it, alias, index, firstSource) }.join("\n\t\tunion all\n\t\t")
            firstSource = false
            return "(${innerQuery}) $alias"
        }.join(",\n\t\t")
        joinCondition = aliases.collect({ "AND ${it}.DUMMY_KEY=${aliases[0]}.DUMMY_KEY" }).join("\n\t\t")
        return """${sourceList}
	where 1=1 ${joinCondition}"""
    }

    private List findAliases(List<Map> records) {
        List aliases = []
        records[0].keySet().each {
            String columnName = it.toString()
            if (columnName.contains('.')) {
                aliases.add(columnName.substring(0, columnName.indexOf('.')))
            }
        }
        if (aliases.size() == 0) {
            aliases.add("SRC")
        }
        return aliases
    }

    public String mapToQuery(Map<String, String> row, String sourceAlias, int index, boolean includeMissingSourceAliasColumns) {
        Map sourceAliasRow = row.findAll {
            String key = it.key.toString()
            return key.startsWith("${sourceAlias}.") || (includeMissingSourceAliasColumns && !key.contains('.'))
        }
        sourceAliasRow = sourceAliasRow.collectEntries { String key, value ->
            if (key.startsWith("${sourceAlias}.")) {
                key = key.substring(key.indexOf('.') + 1)
            }
            [key, value]
        }
        String expressions = sourceAliasRow.collect({ key, value -> "${value} as ${key}" }).join(",")
        return getRecordQuery(index, expressions)
    }


    String getColumnDefinition(Column column) {
        "${column.getName()} ${column.getDataType()}${getDefaultValueClause(column)}${getNotNullConstraint(column)}${getCheckConstraint(column)}"
    }

    String getNotNullConstraint(Column column) {
        if (column.isNotNull()) {
            return " NOT NULL"
        }
        return ''
    }


    String getDefaultValueClause(Column column) {
        if (column.getDefaultValue() == null) {
            return ''
        }
        return " DEFAULT ${column.getDefaultValue()}"

    }

    String getCheckConstraint(Column column) {
        if (column.getCheck()) {
            return " CHECK (${column.getCheck()})"
        }
        return ''
    }

    public ReverseEngineeringService getReverseEngineeringService() {
        return new ReverseEngineeringService()
    }

    @Override
    protected void finalize() throws Throwable {
        if (sqlProxy != null) {
            log.debug("${name} - Closing jdbcConnection for url: ${getJdbcUrl()}, user: ${getUser()}.")
            sqlProxy.close()
            setSqlProxy(null)
        }
    }

    static class SqlProxy {
        Sql sql

        SqlProxy() {
            addShutdownHook { close() }
        }

        void close() {
            if (sql != null) {
                sql.close()
            }
            sql = null
        }
    }

}
