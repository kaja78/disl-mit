package org.disl.util.wiki

import org.disl.meta.Table
import org.disl.pattern.FileOutputStep

/**
 * Generate markdown wiki page file with Table documentation.
 */
class TablePageStep extends FileOutputStep {

    Table table

    @Override
    File getFile() {
        return WikiHelper.getWikiPageFile(table)
    }

    @Override
    String getCharset() {
        'utf-8'
    }

    @Override
    public String getCode() {
        """\
+++
    title="${table.getClass().getName().substring(table.getClass().getPackage().getName().length() + 1)}"
    sourceFileLink="${table.sourceFileLink}"
    packages=["${table.getClass().getPackage().getName().replace('.', '/')}"]
    schemas=["${table.getSchema()}"]    
    types=["Table"]
    ${table.pattern ? "patterns=[\"${table.pattern.class.simpleName}\"]" : ''}
    ${table.physicalSchema ? "physical-schemas=[\"${table.getPhysicalSchema()}\"]" : ''}
    ${!table.tags.isEmpty() ? "tags=[${table.tags.collect({ "\"${it}\"" }).join(',')}]" : ''}    
+++
${WikiHelper.renderElementDescription(table)}


${WikiHelper.renderDataModel(table)}

<H2>Columns</H2>
<table>
<tr><th>Name</th><th>Data type</th><th>Description</th></tr>
${table.columns.collect({ """<tr><td class="nowrap">$it.name</td><td class="nowrap">$it.dataType</td><td>${WikiHelper.renderColumnDescription(it.description)}</td></tr>\n""" }).join()}
</table>

${WikiHelper.renderDataLineage(table)}
"""
    }

}
