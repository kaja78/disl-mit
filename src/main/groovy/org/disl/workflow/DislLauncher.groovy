package org.disl.workflow

import org.disl.meta.Context
import org.disl.meta.MetaFactory


/**
 * Launcher for data integration processes implemented in DISL.
 * */
class DislLauncher {

    String[] arguments

    String className
    String contextName = "default"

    /**
     * Main methods expecting 2 arguments.
     * First argument is className of Executable implementing DISL data integration process.
     * Second argument is name of context which will be used for execution. This argument is optional, default context will be used by default.
     * */
    public static void main(String[] args) {
        new DislLauncher(arguments: args).execute()
        System.exit(0)
    }

    public void execute() {
        init()
        Context.setContextName(getContextName());
        def executable = MetaFactory.create(Class.forName(getClassName()))
        executable.execute()
    }

    protected void init() {
        if (arguments.length < 1 || arguments.length > 2) {
            throw new IllegalArgumentException("Invalid argument count. Usage: [className] ([contextName])");
        }
        className = arguments[0]
        if (arguments.length == 2) {
            contextName = arguments[1]
        }
    }
}
