package org.disl.db


import org.disl.meta.Context
import org.junit.Before

/**
 * Abstract parent for test classes which need to execute unit tests in special context.
 * Main purpose is for unit tests targeted for specific databases (Oracle, MSSql, Vertica ...),
 * which may require special infrastructure (testing db server) and 3rd party libraries not available in Maven central.
 * For such scenario:
 * 1) Define special context in /src/test/resources/[xxx].db.test.context.properties (will be gitignored by wildcard)
 * 2) Copy required jars into lib directory (jars will be gitignored by default)
 * 3) Implement test class by extending ChangeContextTest. Mark it with @Ignore to exclude it from default unit tests.
 * 4) Run unit tests manually from your IDE.
 */
abstract class ChangedContextTest {

    @Before
    void init() {
        ChangedContextTest.setContext(getContextName())
    }

    /**
     * Set con
     * */
    static void setContext(String name) {
        System.setProperty('disl.context', name)
        if (Context.getContext().getName().equals(name)) {
            return
        }
        Context.setContextName(name)
    }

    abstract String getContextName();

}
