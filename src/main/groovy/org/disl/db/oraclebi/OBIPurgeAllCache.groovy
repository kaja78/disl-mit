package org.disl.db.oraclebi

import groovy.sql.Sql
import org.disl.pattern.ExecuteSQLQueryStep

/**
 * Purge Oracle BI Server cache.
 * */
class OBIPurgeAllCache extends ExecuteSQLQueryStep {

    Sql sql
    String code = 'Call SAPurgeAllCache()'

    @Override
    public int executeInternal() {
        int processedRow = super.executeInternal();
        //Open OBI connection blocks JVM shutdown.
        getSql().close()
        return processedRow
    }

}
