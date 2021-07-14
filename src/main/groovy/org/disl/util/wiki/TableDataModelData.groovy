package org.disl.util.wiki

import groovy.json.JsonBuilder
import org.disl.meta.Table
import org.disl.pattern.FileOutputStep
import org.disl.util.wiki.visjs.DataModelNetwork

/**
 * Create file containg table data model in JSON format for vis.js network visualisation.
 */
class TableDataModelData extends FileOutputStep {

    Table table

    @Override
    File getFile() {
        return WikiHelper.getDataModelDataFile(table.class.name)
    }

    @Override
    String getCode() {
        new JsonBuilder(new DataModelNetwork(table)).toPrettyString()
    }

    @Override
    int executeInternal() {
        //generate data model file only when foreign keys exist.
        if (table.foreignKeys.size() > 0) {
            return super.executeInternal()
        }
        return 0
    }
}
