package org.disl.pattern

import groovy.transform.CompileStatic;

@CompileStatic
class ExecutionInfo {

    /**
     * Get runtime status.
     * *
     * @return
     */
    Status status = Status.NEW

    /**
     * Get execution start time.
     * */
    Long startTime

    /**
     * Get execution end time.
     */
    Long endTime

    /**
     * Get number of processed rows.
     * */
    int processedRows = 0

    Exception exception

    /**
     * Get execution duration in miliseconds.
     * *
     * @return
     */
    Long getDuration() {
        if (startTime == null) {
            return null;
        }
        if (endTime == null) {
            return System.currentTimeMillis() - startTime
        }
        return endTime - startTime
    }

    void start() {
        startTime = System.currentTimeMillis()
        status = Status.RUNNING
    }

    void finish() {
        endTime = System.currentTimeMillis()
        status = Status.FINISHED
    }

    void error(Exception e) {
        endTime = System.currentTimeMillis()
        status = Status.ERROR
        exception = e
    }
}


