package org.disl.pattern

import groovy.transform.CompileStatic
import org.disl.meta.Base
import org.disl.meta.Context
import org.disl.meta.MetaFactory
import org.disl.util.clipboard.ClipboardHelper
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals;

class TestPattern {

    TestingPattern testingPattern

    @Before
    void initTest() {
        Context.setContextName('disl-test')
        Context.getContext().setExecutionMode(Context.EXECUTION_MODE_DEFAULT)
        testingPattern = MetaFactory.create(TestingPattern)
    }

    @Test
    @CompileStatic
    public void testSimulate() {
        testingPattern.simulate();
        assertEquals("DROP \nBaseMock", testingPattern.steps[0].code)
    }

    @Test
    public void testCopyCodeToClipboard() {
        testingPattern.copyCodeToClipboard()
        String expected = """\
DROP 
BaseMock"""
        assertEquals(expected, ClipboardHelper.getContent())

    }

    @Test
    public void testExecute() {
        testingPattern.execute();
    }

    @Test
    void testSteps() {
        Assert.assertEquals(1, testingPattern.steps.size())
    }

    private static class BaseMock extends Base {}

    static class TestingPattern extends Pattern {
        BaseMock element = new BaseMock()

        @Override
        public void init() {
            add TestingStep
            add TestingExcludedStep
        }
    }

    static class TestingStep extends Step {
        String getCode() {
            """\
DROP 
${getPattern().element.getName()}"""
        }

        @Override
        protected int executeInternal() {
            return 1;
        }
    }

    static class TestingExcludedStep extends TestingStep {

        @Override
        List<String> getExecutionModes() {
            return ['doNotExecute']
        }
    }
}
