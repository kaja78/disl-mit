package org.disl.pattern


import groovy.transform.CompileStatic
import org.disl.meta.Mapping

/**
 * Pattern for mapping.
 * */
@CompileStatic
abstract class MappingPattern<M extends Mapping> extends Pattern {
    M mapping

    final M getMapping() {
        mapping
    }

    void addSqlScriptStep(String name, String code) {
        add(ExecuteSQLScriptMappingStep.create(name, code))
    }

    @Override
    public String toString() {
        "${this.getClass().getSimpleName()}(${getMapping().getName()})"
    }
}
