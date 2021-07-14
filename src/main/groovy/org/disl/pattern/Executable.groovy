package org.disl.pattern;

/**
 * Interface representing ability to perform some operation(s). 
 * In DISL this typically means to generate code based on provided data integration model element and eventually execute it in runtime engine (typically database server). 
 * */
public interface Executable {

    /**
     * Perform all operations related to this executable.
     * */
    public abstract void execute();

    /**
     * Simulate execution. Simulation
     * means tracing code or output without execution.
     */
    public abstract void simulate()

    public abstract ExecutionInfo getExecutionInfo()

    /**
     * Hint if this executable is meant for execution based on current application state.
     * */
    default boolean isToExecute() {
        true
    }
}