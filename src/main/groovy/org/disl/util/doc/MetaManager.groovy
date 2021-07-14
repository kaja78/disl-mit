package org.disl.util.doc

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.disl.meta.*
import org.disl.workflow.ClassFinder
import org.disl.workflow.Job

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

@Slf4j
@CompileStatic
class MetaManager {

    List<Class> dislClasses = []

    Set<String> elements = new TreeSet(new ClassNameComparator())
    Map<String, String> elementDescriptionMap = new HashMap()
    Map<String, Set<String>> sourceUsage = new TreeMap(new ClassNameComparator())
    Map<String, Set<String>> targetUsage = new TreeMap(new ClassNameComparator())

    /**
     * Map holding set of model element names by package name string key.
     * */
    Map<String, Set<String>> packageContent = new TreeMap()
    Map<String, Set<String>> packageTables = new TreeMap()
    Map<String, Set<String>> packageMappings = new TreeMap()

    void addRootPackage(String rootPackage) {
        log.info("Searching for DISL classes in root package $rootPackage")
        ClassFinder cf = ClassFinder.createClassFinder(rootPackage)
        List<Class<Base>> classes = cf.findTypes(Base, {
            Class type = (Class) it


            int modifiers = type.getModifiers()
            return includeType(type) && !Modifier.isAbstract(modifiers) && ((Modifier.isStatic(modifiers) && type.isLocalClass()) || !type.isLocalClass()) && hasDefaultPublicConstructor(type)
        })
        dislClasses.addAll(classes)
        log.info("${classes.size()} DISL elements found for root package $rootPackage.")
    }

    boolean hasDefaultPublicConstructor(Class type) {
        try {
            Constructor constructor=type.getConstructor()
            return Modifier.isPublic(constructor.getModifiers())
        } catch (NoSuchMethodException e) {
            return false
        }
    }

    protected boolean includeType(Class type) {
        (MappingSource.isAssignableFrom(type) || Job.isAssignableFrom(type))
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    void process(Closure closure) {
        dislClasses.each {
            log.info("Processing ${it.name}.")
            Class<Base> modelElement = it
            String packageName = modelElement.getPackage().getName()
            try {
                Base element = MetaFactory.create(modelElement)
                closure.call(element)
                addPackageContent(packageName, element)
                addUsage(element)
                if (element instanceof MappingSource) {
                    elementDescriptionMap.put(element.class.name, element.description)
                }
            } catch (Exception e) {
                log.error(e.getMessage())
                e.printStackTrace()
            }
        }
    }

    void addPackageContent(String packageName, Base modelElement) {
        elements.add(modelElement.class.name)
        addContent(packageContent, packageName, modelElement.class.name)
        if (modelElement instanceof Mapping) {
            addContent(packageMappings, packageName, modelElement.class.name)
        }
        if (modelElement instanceof Table) {
            addContent(packageTables, packageName, modelElement.class.name)
        }
    }

    void addContent(Map<String, Set<String>> map, String packageName, String className) {
        Set<String> l = map.get(packageName)
        if (!l) {
            l = new TreeSet(new ClassNameComparator())
            map.put(packageName, l)
        }
        l.add(className)
    }

    void addUsage(Mapping mapping) {
        getSourceDependencies(mapping).each {
            addSourceUsage(it.class.name, mapping.class.name)
            addTargetUsage(mapping.class.name, it.class.name)
        }
        if (mapping instanceof TableMapping) {
            addTargetUsage(mapping.target.class.name, mapping.class.name)
            addSourceUsage(mapping.class.name, mapping.target.class.name)
        }
    }

    /**
     * Get all source dependencies this mapping depends on:
     * 1) All properties (fields & getters) of type MappingSource except for TableMapping.target.
     * 2) Mapping.sources collection.
     * 3) All set operations sources.
     * */
    static List<MappingSource> getSourceDependencies(Mapping mapping) {
        HashSet<MappingSource> mappingSources = new HashSet<>()
        //Add all properties of type MappingSource, to cover inline subqueries, which might not be included in Mapping.sources
        mappingSources.addAll(mapping.getPropertyValuesByType(MappingSource))
        //Remove target MappingSource for TableMappings
        if (mapping instanceof TableMapping) {
            mappingSources.remove(mapping.target)
        }
        //Add all Mapping.sources, to cover cases where mapping source is not declared as property, but injected in initMapping()
        mappingSources.addAll(mapping.getSources())
        mappingSources.addAll(mapping.setOperations.collect({ it.source }))
        mappingSources.toList()
    }

    void addUsage(Job job) {
        job.jobEntries.each {
            addSourceUsage(it.executable.class.name, job.class.name)
            addTargetUsage(job.class.name, it.executable.class.name)
        }
    }

    void addUsage(Perspective perspective) {
        //do nothing
    }

    void addUsage(MappingSource source) {
        //do nothing
    }

    void addSourceUsage(String className, String usedByClassName) {
        Set<String> l = sourceUsage.get(className)
        if (!l) {
            l = new TreeSet()
            sourceUsage.put(className, l)
        }
        l.add(usedByClassName)
    }

    void addTargetUsage(String className, String usedByClassName) {
        Set<String> l = targetUsage.get(className)
        if (!l) {
            l = new TreeSet()
            targetUsage.put(className, l)
        }
        l.add(usedByClassName)
    }

    public static String getElementName(String className) {
        if (className.contains('$')) {
            return className.substring(className.lastIndexOf('$') + 1)
        }
        if (className.contains('.')) {
            return className.substring(className.lastIndexOf('.') + 1)
        }
        return className
    }


    static class ClassNameComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            o1 = MetaManager.getElementName(o1) + o1
            o2 = MetaManager.getElementName(o2) + o2
            return o1.compareTo(o2)
        }
    }
}
