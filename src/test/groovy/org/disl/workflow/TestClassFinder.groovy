package org.disl.workflow

import groovy.transform.CompileStatic
import org.junit.Assert
import org.junit.Test

@CompileStatic
class TestClassFinder {

    ClassFinder fsFinder = ClassFinder.createClassFinder('org.disl.meta')
    ClassFinder jarFinder = ClassFinder.createClassFinder('org.junit.runner')

    @Test
    public void testFindTypesJar() {
        Class sourceClass = org.junit.runner.Runner
        Class typeInSubpackage = org.junit.runner.notification.Failure
        Class typeInSamePackage = org.junit.runner.Result
        Class typeInParentPackage = org.junit.ComparisonFailure
        Class typeInOtherPackage = org.junit.validator.AnnotationValidator

        Assert.assertEquals(0, jarFinder.findTypes(Object, { false }).size())

        Collection<Class<Object>> types = jarFinder.findTypes(Object, { true })
        println jarFinder.listClassNames('org.junit.runner')

        Assert.assertTrue(types.contains(sourceClass))
        Assert.assertTrue(types.contains(typeInSubpackage))
        Assert.assertTrue(types.contains(typeInSamePackage))

        Assert.assertFalse(types.contains(typeInParentPackage))
        Assert.assertFalse(types.contains(typeInOtherPackage))
    }

    @Test
    public void testFindTypesDir() {
        Class testingClass = org.disl.meta.Base
        Class typeInSamePackage = org.disl.meta.Table
        Class typeInOtherPackage = org.disl.workflow.Job


        Assert.assertEquals(0, fsFinder.findTypes(Object, { false }).size())

        Collection<Class<Object>> types = fsFinder.findTypes(Object, { true })
        println fsFinder.listClassNames('org.disl.meta')

        Assert.assertTrue(types.contains(testingClass))
        Assert.assertTrue(types.contains(typeInSamePackage))

        Assert.assertFalse(types.contains(typeInOtherPackage))
    }

    @Test
    public void testToString() {
        Assert.assertTrue fsFinder.toString().startsWith('org.disl.workflow.ClassFinder$CompositeFinder: org.disl.workflow.ClassFinder$FileSystemFinder: file:')
        Assert.assertTrue jarFinder.toString().startsWith('org.disl.workflow.ClassFinder$CompositeFinder: org.disl.workflow.ClassFinder$JarFinder: jar:file:')
    }


}
