package org.disl.util.wiki

import groovy.json.JsonBuilder
import org.disl.meta.Table
import org.disl.pattern.FileOutputStep
import org.disl.util.wiki.visjs.MappingLineageNetwork

/**
 * Create file containg tadle data lineage in JSON format for vis.js network visualisation.
 */
class TableLineageDataStep extends FileOutputStep {

    Table table

    @Override
    File getFile() {
        return WikiHelper.getLineageDataFile(table.class.name)
    }

    @Override
    String getCode() {
        new JsonBuilder(new MappingLineageNetwork(table)).toPrettyString()
    }
}
