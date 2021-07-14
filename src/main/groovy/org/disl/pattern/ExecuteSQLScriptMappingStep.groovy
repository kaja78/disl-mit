package org.disl.pattern

import groovy.sql.Sql
import org.disl.meta.Context
import org.disl.meta.Mapping

@groovy.transform.CompileStatic
abstract class ExecuteSQLScriptMappingStep<M extends Mapping> extends ExecuteSQLScriptStep {

    MappingPattern getPattern() {
        (MappingPattern) super.getPattern()
    }

    M getMapping() {
        getPattern().getMapping()
    }

    @Override
    public Sql getSql() {
        return Context.getSql(getMapping().getSchema());
    }

    public static ExecuteSQLScriptMappingStep create(String name, String code) {
        ExecuteSQLScriptMappingStep step = new ExecuteSQLScriptMappingStep() {
            String getCode() {
                code
            }

            Sql getSql() {
                Context.getSql(getPattern().getMapping().getSchema())
            }
        }
        step.setName(name)
        return step
    }


}
