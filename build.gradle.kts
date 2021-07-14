plugins {
    id("idea")
    id("groovy")
    id("java-library-distribution")
    id("java-test-fixtures")
    id("maven-publish")
    id("signing")
}

group = "com.github.kaja78"
version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    val groovyVersion: String by project
    val junitVersion: String by project
    val slf4jVersion: String by project

    implementation("org.codehaus.groovy:groovy:${groovyVersion}")
    implementation("org.codehaus.groovy:groovy-json:${groovyVersion}")
    implementation("org.codehaus.groovy:groovy-sql:${groovyVersion}")
    implementation("org.slf4j:slf4j-api:${slf4jVersion}")

    testFixturesImplementation("junit:junit:${junitVersion}")
    testFixturesImplementation("org.codehaus.groovy:groovy:${groovyVersion}")
    testFixturesImplementation("org.codehaus.groovy:groovy-sql:${groovyVersion}")
    testFixturesImplementation("org.slf4j:slf4j-api:${slf4jVersion}")

    testImplementation("org.codehaus.groovy:groovy-test:${groovyVersion}")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.hsqldb:hsqldb:2.3.2")

    testRuntimeOnly("org.slf4j:slf4j-simple:${slf4jVersion}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

val dislSigning: String by project
if (dislSigning == "enabled") {
    signing {
        sign(configurations.getByName("archives"))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {

                name.set("disl-mit")
                description.set("disl-mit = data integration specific language published under MIT license. Goal of this project is to implement groovy based domain specific language supporting modelling of data integration projects. Disl will support data modeling and data mapping including support for MDA transformations and unit testing.")
                url.set("https://github.com/kaja78/disl")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        name.set("Karel Hübl")
                        email.set("karel.huebl@gmail.com")
                        organization.set("Karel Hübl")
                        organizationUrl.set("https://github.com/kaja78")
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/kaja78/disl-mit/issues")
                }
                scm {
                    connection.set("scm:git:git@github.com:kaja78/disl-mit.git")
                    developerConnection.set("scm:git:git@github.com:kaja78/disl-mit.git")
                    url.set("git:git@github.com:kaja78/disl-mit.git")
                }
            }
        }
    }
}