package org.disl.util.test

import groovy.transform.CompileStatic
import junit.framework.JUnit4TestAdapter
import junit.framework.TestSuite
import org.disl.meta.Mapping
import org.disl.meta.Table
import org.disl.workflow.ClassFinder
import org.junit.runner.RunWith
import org.junit.runners.AllTests

@CompileStatic
@RunWith(AllTests.class)
abstract class AbstractDislTestSuite {

    private String rootPackage = getClass().getPackage().getName()
    private ClassFinder classFinder
    protected TestSuite testSuite

    protected ClassFinder getClassFinder() {
        if (!classFinder) {
            classFinder = ClassFinder.createClassFinder(rootPackage)
        }
        return classFinder
    }

    public void setRootPackage(String rootPackage) {
        classFinder = null
        this.rootPackage = rootPackage
    }

    public String getRootPackage() {
        return this.rootPackage
    }

    TestSuite getTestSuite() {
        if (!testSuite) {
            testSuite = new TestSuite(this.getClass().getName());
            initTestSuite()
        }
        return testSuite
    }

    protected void initTestSuite() {
        addTestSuite('mappings', Mapping)
        addTestSuite('tables', Table)
        addTestSuite('dislTestCase', AbstractDislTestCase)
    }

    protected void addTestSuite(String name, Class assignableType) {
        TestSuite suite = new TestSuite(name)
        getClassFinder().findNonAbstractTypes(assignableType).each {
            suite.addTest(new JUnit4TestAdapter((Class) it))
        }
        this.testSuite.addTest(suite)
    }
}
