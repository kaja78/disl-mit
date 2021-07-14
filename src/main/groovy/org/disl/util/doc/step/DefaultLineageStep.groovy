package org.disl.util.doc.step


import org.disl.util.doc.IDocumentationStep.ILineageStep
import org.disl.util.doc.LineageRenderer

class DefaultLineageStep extends AbstractDocStep implements ILineageStep {

    String elementClassName

    private LineageRenderer r = new LineageRenderer()

    @Override
    protected String getFileName() {
        "model/${elementClassName}-lineage.html"
    }

    @Override
    public String getCode() {
        new LineageRenderer(metaManager, elementClassName).renderLineage()
    }

}
