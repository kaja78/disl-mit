package org.disl.db.oracle

import org.disl.db.ChangedContextTest

abstract class OracleTest extends ChangedContextTest {

    @Override
    String getContextName() {
        'oracle.db.test'
    }
}
