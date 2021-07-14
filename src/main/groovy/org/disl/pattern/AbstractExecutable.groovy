package org.disl.pattern

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.disl.meta.Base
import org.disl.meta.MetaFactory

@Slf4j
@CompileStatic
abstract class AbstractExecutable extends Base implements Executable {

    boolean ignoreErrors = false
    ExecutionInfo executionInfo = new ExecutionInfo()

    public ExecutionInfo getExecutionInfo() {
        executionInfo
    }

    /**
     * Execute the step and return number of processed rows.
     * */
    protected abstract int executeInternal();

    @Override
    public final void execute() {
        try {
            executionInfo.start()
            beforeExecute()
            executionInfo.processedRows = executeInternal()
            executionInfo.finish()
        } catch (Exception e) {
            executionInfo.error(e)
            handleException(e)
        } finally {
            postExecute()
        }
    }

    public void handleException(Exception e) {
        if (!isIgnoreErrors()) {
            log.error("${e.class.name} executing $this: ${e.message}", e)
            throw e
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Ignored ${e.class.name} executing $this: ${e.message}", e)
            } else {
                log.warn("Ignored ${e.class.name} executing $this: ${e.message}")
            }
        }
    }

    /**
     * Hook to implement before execution logic.
     * */
    void beforeExecute() {
    }

    /**
     * Hook to implement post execution logic.
     * */
    void postExecute() {
    }

    Collection createAll(String packageName, Class assignableFrom) {
        return MetaFactory.createAll(packageName, assignableFrom)
    }

}
