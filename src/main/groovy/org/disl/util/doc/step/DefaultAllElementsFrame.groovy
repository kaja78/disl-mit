package org.disl.util.doc.step

class DefaultAllElementsFrame extends AbstractDocStep {

    @Override
    protected String getFileName() {
        'allelements-frame.html'
    }

    @Override
    public String getCode() {
        """\
<link rel="stylesheet" type="text/css" href="disldoc.css">
<H2>All elements</H2>
${metaManager.elements.collect({
            "<li>${link(it, 'model/')}</li>"
        }).join()}
"""
    }

}
