package org.disl.util.sqlDep

import groovy.transform.CompileStatic
import org.junit.Ignore
import org.junit.Test

@Ignore
@CompileStatic
class TestSqlDepService {

    @Test
    void testPost() {
        SqlDepService sqlDep = new SqlDepService(restUrl: SqlDepService.SQL_DEP_MOCK_URL)
        CreateSqlSetRequest request = new CreateSqlSetRequest(customSqlSetName: 'test', queries: [new SqlDepQuery(sourceCode: 'select 1 as a from dual', groupName: 'testing group', name: 'testing query')])
        println sqlDep.post(request)
    }
}
