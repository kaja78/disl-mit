package org.disl.meta

import groovy.transform.CompileStatic

import java.lang.reflect.Field

/**
 * Abstract parent class for DISL meta classes.
 * */
@CompileStatic
abstract class Base implements Comparable<Base>, Initializable {

    String name
    String description = ''
    Set<String> tags = new HashSet<>()

    @Override
    void init() {
        initDescription()
        initName()
        initTags()
    }

    protected void initDescription() {
        Description desc = this.getClass().getAnnotation(Description)
        if (desc) {
            setDescription(desc.value())
        }
    }

    /**
     * Init name by annotation value.
     * */
    void initName() {
        Name name = this.getClass().getAnnotation(Name)
        if (name) {
            this.name = name.value()
        }
    }

    /**
     * Init tags by annotation value.
     * */
    void initTags() {
        tags = getTags(getClass())
    }

    Set<String> getTags(Class clazz) {
        Set<String> tagValues = []
        if (clazz.getSuperclass()) {
            tagValues = getTags(clazz.getSuperclass())
        }
        if (clazz.isAnnotationPresent(Tags)) {
            tagValues.addAll(((Tags) clazz.getAnnotation(Tags)).value())
        }
        return tagValues
    }

    String getSourceFileLink() {
        sourceFileLink(this.class)
    }

    static String sourceFileLink(Class clazz) {
        "src/main/groovy/${clazz.name.replace('.', '/')}.groovy"
    }

    public String getName() {
        if (name == null) {
            return this.getClass().getSimpleName()
        }
        name
    }

    public String toString() {
        getName()
    }

    public List<Field> getFieldsByType(Class matchType) {
        getFieldsByType(this.getClass(), matchType);
    }

    public List<Field> getFieldsByType(Class type, Class matchType) {
        List<Field> fields = []
        if (type.getSuperclass() != null) {
            fields.addAll(getFieldsByType(type.getSuperclass(), matchType))
        }
        fields.addAll(type.getDeclaredFields().findAll { matchType.isAssignableFrom(it.getType()) })
        return fields
    }

    public Field getFieldByName(String name) {
        return getFieldByName(name, this.getClass())
    }

    public Field getFieldByName(String name, Class type) {
        try {
            return type.getDeclaredField(name)
        } catch (NoSuchFieldException e) {
            if (type.getSuperclass()) {
                return getFieldByName(name, type.getSuperclass())
            }
            return null
        }
    }

    public <T extends Object> List<T> getPropertyValuesByType(Class<T> type) {
        return metaClass.properties.findAll({ type.isAssignableFrom(it.type) }).collect({ (T) this.getProperty(it.name) })
    }

    public List<String> getPropertyNamesByType(Class type) {
        return metaClass.properties.findAll({ type.isAssignableFrom(it.type) }).collect({ it.name });
    }

    @Override
    public int compareTo(Base o) {
        return this.getName().compareTo(o.getName())
    }

}
