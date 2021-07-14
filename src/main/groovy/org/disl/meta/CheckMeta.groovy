package org.disl.meta

import groovy.transform.CompileStatic

@CompileStatic
class CheckMeta {
    /**
     * Check constraint name.
     * */
    String name

    /**
     * Check condition.
     * */
    String check

    public static initCheckConstraints(Table table) {
        CheckConstraints checkConstraints = table.getClass().getAnnotation(CheckConstraints)
        if (checkConstraints) {
            checkConstraints.value().each {
                CheckMeta.initCheck(table, (Check) it)
            }
        }

    }

    public static initCheck(Table table, Check check) {
        table.checkConstraints.add(new CheckMeta(name: check.name(), check: check.value()))
    }

}
