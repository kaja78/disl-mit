package org.disl.db.oracle


import groovy.sql.Sql
import org.disl.meta.Context
import org.junit.*

/**
 * Unit test for OracleReverseEngineerig service.
 * This test is ignored by default and may be run manually from IDE.
 * To run this test manualy:
 * 1) Copy Oracle JDBC driver jars into lib dir.
 * 2) Create context configuration file name oracle.db.test.context.properties in src/test/resources
 *
 * Template for context config:
 *
 default=Oracle
 default.host=localhost
 default.port=1521
 default.serviceName=orcl
 default.schema=[user]
 default.user=[user]
 default.password=[password]

 * */
@Ignore
class TestOracleReverseEngineeringService extends OracleTest {

    OracleReverseEngineeringService s
    Sql sql


    @Before
    void init() {
        super.init()
        s = Context.getReverseEngineeringService('default')
        sql = Context.getSql('default')
        sql.execute 'create table DISL_TEST_ORA_REVERSE (C32CHAR char(32 char),C32 char(32),VC4000 varchar2(4000),VC4000CHAR varchar2(4000 CHAR),CDATE DATE,CTIMESTAMP TIMESTAMP,CTIMESTAMP_9 TIMESTAMP(9),CN NUMBER,CN38 NUMBER(38),CN38_2 NUMBER(38,2),NC_30 NCHAR(32),NVC2_32 NVARCHAR2(32))'
        sql.execute 'comment on table DISL_TEST_ORA_REVERSE is \'DISL reverse engineering testing table.\''
        sql.execute 'comment on column DISL_TEST_ORA_REVERSE.C32CHAR is \'Data type is CHAR(32 CHAR).\''
        sql.execute 'comment on column DISL_TEST_ORA_REVERSE.C32 is \'Data type is CHAR(32).\''
    }

    @Test
    void testReverseEngineerSchema() {
        new File("build/test/oracle/DISL_TEST_ORA_REVERSE.groovy").delete()
        s.reverseSchemaTables('oracle', 'DISL_TEST_ORA_REVERSE', Context.getPhysicalSchemaName('default'), new File('build/test'))
        String expected = '''\
package oracle

import org.disl.meta.*

@Description("""DISL reverse engineering testing table.""")
@groovy.transform.CompileStatic
class DISL_TEST_ORA_REVERSE extends AbstractOracleTable {

\t\t@Description("""Data type is CHAR(32 CHAR).""")
\t\t@DataType("CHAR(32 CHAR)")
\t\tColumn C32CHAR

\t\t@Description("""Data type is CHAR(32).""")
\t\t@DataType("CHAR(32)")
\t\tColumn C32

\t\t
\t\t@DataType("VARCHAR2(4000)")
\t\tColumn VC4000

\t\t
\t\t@DataType("VARCHAR2(4000 CHAR)")
\t\tColumn VC4000CHAR

\t\t
\t\t@DataType("DATE")
\t\tColumn CDATE

\t\t
\t\t@DataType("TIMESTAMP(6)")
\t\tColumn CTIMESTAMP

\t\t
\t\t@DataType("TIMESTAMP(9)")
\t\tColumn CTIMESTAMP_9

\t\t
\t\t@DataType("NUMBER")
\t\tColumn CN

\t\t
\t\t@DataType("NUMBER(38)")
\t\tColumn CN38

\t\t
\t\t@DataType("NUMBER(38,2)")
\t\tColumn CN38_2

\t\t
\t\t@DataType("NCHAR(32)")
\t\tColumn NC_30

\t\t
\t\t@DataType("NVARCHAR2(32)")
\t\tColumn NVC2_32
}'''
        Assert.assertEquals(expected, new File("build/test/oracle/DISL_TEST_ORA_REVERSE.groovy").getText())
    }

    @After
    void after() {
        sql.execute('drop table DISL_TEST_ORA_REVERSE')
    }

}
