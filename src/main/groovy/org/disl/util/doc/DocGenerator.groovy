package org.disl.util.doc

import groovy.util.logging.Slf4j
import org.disl.meta.Base
import org.disl.meta.Lookup
import org.disl.meta.Mapping
import org.disl.meta.Table
import org.disl.util.doc.IDocumentationStep.IJobDocumentationStep
import org.disl.util.doc.IDocumentationStep.ILineageStep
import org.disl.util.doc.IDocumentationStep.ILookupDocumentationStep
import org.disl.util.doc.IDocumentationStep.IMappingDocumentationStep
import org.disl.util.doc.IDocumentationStep.IPackageDocumentationStep
import org.disl.util.doc.IDocumentationStep.ITableDocumentationStep
import org.disl.util.doc.step.*
import org.disl.workflow.Job

@Slf4j
class DocGenerator {
    String outputFolder = 'build/docs/disldoc'
    MetaManager metaManager = new MetaManager()

    IDocumentationStep indexStep = new DefaultIndexStep(docGenerator: this)
    IDocumentationStep cssStep = new DefaultCssStep(docGenerator: this)
    IDocumentationStep allElementsStep = new DefaultAllElementsFrame(docGenerator: this)
    IDocumentationStep overviewFrameStep = new DefaultOverviewFrameStep(docGenerator: this)
    IDocumentationStep overviewSummaryStep = new DefaultOverviewSummaryStep(docGenerator: this)
    ITableDocumentationStep tableDocumentationStep = new DefaultTableDocumentationStep(docGenerator: this)
    IMappingDocumentationStep mappingDocumentationStep = new DefaultMappingDocumentationStep(docGenerator: this)
    ILookupDocumentationStep lookupDocumentationStep = new DefaultLookupDocumentationStep(docGenerator: this)
    IPackageDocumentationStep packageFrameStep = new DefaultPackageFrameStep(docGenerator: this)
    IPackageDocumentationStep packageSummaryStep = new DefaultPackageSummaryStep(docGenerator: this)
    IJobDocumentationStep jobDocumentationStep = new DefaultJobDocumentationStep(docGenerator: this)
    ILineageStep lineageStep = new DefaultLineageStep(docGenerator: this)


    static String link(Base base, String urlPrefix = '') {
        link(base.class.name, urlPrefix)
    }

    static String link(String baseClassName, String urlPrefix = '') {
        "<a href='${urlPrefix}${baseClassName}.html' target='main'>${MetaManager.getElementName(baseClassName)}</a>"
    }

    void addRootPackage(String rootPackage) {
        metaManager.addRootPackage(rootPackage)
    }

    public void generate() {
        clean()
        org.codehaus.groovy.runtime.NullObject.metaClass.toString = { return '' }
        metaManager.process({ generate(it) })
        generatePackages()
        generateLineage()
        indexStep.execute()
        cssStep.execute()
        allElementsStep.execute()
        overviewFrameStep.execute()
        overviewSummaryStep.execute()
    }

    void clean() {
        new File(getOutputFolder()).deleteDir()
    }

    protected void generate(Table table) {
        tableDocumentationStep.setTable(table)
        tableDocumentationStep.execute()
        generateEmptyLineage(table)
    }

    protected void generate(Mapping mapping) {
        mappingDocumentationStep.setMapping(mapping)
        mappingDocumentationStep.execute()
        generateEmptyLineage(mapping)
    }

    protected void generate(Lookup lookup) {
        lookupDocumentationStep.setLookup(lookup)
        lookupDocumentationStep.execute()
        generateEmptyLineage(lookup)
    }

    protected void generate(Job job) {
        jobDocumentationStep.setJob(job)
        jobDocumentationStep.execute()
        generateEmptyLineage(job)
    }

    protected void generateEmptyLineage(Base base) {
        new File(outputFolder, "model/${base.class.name}-lineage.html").createNewFile()
    }

    protected void generate(Base source) {
        log.warn("No documentation generated for ${source.class.name}.")
    }

    protected void generatePackages() {
        metaManager.packageContent.keySet().each {
            packageFrameStep.setPackageName(it)
            packageFrameStep.execute()
            packageSummaryStep.setPackageName(it)
            packageSummaryStep.execute()
        }
    }

    /**
     * Generate lineage. Overwrite default empty lineage.
     * */
    protected void generateLineage() {
        Set lineageElements = new HashSet()
        lineageElements.addAll(metaManager.sourceUsage.keySet())
        lineageElements.addAll(metaManager.targetUsage.keySet())
        lineageElements.each {
            lineageStep.setElementClassName(it)
            lineageStep.execute()
        }

    }
}
