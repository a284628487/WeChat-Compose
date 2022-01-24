package com.compose.wechat

import androidx.room.Dao
import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import dagger.internal.DaggerGenerated
import org.junit.Test

class ArchTest {

    @Test
    fun layerTest() {
        val importedClasses = ClassFileImporter().importPackages("com.compose.wechat")
        val arch = layeredArchitecture()
            .layer("Root").definedBy("com.compose.wechat")
            .layer("Main").definedBy("..ui.main")
            .layer("Profile").definedBy("..ui.main.profile")
            // Profile只能被Main和Root访问
            .whereLayer("Profile").mayOnlyBeAccessedByLayers("Main", "Root")
        arch.check(importedClasses)
    }

    @Test
    fun accessTest() {
        val importedClasses = ClassFileImporter()
            .importPackages("com.compose.wechat")
            .that(object : DescribedPredicate<JavaClass>("") {
                override fun apply(input: JavaClass?): Boolean {
                    return !(input?.isAnnotatedWith(DaggerGenerated::class.java) ?: false)
                }
            }).also {
                it.forEach {
                    println("classes: ${it.name}")
                }
            }

        val rule = classes().that().areAnnotatedWith(Dao::class.java)
            .should().onlyBeAccessed().byClassesThat().haveSimpleNameEndingWith("Inject")
            .orShould().onlyBeAccessed().byClassesThat().haveSimpleNameEndingWith("Repo")
        rule.check(importedClasses)

        // https://www.archunit.org/userguide/html/000_Index.html#_class_dependency_checks
        // val rule2 = classes().that().haveNameMatching(".*Bar")
        //    .should().onlyHaveDependentClassesThat().haveSimpleName("Repo")
        // rule2.check(importedClasses)
        // Repo能依赖Bar，其它类不能依赖Bar。
    }

    // this test will fail, because profile depends on moments
    @Test
    fun packageDependTest() {
        val importedClasses = ClassFileImporter()
            .importPackages("com.compose.wechat")
            .that(object : DescribedPredicate<JavaClass>("") {
                override fun apply(input: JavaClass?): Boolean {
                    return !(input?.isAnnotatedWith(DaggerGenerated::class.java) ?: false)
                }
            })
        // profile不能访问moments
        // https://www.archunit.org/userguide/html/000_Index.html#_package_dependency_checks
        val rule = noClasses().that().resideInAPackage("com.compose.wechat.ui.main.profile")
            .should().dependOnClassesThat().resideInAPackage("com.compose.wechat.ui.main.moments")
        rule.check(importedClasses)
    }

    @Test
    fun classPackagesLimitTest() {
        // https://www.archunit.org/userguide/html/000_Index.html#_class_and_package_containment_checks
        // classes().that().haveSimpleNameStartingWith("Foo")
        //    .should().resideInAPackage("com.foo")
        // com.foo.Foo [y];
        // com.abc.FooBar [n];
    }

    @Test
    fun classNameCheck() {
        // https://www.archunit.org/userguide/html/000_Index.html#_inheritance_checks
        // classes().that().implement(Connection.class)
        //    .should().haveSimpleNameEndingWith("Connection")
    }

    @Test
    fun annotationCheck() {
        // https://www.archunit.org/userguide/html/000_Index.html#_annotation_checks
        // classes().that().areAssignableTo(EntityManager.class)
        //    .should().onlyHaveDependentClassesThat().resideInAnyPackage("..persistence..")
        // EntityManager 只依赖于..persistence..
    }

}