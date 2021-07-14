package org.disl.util.wiki

import groovy.json.JsonBuilder
import org.disl.meta.Perspective
import org.disl.meta.Table
import org.disl.pattern.FileOutputStep
import org.disl.util.wiki.visjs.DataModelNetwork

/**
 * Create file containg perspective data model in JSON format for vis.js network visualisation.
 */
class PerspectiveDataModelStep extends FileOutputStep {

    Perspective perspective

    @Override
    File getFile() {
        return WikiHelper.getDataModelDataFile(perspective.class.name)
    }

    @Override
    String getCode() {
        DataModelNetwork dataModelNetwork = new DataModelNetwork()
        perspective.elements.findAll({ it instanceof Table }).each {
            dataModelNetwork.add(it)
        }
        new JsonBuilder(dataModelNetwork).toPrettyString()
    }


}
