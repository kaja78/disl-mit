package org.disl.pattern.generic.step;

import org.disl.pattern.ExecuteSQLScriptTableStep;

class DropTable extends ExecuteSQLScriptTableStep {

    String getCode() {
        """DROP TABLE "${table.physicalSchema}"."${table.name}";"""
    }

    DropTable() {
        ignoreErrors = true
    }
}