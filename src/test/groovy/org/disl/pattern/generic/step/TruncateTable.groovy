package org.disl.pattern.generic.step


import org.disl.pattern.ExecuteSQLScriptMappingStep

class TruncateTable extends ExecuteSQLScriptMappingStep {
    String getCode() {
        "TRUNCATE TABLE ${getMapping().target.name}"
    }
}