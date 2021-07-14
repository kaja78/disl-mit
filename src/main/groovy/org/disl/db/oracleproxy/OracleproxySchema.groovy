package org.disl.db.oracleproxy

import groovy.util.logging.Slf4j
import org.disl.db.oracle.OracleSchema
import org.disl.meta.Context
import org.disl.meta.PhysicalSchema


/**
 * Implementation of DISL physical schema for Oracle Database supporting connection using proxy user.
 * */
@Slf4j
class OracleproxySchema extends OracleSchema {

    /**
     * DISL logical schema used for database connection.
     * */
    PhysicalSchema proxy

    @Override
    void init() {
        super.init()
        String proxySchema = getSchemaProperty('proxySchema', 'default')
        proxy = Context.getContext().getPhysicalSchema(proxySchema)
        log.debug("")

    }

    @Override
    String getUser() {
        return "${proxy.user}[${this.getSchema()}]"
    }

    @Override
    String getPassword() {
        return proxy.getPassword()
    }

    @Override
    String getJdbcUrl() {
        if (!proxy) {
            return null
        }
        return proxy.getJdbcUrl()
    }
}
