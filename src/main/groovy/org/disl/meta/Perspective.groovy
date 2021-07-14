package org.disl.meta

import org.disl.pattern.Executable
import org.disl.pattern.ExecutionInfo
import org.disl.pattern.Pattern

/**
 * Set of related DISL model objects documented by @Description.
 * Intent of this object is to describe part of DISL model and enable generation of documentation.
 * Following class level annotations are supported:
 * <li>@Name - name of perspective.</li>
 * <li>@Elements - set of DISL model classes.</li>
 * <li>@Description </li>
 * */
abstract class Perspective extends Base implements Initializable, Executable {

    Set<Base> elements = new TreeSet<>(new ClassComparator())

    abstract Pattern getPattern()

    void init() {
        super.init()
        initElements()
        initName()
        initTags()
    }

    @Override
    void execute() {
        getPattern().execute()
    }

    @Override
    void simulate() {
        getPattern().simulate()
    }

    @Override
    ExecutionInfo getExecutionInfo() {
        getPattern().getExecutionInfo()
    }

    /**
     * Init elements by annotation value.
     * */
    void initElements() {
        Elements elements = this.getClass().getAnnotation(Elements)
        if (elements) {
            elements.value().each {
                this.elements.add MetaFactory.create(it)
            }
        }
    }

    static class ClassComparator implements Comparator {
        @Override
        int compare(Object o1, Object o2) {
            return o1.class.name.compareTo(o2.class.name)
        }
    }


}
