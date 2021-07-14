package org.disl.pattern.generic.step;

import org.disl.pattern.ExecuteSQLScriptTableStep;

class CreateTable extends ExecuteSQLScriptTableStep {

    String getCode() {
        """\
CREATE TABLE "${table.physicalSchema}"."${table.name}" (
	${table.columnDefinitions.join(",\n\t")});

COMMENT ON TABLE ${table.physicalSchema}.${table.name} IS '${table.description}';

${table.columns.collect({ "COMMENT ON COLUMN ${table.physicalSchema}.${table.name}.${it.name} is '${it.description}';" }).join('\n')}
"""
    }
}