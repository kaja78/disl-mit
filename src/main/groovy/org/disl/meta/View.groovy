package org.disl.meta

import groovy.transform.CompileStatic;

/**
 * Representation of view in DISL data tranformation model.
 * */
@CompileStatic
abstract class View extends Mapping {

    @Override
    public String getRefference() {
        String alias = ""
        if (sourceAlias != null) {
            alias = " ${sourceAlias}"
        }
        return "${fullName}${alias}"
    }

    public String getFullName() {
        String ownerPrefix = ""
        String owner = getPhysicalSchema()
        if (owner != null) {
            ownerPrefix = "${owner}."
        }
        "${ownerPrefix}${name}"
    }

    public String getPhysicalSchema() {
        Context.getPhysicalSchemaName(getSchema())
    }

}
