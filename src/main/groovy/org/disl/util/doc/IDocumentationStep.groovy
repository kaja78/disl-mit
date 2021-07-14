package org.disl.util.doc


import org.disl.meta.Lookup
import org.disl.meta.Mapping
import org.disl.meta.Table
import org.disl.workflow.Job

interface IDocumentationStep {

    void execute()

    void simulate()

    DocGenerator getDocGenerator()

    void setDocGenerator(DocGenerator docGenerator)

    static interface ITableDocumentationStep extends IDocumentationStep {
        void setTable(Table table)
    }

    static interface IMappingDocumentationStep extends IDocumentationStep {
        void setMapping(Mapping mapping)
    }

    static interface ILookupDocumentationStep extends IDocumentationStep {
        void setLookup(Lookup lookup)
    }

    static interface IPackageDocumentationStep extends IDocumentationStep {
        void setPackageName(String packageName)
    }

    static interface IJobDocumentationStep extends IDocumentationStep {
        void setJob(Job job)
    }

    static interface ILineageStep extends IDocumentationStep {
        void setElementClassName(String elementClassName)
    }

}