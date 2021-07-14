package org.disl.workflow

import groovy.io.FileType
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.lang.reflect.Modifier
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.regex.Pattern

/**
 * Helper for finding classes based on search conditions.
 * */
@CompileStatic
@Slf4j
abstract class ClassFinder {

    URL sourceURL
    String rootDir

    protected String getClassName(String fileName) {
        return fileName.substring(0, fileName.length() - 6).replace('/', '.').replace('\\', '.')
    }

    @Override
    public String toString() {
        "${getClass().getName()}: $sourceURL"
    }


    public static ClassFinder createClassFinder(String rootPackage) {
        List<ClassFinder> finders = []
        String rootDir = rootPackage.replace('.', '/')
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader()
        classLoader.getResources(rootDir).each {
            URL url = (URL) it

            switch (url.getProtocol()) {
                case 'jar':
                    finders.add new JarFinder(rootDir: rootDir, sourceURL: url)
                    break
                case 'file':
                    finders.add new FileSystemFinder(rootDir: rootDir, sourceURL: url)
                    break
                default:
                    throw new RuntimeException("Unsupported URL protocol for resource $url");
            }
        }
        return new CompositeFinder(finders: finders, rootDir: rootDir)
    }

    public <T> List<Class<T>> findNonAbstractTypes(Class<T> assignableType) {
        findNonAbstractTypes(rootDir, assignableType)
    }

    public <T> List<Class<T>> findNonAbstractTypes(String rootDir, Class<T> assignableType) {
        findTypes(assignableType, rootDir, { assignableType.isAssignableFrom(((Class) it)) && !Modifier.isAbstract(((Class) it).getModifiers()) })
    }

    public <T> List<Class<T>> findTypes(Class<T> assignableType, Closure classFilter) {
        return findTypes(assignableType, rootDir, classFilter);
    }

    public abstract Collection<String> listClassNames(String rootPackage)

    public abstract <T> List<Class<T>> findTypes(Class<T> assignableType, String rootPackage, Closure classFilter);

    static class FileSystemFinder extends ClassFinder {

        public <T> List<Class<T>> findTypes(Class<T> assignableType, String rootDir, Closure classFilter) {
            listClassNames(rootDir).collect({
                Class<T> type = (Class<T>) Class.forName(it)
            }).findAll(classFilter).toList()
        }

        public Collection<String> listClassNames(String rootPackage) {
            File traverseDir = new File(sourceURL.toURI())
            Pattern filterClassFiles = ~/.*\.class$/
            List<String> classNames = []
            traverseDir.traverse((Map) [type: FileType.FILES, nameFilter: filterClassFiles]) {
                String classFile = rootDir + '/' + it.absolutePath.substring(traverseDir.absolutePath.length() + 1)
                classNames.add(getClassName(classFile))
            }
            return classNames

        }

    }

    static class JarFinder extends ClassFinder {

        JarFile getJarFile() {
            ((JarURLConnection) sourceURL.openConnection()).getJarFile()
        }

        public <T> List<Class<T>> findTypes(Class<T> assignableType, String rootPackage, Closure classFilter) {
            return listClassNames(rootPackage).collect({ Class.forName(it) }).findAll(classFilter).toList()
        }


        @Override
        Collection<String> listClassNames(String rootPackage) {
            String rootDir = rootPackage.replace('.', '/') + '/'
            JarFile jarFile = getJarFile()
            Collection<JarEntry> entries = (Collection<JarEntry>) getJarFile().entries().findAll()
            List<String> classNames = []
            return entries.findAll({ (it.getName().startsWith(rootDir) && it.getName().endsWith('.class')) }).collect({ getClassName(it.getName()) })
        }
    }

    static class CompositeFinder extends ClassFinder {

        List<ClassFinder> finders

        public <T> List<Class<T>> findTypes(Class<T> assignableType, String rootPackage, Closure classFilter) {
            List<Class<T>> types = []
            finders.each {
                types.addAll(it.findTypes(assignableType, rootPackage, classFilter))
            }
            return types
        }

        @Override
        Collection<String> listClassNames(String rootPackage) {
            List<String> classNames = []
            finders.each {
                classNames.addAll(it.listClassNames(rootPackage))
            }
            return classNames.unique()
        }

        @Override
        public String toString() {
            "${getClass().getName()}: ${finders.join(',\n')}"
        }

    }
}
