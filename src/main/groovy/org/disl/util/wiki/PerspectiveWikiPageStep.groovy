package org.disl.util.wiki

import org.disl.meta.Perspective
import org.disl.pattern.FileOutputStep

/**
 * Generate markdown wiki page file with Perspective documentation.
 */
class PerspectiveWikiPageStep extends FileOutputStep {
    Perspective perspective

    @Override
    String getCharset() {
        'utf-8'
    }

    @Override
    File getFile() {
        return WikiHelper.getWikiPageFile(perspective)
    }

    @Override
    String getCode() {
        """\
+++
    title= "${perspective.name}"
    sourceFileLink="${perspective.sourceFileLink}"
    packages=["${perspective.getClass().getPackage().getName().replace('.', '/')}"]
    types=["Perspective"]
+++
${WikiHelper.renderElementDescription(perspective)}


${WikiHelper.renderDataModel(perspective)}

"""
    }


}
