package org.disl.util.jenkins

import org.disl.meta.Base
import org.disl.meta.Mapping
import org.disl.meta.Table
import org.disl.pattern.ExecutionInfo
import org.disl.pattern.Pattern
import org.disl.pattern.Status
import org.disl.workflow.Job

/**
 * Formats job execution info as JUnit XML suitable for processing by Jenkins publish JUnit test result report post build action.
 */
class JobJUnitFormat {

    Job job

    private String jobEntryIndex

    void save(File dir = new File('build/test-results')) {
        dir.mkdirs()
        def fileWriter = new OutputStreamWriter(new FileOutputStream(new File(dir, "${job.class.name}.xml")), 'UTF-8')
        format(fileWriter)
        fileWriter.close()
    }

    String format() {
        Writer w = new StringWriter()
        format(w)
        w.toString()
    }

    void format(Writer w) {
        w.append('<?xml version="1.0" encoding="UTF-8"?>\n')
        w.append('<testsuite>\n')
        w.append(formatSummary())
        job.getJobEntries().each {
            jobEntryIndex = formatIndex(job.getJobEntries(), it)
            w.append(format(it.getExecutable()))
        }
        w.append('</testsuite>')
    }

    protected String formatSummary() {
        """\t<testcase name="executionSummary" classname="${job.class.name}.!Summary" time="0">${formatCode(job.getExecutionSummaryMessage())}</testcase>\n"""
    }

    protected String format(Mapping mapping) {
        format(mapping.pattern, mapping)
    }

    protected String format(Table table) {
        format(table.pattern, table)
    }

    protected String format(Pattern pattern) {
        format(pattern, pattern)
    }

    protected String format(def executable) {
        format(executable.toString(), job.class.name, executable.getExecutionInfo() as ExecutionInfo)
    }

    protected String format(Pattern pattern, Base parent) {
        pattern.getSteps().collect({ format("${formatIndex(pattern.getSteps(), it)}_${it.name}", "${job.class.name}.${jobEntryIndex}_${parent.name}", it.getExecutionInfo(), it.getCode()) }).join()
    }

    String formatIndex(List list, def item) {
        int index = list.indexOf(item) + 1
        int paddingSize = (list.size() / 10) + 1
        String.format("%0${paddingSize}d", index)
    }


    protected String format(String name, String parentName, ExecutionInfo executionInfo, String code = null) {
        """\t<testcase name="${name}" classname="${parentName}" time="${formatTime(executionInfo)}">${formatCode(code)}${formatStatus(executionInfo)}</testcase>\n"""
    }

    protected String formatCode(String code) {
        if (code) {
            return "<system-out><![CDATA[${code}]]></system-out>"
        }
        return ''
    }

    protected String formatStatus(ExecutionInfo executionInfo) {
        Status status = executionInfo.getStatus()
        if (status == Status.ERROR) {
            return """${formatStackTrace(executionInfo)}<failure message="${getRootCause(executionInfo.exception).getMessage()}"></failure>"""
        } else if (status == Status.FINISHED) {
            return ''
        } else {
            return "<skipped/>"
        }
    }

    protected String formatTime(ExecutionInfo executionInfo) {
        if (executionInfo.duration) {
            return executionInfo.duration / 1000
        }
        return 0
    }

    Exception getRootCause(Exception e) {
        if (e && e.getCause()) {
            return getRootCause(e.getCause())
        }
        return e
    }

    protected String formatStackTrace(ExecutionInfo executionInfo) {
        if (executionInfo.exception) {
            return "<system-err><![CDATA[${getStackTrace(executionInfo.exception)}]]></system-err>"
        }
        return ''
    }

    private String getStackTrace(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        PrintStream p = new PrintStream(out)
        e.printStackTrace(p)
        p.close()
        return out.toString().trim()
        //return e.stackTrace.join('\n')
    }

}
