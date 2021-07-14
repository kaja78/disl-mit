package org.disl.pattern.generic

import org.disl.pattern.TablePattern
import org.disl.pattern.generic.step.CreateTable
import org.disl.pattern.generic.step.DropTable

/**
 * Generic create or replace table deployment pattern.
 * */
class CreateOrReplaceTablePattern extends TablePattern {

    @Override
    public void init() {
        add([DropTable, CreateTable])
    }
}