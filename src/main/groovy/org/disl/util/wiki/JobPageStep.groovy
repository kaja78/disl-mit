package org.disl.util.wiki

import groovy.util.logging.Slf4j
import org.disl.meta.Base
import org.disl.pattern.Executable
import org.disl.pattern.FileOutputStep
import org.disl.workflow.Job
import org.disl.workflow.JobEntry

@Slf4j
class JobPageStep extends FileOutputStep {

    Job job

    @Override
    File getFile() {
        return WikiHelper.getWikiPageFile(job)
    }

    @Override
    String getCharset() {
        'utf-8'
    }


    @Override
    public String getCode() {
        """\
${renderHeader()}

${WikiHelper.renderElementDescription(job)}

${this.job.getJobEntries().collect({ renderJobEntry(it) }).join()}

"""
    }

    String renderHeader() {
        """\
+++
    title= "${job.name}"
    sourceFileLink="${job.sourceFileLink}"
    packages=["${job.getClass().getPackage().getName().replace('.', '/')}"]
    types=["Job"]
+++
"""
    }

    String renderJobEntry(JobEntry entry) {
        Executable executable = entry.executable
        if (executable instanceof Base) {
            return "* [${executable.name}](${url(executable)}) - ${executable.description}\n"
        }
        return "* ${executable.class.name}\n"
    }

    protected String url(Executable executable) {
        try {
            return WikiHelper.url(executable)
        } catch (Exception e) {
            log.warn("Unable to get url for Executable ${executable}.")
        }

    }
}