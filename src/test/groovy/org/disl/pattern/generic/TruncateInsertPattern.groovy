package org.disl.pattern.generic


import org.disl.pattern.MappingPattern
import org.disl.pattern.generic.step.InsertIntoTable
import org.disl.pattern.generic.step.TruncateTable

class TruncateInsertPattern extends MappingPattern {


    @Override
    public void init() {
        add([TruncateTable, InsertIntoTable])
    }


}
