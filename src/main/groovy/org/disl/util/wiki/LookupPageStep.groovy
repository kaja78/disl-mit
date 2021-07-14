package org.disl.util.wiki

import org.disl.meta.Lookup
import org.disl.pattern.FileOutputStep

/**
 * Generate markdown wiki page file with Lookup documentation.
 */
class LookupPageStep extends FileOutputStep {
    Lookup lookup

    @Override
    File getFile() {
        return WikiHelper.getWikiPageFile(lookup)
    }

    @Override
    String getCharset() {
        'utf-8'
    }

    @Override
    public String getCode() {
        """\
+++
    title="${lookup.name}"
    sourceFileLink="${lookup.sourceFileLink}"
    packages=["${lookup.getClass().getPackage().getName().replace('.', '/')}"]
    schemas=["${lookup.getSchema()}"]
    types=["Lookup"]
+++


${WikiHelper.renderElementDescription(lookup)}

<H2>Columns</H2>
<table>
<tr><th>Name</th><th>Data type</th><th>Decription</th></tr>
${lookup.columns.collect({ """<tr><td class="nowrap">$it.name</td><td class="nowrap">$it.dataType</td><td>${WikiHelper.renderColumnDescription(it.description)}</td></tr>\n""" }).join()}
</table>

<H2>Values</H2>
<table>
<tr>${lookup.columns.collect({ "<th>$it.name</th>" }).join()}</tr>
${lookup.records.collect({ "<tr>${it.collect({ """<td><pre><code>$it</pre></code></td>""" }).join()}</tr>" }).join('\n')}

</table>

"""
    }

}
