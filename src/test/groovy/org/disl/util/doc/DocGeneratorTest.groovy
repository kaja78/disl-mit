package org.disl.util.doc


import org.junit.Test

class DocGeneratorTest {

    DocGenerator g = new DocGenerator()

    @Test
    void testGenerate() {
        g.addRootPackage("org.disl")
        g.generate()
    }
}
