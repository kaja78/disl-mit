package org.disl.util.doc

import org.disl.db.hsqldb.pattern.TestTruncateInsertMapping.TEST_TABLE
import org.disl.meta.TestMapping.TestingMapping
import org.disl.meta.TestTable.TESTING_TABLE
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LineageRendererTest {
    LineageRenderer renderer = new LineageRenderer()

    @Before
    void init() {
        renderer.sources = [TEST_TABLE.class.name, TESTING_TABLE.class.name, TEST_TABLE.class.name, TEST_TABLE.class.name, TEST_TABLE.class.name]
        renderer.elementClassName = TestingMapping.class.name
        renderer.targets = [TEST_TABLE.class.name, TESTING_TABLE.class.name]
    }


    @Test
    void testRenderLineage() {
        String expected = """\
<pre><code>
<a href='org.disl.db.hsqldb.pattern.TestTruncateInsertMapping\$TEST_TABLE.html' target='_parent'>TEST_TABLE</a>    -->                                 
<a href='org.disl.meta.TestTable\$TESTING_TABLE.html' target='_parent'>TESTING_TABLE</a> -->                --> <a href='org.disl.db.hsqldb.pattern.TestTruncateInsertMapping\$TEST_TABLE.html' target='_parent'>TEST_TABLE</a>   
<a href='org.disl.db.hsqldb.pattern.TestTruncateInsertMapping\$TEST_TABLE.html' target='_parent'>TEST_TABLE</a>    --> TestingMapping --> <a href='org.disl.meta.TestTable\$TESTING_TABLE.html' target='_parent'>TESTING_TABLE</a>
<a href='org.disl.db.hsqldb.pattern.TestTruncateInsertMapping\$TEST_TABLE.html' target='_parent'>TEST_TABLE</a>    -->                                 
<a href='org.disl.db.hsqldb.pattern.TestTruncateInsertMapping\$TEST_TABLE.html' target='_parent'>TEST_TABLE</a>    -->                                 
</code></pre>"""
        Assert.assertEquals(expected, renderer.renderLineage())
    }

}
