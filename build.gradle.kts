plugins {
    kotlin("jvm") version "2.1.10"
    `maven-publish`
}

group = "me.blueb"
version = "2025.6.1.0-SNAPSHOT"

description = "Simple Kotlin utility for parsing a Signature header string into something more usable, validating HTTP signatures, and more."

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.10.2")
    implementation("org.jetbrains.kotlinx", "kotlinx-datetime-jvm", "0.6.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.blueb"
            artifactId = "httpSignatureUtility"
            version = project.version.toString()

            description = project.description

            from(components["java"])
        }
    }
}