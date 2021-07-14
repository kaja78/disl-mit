package org.disl.util.doc

import org.disl.meta.Mapping
import org.disl.meta.MetaFactory
import org.disl.meta.Table
import org.disl.meta.TestTable
import org.disl.pattern.TablePattern
import org.disl.util.test.DislTestHelper
import org.disl.util.test.MsSqlTestHelper
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue;

public class MetaManagerTest {
    MetaManager mm = new MetaManager()

    @Test
    public void testAddRootPackage() {
        mm.addRootPackage('org.disl')
        mm.process({})
        assertTrue(mm.packageContent.get('org.disl.meta').contains('org.disl.meta.TestSubMapping$TestingSubMapping'))
        assertTrue(mm.sourceUsage.get('org.disl.meta.TestMapping$TestingMapping').contains('org.disl.meta.TestSubMapping$TestingSubMapping'))
        assertTrue(mm.targetUsage.get('org.disl.db.hsqldb.pattern.TestTruncateInsertMapping$TEST_TABLE').contains('org.disl.db.hsqldb.pattern.TestTruncateInsertMapping$TestMapping'))
    }

    @Test
    void testGetSourceDependencies() {
        assertEquals(3, MetaManager.getSourceDependencies(MetaFactory.create(TestingMapping)).size())
    }

    @Test
    void testHasDefaultPublicConstructor() {
        assertTrue(mm.hasDefaultPublicConstructor(TestTable.TESTING_TABLE))
    }


    static class TestingMapping extends Mapping {
        T1 src

        @Override
        void initMapping() {
            from(MetaFactory.create(T2))
            unionAll(MetaFactory.create(T3))
        }
    }

    static class T1 extends Table {
        @Override
        TablePattern getPattern() {
            return null
        }
    }

    static class T2 extends Table {
        @Override
        TablePattern getPattern() {
            return null
        }
    }

    static class T3 extends Table {
        @Override
        TablePattern getPattern() {
            return null
        }
    }

}
