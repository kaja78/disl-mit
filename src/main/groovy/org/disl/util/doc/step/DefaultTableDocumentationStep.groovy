package org.disl.util.doc.step

import org.disl.meta.Table
import org.disl.util.doc.IDocumentationStep.ITableDocumentationStep
import org.disl.util.doc.LineageRenderer

class DefaultTableDocumentationStep extends AbstractDocStep implements ITableDocumentationStep {

    Table table

    public String getFileName() {
        "model/${table.class.name}.html"
    }

    @Override
    public String getCode() {
        """\
<link rel="stylesheet" type="text/css" href="../disldoc.css">
${table.class.name}
<H1>Table ${table.name} (${table.schema})</H1> 
${table.description}

${LineageRenderer.renderContainer(table.class.name)}

<H2>Columns</H2>
<table>
<tr><th>Name</th><th>Data type</th><th>Decription</th></tr>
${table.columns.collect({ """<tr><td class="nowrap">$it.name</td><td class="nowrap">$it.dataType</td><td>$it.description</td></tr>\n""" }).join()}
</table>
"""
    }


}
