package org.disl.pattern

import groovy.transform.CompileStatic
import org.disl.meta.Context

/**
 * Represents one operation to be performed as part of Pattern execution.
 * */
@CompileStatic
public abstract class Step extends AbstractExecutable {

    String name;

    /**
     *  Decides if this should be executed. By default only steps of the same execution mode as the context execution mode are executed.
     * */
    @Override
    public boolean isToExecute() {
        this.getExecutionModes().contains(Context.getContext().getExecutionMode())
    }

    /**
     * Step execution mode.
     * */
    List<String> getExecutionModes() {
        [Context.EXECUTION_MODE_DEFAULT]
    }

    Pattern pattern

    abstract String getCode()

    @Override
    public void simulate() {
        println "/*  Simulating step ${this}:*/"
        println """\t${code.replace("\n", "\n\t")}""";
    }

    public String getName() {
        if (name == null) {
            return this.getClass().getSimpleName()
        }
        return name
    }

    public void setName(String name) {
        this.name = name
    }

    public Context getContext() {
        Context.getContext()
    }

    @Override
    public String toString() {
        return getName()
    }
}
