package org.disl.pattern

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.disl.meta.Initializable
import org.disl.meta.MetaFactory
import org.disl.util.clipboard.ClipboardHelper

import java.awt.datatransfer.StringSelection

/**
 * Represents set of operations (steps) to be performed to implement MDA pattern for given DISL data integration model object.
 * Pattern for table may generate DDL for table deployment into target database or execute replication or change data capture process.
 * Pattern for mapping may generate or execute data integration logic.
 * Patterns are use to define generic transformation of DISL model objects into artifacts.
 * */
@Slf4j
@CompileStatic
public abstract class Pattern extends AbstractExecutable implements Initializable {

    List<Step> steps = []

    protected Pattern() {}


    public void add(Step step) {
        if (step.isToExecute()) {
            step.setPattern(this)
            steps.add(step)
        }
    }

    public void add(Class<Step> type) {
        Step step = (Step) MetaFactory.create(type)
        add(step)
    }

    public void add(Collection<Class<Step>> types) {
        types.each { add(it) }
    }

    @Override
    public int executeInternal() {
        long timestamp = System.currentTimeMillis();
        log.debug("Executing pattern $this:")
        int processedRows = 0
        steps.each { it.execute(); processedRows += it.executionInfo.processedRows }
        log.debug("${this} processed ${processedRows} rows in ${System.currentTimeMillis() - timestamp} ms.")
        return processedRows
    }

    @Override
    public void simulate() {
        println "Simulating pattern $this:"
        steps.each { it.simulate() }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * Copy code from all pattern steps to clipboard.
     * */
    public void copyCodeToClipboard() {
        StringBuffer sb = new StringBuffer()
        steps.each { sb.append(it.getCode()) }
        StringSelection ss = new StringSelection(sb.toString());
        ClipboardHelper.copy(ss)
    }

    public abstract void init();

    void traceStepExecutionInfo() {
        print stepExecutionInfoMessage
    }

    public String getStepExecutionInfoMessage() {
        """ Step execution info:
*********************************************************************************************
*  Name                                              *   Status   *  Time (ms)*        Rows *
*********************************************************************************************
${steps.collect({ formatExecutionInfo(it.name, it.executionInfo) }).join('\n')}
*********************************************************************************************
"""
    }

    String formatExecutionInfo(String name, ExecutionInfo executionInfo) {
        name = name.padRight(50).toString().substring(0, 50)
        String dur = executionInfo.duration.toString().padLeft(10).toString().substring(0, 10)
        String stat = executionInfo.status.toString().padLeft(10).toString().substring(0, 10)
        String processedRows = Integer.toString(executionInfo.processedRows).padLeft(10).toString().substring(0, 10)
        return "* ${name} * ${stat} * ${dur} * ${processedRows} *"
    }

}
