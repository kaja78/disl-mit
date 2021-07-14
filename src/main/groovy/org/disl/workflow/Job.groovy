package org.disl.workflow

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.disl.meta.MetaFactory
import org.disl.pattern.AbstractExecutable
import org.disl.pattern.Executable
import org.disl.pattern.Status

/**
 * Job executes list of job entries in serial order.
 * */
@Slf4j
@CompileStatic
abstract class Job extends AbstractExecutable {

    List<JobEntry> jobEntries = []
    Exception lastException
    boolean continueOnError = false

    /**
     * Add executable instance to job entry list.
     * */
    void add(Executable executable) {
        if (executable.isToExecute()) {
            this.jobEntries.add(new JobEntry(executable: executable))
        }
    }

    /**
     * Create new instance of Executable and add it to job entry list.
     * */
    public Job addType(Class<? extends Executable> type) {
        add((Executable) MetaFactory.create(type))
        return this
    }

    /**
     * Create list of Executable instances and add it to job entry list.
     * */
    public Job addTypes(List<Class<? extends Executable>> types) {
        types.each({ addType(it) })
        return this
    }

    /**
     * Add list of executables to job entry list.
     * */

    public Job addAll(List<? extends Executable> executables) {
        executables.each { add(it) }
        return this
    }

    /**
     * Find, create and add executables in job package and all subpackages to job entry list.
     * Compiled executables must be located in the same classpath element (directory or jar).
     * @param assignableType Only classes assignable from assignableType will be added to job entry list.
     * */
    public Job addAll(Class assignableType) {
        addAll(MetaFactory.createAll(this.getClass().getPackage().getName(), assignableType));
    }

    /**
     * Find, create and add executables to job entry list.
     * Compiled executables must be located in the same classpath element (directory or jar).
     * @param rootPackage Root package to look for executables classes in.
     * @param assignableType Only classes assignable from assignableType will be added to job entry list.
     * */
    public Job addAll(String rootPackage, Class assignableType) {
        addAll(MetaFactory.createAll(rootPackage, assignableType));
    }

    @Override
    void beforeExecute() {
        super.beforeExecute()
        log.info("Starting execution of job ${getName()}. (${getJobEntries().size()} entries.)")
    }

    protected int executeInternal() {
        jobEntries.each {
            execute(it)
        }
        if (lastException) {
            throw lastException
        }
        return executionInfo.processedRows
    }

    protected void execute(JobEntry entry) {
        if (this.executionInfo.status == Status.ERROR) {
            return
        }
        try {
            entry.execute()
        }
        catch (Exception e) {
            lastException = e
            if (!continueOnError) {
                executionInfo.status = Status.ERROR
            }
        }
    }

    @Override
    public void postExecute() {
        super.postExecute();
        updateExecutionInfo()
        traceStatus()
    }

    void updateExecutionInfo() {
        executionInfo.processedRows = 0
        jobEntries.each {
            executionInfo.processedRows += it.executionInfo.processedRows
        }
    }

    public void simulate() {
        jobEntries.each { it.simulate() }
    }

    public void traceStatus() {
        if (log.infoEnabled) {
            log.info(getExecutionSummaryMessage())
        }
    }

    public String getExecutionSummaryMessage() {
        String name = toString().padRight(50).toString().substring(0, 50)
        String dur = executionInfo.duration.toString().padLeft(10).toString().substring(0, 10)
        String stat = executionInfo.status.toString().padLeft(10).toString().substring(0, 10)
        String processedRows = executionInfo.processedRows.toString().padLeft(10).toString().substring(0, 10)
        return """ Execution results for ${name}:
*********************************************************************************************
*  Name                                              *   Status   *  Time (ms)*        Rows *
*********************************************************************************************
* ${name} * ${stat} * ${dur} * ${processedRows} *
*********************************************************************************************
${jobEntries.join('\n')}
*********************************************************************************************
"""
    }
}
