package org.disl.workflow
/**
 * ParallelJob executes executables in parallel.
 * */
abstract class ParallelJob extends Job {

    @Override
    public int executeInternal() {
        ParallelJobExecutor.instance.execute(this)
        if (lastException) {
            throw lastException
        }
        return executionInfo.processedRows
    }

}
