package org.disl.workflow

import groovy.util.logging.Slf4j
import org.disl.meta.Context
import org.disl.pattern.Executable

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Handles parallel execution of job entries.
 * */
@Slf4j
@Singleton(lazy = true, strict = false)
class ParallelJobExecutor {
    int parallelJobsInProgress = 0

    private ExecutorService executorService
    private ExecutorService parallelJobExecutorService

    protected ParallelJobExecutor() {
        addShutdownHook { shutdownExecutors() }
    }


    private synchronized ExecutorService getParallelJobExecutorService() {
        if (parallelJobExecutorService == null) {
            parallelJobExecutorService = Executors.newCachedThreadPool()
        }
        return parallelJobExecutorService
    }

    private synchronized ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(Integer.parseInt(Context.getContextProperty("disl.parallelExecutorThreads", "4")))
        }
        return executorService
    }

    /**
     * Execute Job's jobEntries in parallel.
     * */
    void execute(Job job) {
        def parallelFutures = submitParallelJobTasks(job)
        def futures = submitTasks(job)
        try {
            checkResults(job, parallelFutures)
            checkResults(job, futures)
        } catch (Exception e) {
            throw new RuntimeException("Exception in asynchronous execution.", e)
        }
    }

    List<Future> submitParallelJobTasks(Job job) {
        Collection<Executable> executables = job.getJobEntries()
        Collection<Callable> parallelJobTasks = executables.findAll({ isParallelJobEntry(it) }).collect({ createCallable(job, it) })
        def service = getParallelJobExecutorService()
        return parallelJobTasks.collect({ service.submit(it) })
    }

    protected List<Future> submitTasks(Job job) {
        Collection<Executable> executables = job.getJobEntries()
        Collection<Callable> tasks = executables.findAll({ !isParallelJobEntry(it) }).collect({ createCallable(job, it) })
        def service = getExecutorService()
        return tasks.collect({ service.submit(it) })
    }

    public void shutdownExecutors() {
        if (executorService != null) {
            executorService.shutdown()
            executorService = null
        }
        if (parallelJobExecutorService != null) {
            parallelJobExecutorService.shutdown()
            parallelJobExecutorService = null
        }
    }

    protected boolean isParallelJobEntry(JobEntry jobEntry) {
        return (jobEntry.executable instanceof ParallelJob)
    }

    /**
     * Check result of Executable execution. The result should be null. However Exception is thrown if execution failed.
     * */
    protected void checkResults(Job job, List<Future> futures) {
        futures.each({
            try {
                it.get()
            } catch (Exception e) {
                if (!job.continueOnError) {
                    log.info('Exception in parallel job execution. Shutting down parallel job executor service.')
                } else {
                    log.info('Exception in parallel job execution. Continuing parallel job execution.')
                }
            }

        })
    }

    protected Callable createCallable(Job job, Executable executable) {
        Context parentContext = Context.getContext()
        return new Callable() {
            Object call() {
                Context.init(parentContext)
                job.execute(executable)
            }
        }
    }

}
