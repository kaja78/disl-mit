package org.disl.meta

import groovy.transform.CompileStatic
import org.disl.meta.TestTable.TESTING_TABLE
import org.disl.test.DislTestCase
import org.disl.util.test.DislTestHelper
import org.junit.Assert
import org.junit.ComparisonFailure
import org.junit.Test

@CompileStatic
class TestPhysicalSchema extends DislTestCase {

    DislTestHelper testHelper=new DislTestHelper()

    @Test
    void testPassword() {
        Context.setContextName('disl-test')
        PhysicalSchema s1 = Context.getContext().getPhysicalSchema("s1")
        PhysicalSchema s2 = Context.getContext().getPhysicalSchema("s2")
        Assert.assertEquals('secure', s1.password)
        Assert.assertEquals('secure', s2.password)
    }

    @Test
    void testGetSql() {
        Context.setContextName('disl-test')
        Context.getContext().getPhysicalSchema("default").getSql()
    }

    @Test
    void testValidateTable() {
        TESTING_TABLE testingTable = (TESTING_TABLE) MetaFactory.create(TESTING_TABLE)
        testingTable.execute()
        PhysicalSchema defaultSchema = Context.getContext().getPhysicalSchema("default")
        testHelper.validateTableDeployment(testingTable)

        def A = testingTable.A
        testingTable.columns.remove(testingTable.A)
        try {
            testHelper.validateTableDeployment(testingTable)
            Assert.fail('ComparisonFailure expected')
        } catch (ComparisonFailure e) {
            Assert.assertTrue(e.getMessage().startsWith('Column definition of deployed PUBLIC.TESTING_TABLE does not match to model. expected:<[]B'))
        }

        testingTable.columns.add(A)
        try {
            testHelper.validateTableDeployment(testingTable)
            Assert.fail('ComparisonFailure expected')
        } catch (ComparisonFailure e) {
            Assert.assertTrue(e.getMessage().startsWith('Column definition of deployed PUBLIC.TESTING_TABLE does not match to model. expected:<[B'))
        }


    }
}
