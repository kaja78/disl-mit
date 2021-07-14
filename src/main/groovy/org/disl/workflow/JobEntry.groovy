package org.disl.workflow

import org.disl.pattern.Executable
import org.disl.pattern.ExecutionInfo

class JobEntry implements Executable {

    Executable executable

    ExecutionInfo getExecutionInfo() {
        executable.executionInfo
    }

    void execute() {
        executable.execute()
    }

    void simulate() {
        executable.simulate()
    }

    @Override
    boolean isToExecute() {
        return true
    }

    String toString() {
        String name = executable.toString().padRight(50).toString().substring(0, 50)
        String dur = executionInfo.duration.toString().padLeft(10).toString().substring(0, 10)
        String stat = executionInfo.status.toString().padLeft(10).toString().substring(0, 10)
        String processedRows = executionInfo.processedRows.toString().padLeft(10).toString().substring(0, 10)
        return "* ${name} * ${stat} * ${dur} * ${processedRows} *"
    }

}