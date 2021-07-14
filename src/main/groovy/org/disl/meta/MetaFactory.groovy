package org.disl.meta


import groovy.transform.CompileStatic
import org.disl.workflow.ClassFinder

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

/**
 * Factory for DISL model objects.
 * */
@CompileStatic
class MetaFactory {
    static <T> T create(Class<T> type, Closure initClosure = null) {
        try {
            createInstance(type, initClosure)
        } catch (Throwable e) {
            throw new RuntimeException("Unable to create instance of class ${type.getName()}", e);
        }
    }

    private static <T> T createInstance(Class<T> type, Closure initClosure) {
        T instance = newInstance(type)
        initInstance(instance, initClosure)
        return instance
    }

    public static <T> T newInstance(Class<T> type) {
        if (Modifier.isAbstract(type.getModifiers())) {
            throw new RuntimeException("Unable to instantiate abstract type $type.name.")
        }
        Constructor<T> constructor = type.getDeclaredConstructor(new Class[0])
        constructor.setAccessible(true)
        return constructor.newInstance(new Object[0])
    }

    private static void initInstance(Object instance, Closure initClosure = null) {
        if (initClosure) {
            initClosure.call(instance)
        }
        if (instance instanceof Initializable) {
            instance.init();
        }
    }


    /**
     * Creates instances for all found classes in given rootPackage (including subpackages) which are assignable to assignableType.
     * Only classes located in the same class path element (jar file or directory) as the sourceClass will be found and created!
     *
     * Example:
     * //Generate all tables defined in disl model for your data warehouse.
     * MetaFactory.createAll(com.yourDw.AbstractDwTable,"com.yourDw",AbstractDwTable).each({it.generate})
     * */
    static <T> List<T> createAll(String rootPackage, Class<T> assignableType) {
        List<Class<T>> typesToCreate = findTypes(rootPackage, assignableType)
        if (typesToCreate.size() == 0) {
            throw new RuntimeException('No classes found!')
        }
        typesToCreate.collect { create(it) }
    }

    /**
     * Returns classes in given rootPackage (including subpackages) which are assignable to assignableType.
     * */
    static <T> List<Class<T>> findTypes(String rootPackage, Class<T> assignableType) {
        ClassFinder.createClassFinder(rootPackage).findNonAbstractTypes(rootPackage, assignableType).unique()
    }


}
