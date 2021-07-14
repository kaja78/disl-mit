package org.disl.util.wiki

import groovy.json.JsonBuilder
import org.disl.meta.Mapping
import org.disl.pattern.FileOutputStep
import org.disl.util.wiki.visjs.MappingLineageNetwork

/**
 * Create file containg mapping lineage in JSON format for vis.js network visualisation.
 */
class MappingLineageDataStep extends FileOutputStep {

    Mapping mapping

    @Override
    File getFile() {
        return WikiHelper.getLineageDataFile(mapping.class.name)
    }

    @Override
    String getCode() {
        new JsonBuilder(new MappingLineageNetwork(mapping)).toPrettyString()
    }
}
