plugins {
    kotlin("jvm") version "2.1.10"

    id("org.jetbrains.dokka") version "2.0.0"

    `java-library`
    `maven-publish`
}

group = "site.remlit.blueb"
version = "2025.6.4.0-SNAPSHOT"

description = "Simple Kotlin utility for parsing a Signature header string into something more usable, validating HTTP signatures, and more."

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.10.2")
    implementation("org.jetbrains.kotlinx", "kotlinx-datetime-jvm", "0.6.2")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

val baseName = "httpSignatureUtility"

tasks.jar {
    archiveBaseName = baseName
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveBaseName = baseName
    archiveClassifier = "sources"
    from(sourceSets.main.get().allSource)
}

val dokkaJavadocJar by tasks.creating(Jar::class) {
    archiveBaseName = baseName
    archiveClassifier = "javadoc"
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.map { it.outputDirectory })
}

artifacts {
    add("archives", sourcesJar)
    add("archives", dokkaJavadocJar)
}

publishing {
    repositories {
        maven {
            name = "remlitSiteMain"
            url = if (version.toString().contains("SNAPSHOT")) uri("https://repo.remlit.site/snapshots") else uri("https://repo.remlit.site/releases")

            credentials {
                username = System.getenv("REPO_ACTOR")
                password = System.getenv("REPO_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "site.remlit.blueb"
            artifactId = "httpSignatureUtility"
            version = project.version.toString()

            from(components["java"])

            artifact(sourcesJar)
            artifact(dokkaJavadocJar)

            pom {
                name = "HttpSignatureUtility"
                description = project.description
                url = "https://github.com/ihateblueb/HttpSignatureUtility"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/license/mit"
                    }
                }

                developers {
                    developer {
                        id = "ihateblueb"
                        name = "ihateblueb"
                        email = "ihateblueb@proton.me"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/ihateblueb/HttpSignatureUtility.git"
                    developerConnection = "scm:git:ssh://github.com/ihateblueb/HttpSignatureUtility.git"
                    url = "https://github.com/ihateblueb/HttpSignatureUtility"
                }
            }
        }
    }
}