package org.disl.util.doc.step

import org.disl.util.doc.IDocumentationStep.IJobDocumentationStep
import org.disl.util.doc.LineageRenderer
import org.disl.workflow.Job

class DefaultJobDocumentationStep extends AbstractDocStep implements IJobDocumentationStep {
    Job job

    @Override
    protected String getFileName() {
        "model/${job.class.name}.html"
    }

    @Override
    public String getCode() {
        """\
<link rel="stylesheet" type="text/css" href="../disldoc.css">
${job.class.name}
<H1>Job ${job.name}</H1> 

${LineageRenderer.renderContainer(job.class.name)}
"""
    }


}
