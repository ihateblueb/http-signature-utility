plugins {
    kotlin("jvm") version "2.2.0"

    id("org.jetbrains.dokka") version "2.0.0"

    `java-library`
    `maven-publish`
}

group = "site.remlit.blueb"
version = "2025.7.2.9-SNAPSHOT"

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

val baseName = "http-signature-utility"

tasks.jar {
    archiveBaseName = baseName
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveBaseName = baseName
    archiveClassifier = "sources"
    from(sourceSets.main.get().allSource)
}

val dokkaJavadocZip by tasks.creating(Zip::class) {
    archiveBaseName = baseName
    archiveClassifier = "javadoc"
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
}

val dokkaHtmlZip by tasks.creating(Zip::class) {
    archiveBaseName = baseName
    archiveClassifier = "dokka"
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.map { it.outputDirectory })
}

artifacts {
    add("archives", sourcesJar)
    add("archives", dokkaJavadocZip)
    add("archives", dokkaHtmlZip)
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
            artifactId = "http-signature-utility"
            version = project.version.toString()

            from(components["java"])

            artifact(sourcesJar)
            artifact(dokkaJavadocZip)
            artifact(dokkaHtmlZip)

            pom {
                name = "http-signature-utility"
                description = project.description
                url = "https://github.com/ihateblueb/http-signature-utility"

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
                    connection = "scm:git:git://github.com/ihateblueb/http-signature-utility.git"
                    developerConnection = "scm:git:ssh://github.com/ihateblueb/http-signature-utility.git"
                    url = "https://github.com/ihateblueb/http-signature-utility"
                }
            }
        }
    }
}