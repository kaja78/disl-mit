package org.disl.util.doc.step

import org.disl.meta.Mapping
import org.disl.meta.TableMapping
import org.disl.util.doc.IDocumentationStep.IMappingDocumentationStep
import org.disl.util.doc.LineageRenderer

class DefaultMappingDocumentationStep extends AbstractDocStep implements IMappingDocumentationStep {

    Mapping mapping

    public String getFileName() {
        "model/${mapping.class.name}.html"
    }

    @Override
    public String getCode() {
        """\
<html>
<head>
	<link rel="stylesheet" type="text/css" href="../disldoc.css">
</head>
<body>
${mapping.class.name}
<H1>Mapping ${mapping.name}</H1>
${mapping.pattern ? "Pattern: ${mapping.pattern.class.simpleName}" : ''} 
${mapping.description}

${LineageRenderer.renderContainer(mapping.class.name)}

${targetSection}

<H2>Columns</H2>
<table>
<tr><th>Name</th><th>Expression</th><th>Description</th></tr>
${mapping.columns.collect({ "<tr><td>$it.alias</td><td><code><pre>$it.expression</pre></code></td><td>$it.description</td></tr>\n" }).join()}
</table>

$sources

<H2>Filter</H2>
<div><pre><code>${mapping.filter}</code></pre></div>

<H2>SQL</H2>
<textarea>
${mapping.getSQLQuery()}
</textarea>
</body></html>
"""
    }

    String getSources() {
        """\
<H2>Sources</H2>
<table>
<tr><th>Alias</th><th>Name</th><th>Join type</th><th>Join condition</th></tr>
${mapping.sources.collect {
            "<tr><td>${it.sourceAlias}</td><td>${link(it)}</td><td>${it.join.class.simpleName}</td><td><pre><code>${it.join.condition}</code></pre></td></tr>\n"
        }.join()
        }
${mapping.setOperations.collect {
            "<tr><td>${it.source.sourceAlias}</td><td>${link(it.source)}</td><td>${it.class.simpleName}</td><td></td></tr>\n"
        }.join()}
</table>
"""
    }

    String getTargetSection() {
        if (mapping instanceof TableMapping) {
            return """\
<H2>Target:  ${link(mapping.target)}</H2>"""
        }
        return ''
    }


}
