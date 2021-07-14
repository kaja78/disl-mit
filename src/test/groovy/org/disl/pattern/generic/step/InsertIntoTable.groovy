package org.disl.pattern.generic.step


import org.disl.pattern.ExecuteSQLScriptMappingStep

class InsertIntoTable extends ExecuteSQLScriptMappingStep {
    String getCode() {
        """\
INSERT INTO ${mapping.target.name}
	(
	${mapping.targetColumnNames.join(",\n\t")}				
	)
${mapping.SQLQuery}				
"""
    }
}