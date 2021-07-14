package org.disl.util.doc.step

import org.disl.util.doc.DocGenerator
import org.disl.util.doc.IDocumentationStep.IPackageDocumentationStep

class DefaultPackageFrameStep extends AbstractDocStep implements IPackageDocumentationStep {

    String packageName

    @Override
    protected String getFileName() {
        "model/${packageName}-frame.html";
    }

    @Override
    public String getCode() {
        """\
<link rel="stylesheet" type="text/css" href="../disldoc.css">
<a href="${packageName}-summary.html" target="main">${packageName}</a>
<H2>DISL elements</H2>
${metaManager.packageContent.get(packageName).collect {
            "<li>${DocGenerator.link(it)}</li>\n"
        }.join()}
"""
    }

}
