import com.collibra.plugins.java.CollibraJavaPlugin

plugins {
    id("groovy")
    //Collibra Gradle Java plugin
    // https://bitbucket.collibra.com/projects/DT/repos/gradle-java-plugin/browse
    id("com.collibra.plugins.java") version "10.3.0"
    //Collibra Cloud SDK) versioning
    // https://github.com/collibra/collibra-sdk/blob/master/cloud/README.md
    // if not applied, project.version is not set -> jars don"t have) versions
    id("collibra-cloud-sdk-versioning") version "2.5.4"
    id("maven-publish")
    id("java-test-fixtures")
}

tasks.wrapper {
    distributionUrl = CollibraJavaPlugin.GRADLE_DISTRIBUTION_URL
    distributionSha256Sum = CollibraJavaPlugin.GRADLE_DISTRIBUTION_SHA256SUM
}

group = "com.collibra.outbound"

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
collibraJavaConfig {
    checkstyleEnabled.set(project.hasProperty("sonarqube.token"))
    sonarqubeToken.set(project.findProperty("sonarqube.token") as String?)
}

val nexusUserName: String by project
val nexusPassword: String by project
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            credentials {
                username = nexusUserName
                password = nexusPassword
            }
            url = uri("https://nexus.collibra.com/repository/collibra-ci")
        }
    }
}