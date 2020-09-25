import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("java")
    id("kotlin-multiplatform")
    id("maven-publish")
    id("java-library")
    id("signing")
    id("org.jetbrains.dokka")

    id("org.jmailen.kotlinter")
    id("com.adarshr.test-logger")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api(project(":kaval-core"))
            }
        }

        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                    implementation(Libs.Arrow.core)
                }
            }
            compilations["test"].defaultSourceSet {
                dependencies {
                    api(project(":kaval-core"))
                    implementation(project(":kaval-kotest"))
                    implementation(Libs.Kotest.assertionsJvm)
                    implementation(Libs.Kotest.assertionsArrow)
                    implementation(Libs.Kotest.runnerJunit5)
                }
            }
        }
    }
}

// FIXME dokka
//dokkaHtml {
//    configure {
//        outputDirectory = "$buildDir/dokka"
//        dokkaSourceSets {
//            create("jvmMain")
//        }
//    }
//}
//dokkaJavadoc {
//    configure {
//        outputDirectory = "$buildDir/javadoc"
//        dokkaSourceSets {
//            create("jvmMain")
//        }
//    }
//}

tasks {
    named<Test>("jvmTest") {
        useJUnitPlatform()
    }
}

publishing {
    repositories {
        maven {
            name = "Sonatype_OSS"
            url = uri(
                if (version.toString().endsWith("SNAPSHOT")) "https://oss.sonatype.org/content/repositories/snapshots"
                else "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            )
            credentials {
                username = System.getenv("NEXUS_USERNAME")
                password = System.getenv("NEXUS_PASSWORD")
            }
        }
    }
}

// Publishing

// Add a Javadoc JAR to each publication as required by Maven Central
val javadocJar by tasks.creating(Jar::class) {
    val dokkaTask = tasks.getByName<DokkaTask>("dokkaJavadoc")
    from(dokkaTask.outputDirectory)
    dependsOn(dokkaTask)
    dependsOn("build")
    archiveClassifier.value("javadoc")
}

publishing {
    publications.withType<MavenPublication>().all {
        artifact(javadocJar)
    }
}

// The root publication also needs a sources JAR as it does not have one by default
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.value("sources")
}

publishing.publications.withType<MavenPublication>().getByName("kotlinMultiplatform").artifact(sourcesJar)

// Customize the POMs adding the content required by Maven Central
fun customizeForMavenCentral(pom: org.gradle.api.publish.maven.MavenPom) = pom.withXml {
    fun groovy.util.Node.add(key: String, value: String) {
        appendNode(key).setValue(value)
    }

    fun groovy.util.Node.node(key: String, content: groovy.util.Node.() -> Unit) {
        appendNode(key).also(content)
    }

    asNode().run {
        add("name", "Kaval")
        add("description", "A POJO validation DSL in Kotlin")
        add("url", "http://github.com/MonkeyPatchIo/kaval")
        node("organization") {
            add("name", "MonkeyPatch")
            add("url", "https://monkeypatch.io/")
        }
        node("issueManagement") {
            add("system", "github")
            add("url", "https://github.com/MonkeyPatchIo/kaval/issues")
        }
        node("licenses") {
            node("license") {
                add("name", "Apache License 2.0")
                add("url", "https://github.com/MonkeyPatchIo/kaval/blob/master/LICENSE")
                add("distribution", "repo")
            }
        }
        node("scm") {
            add("url", "https://github.com/MonkeyPatchIo/kaval")
            add("connection", "scm:git:git://github.com/MonkeyPatchIo/kaval.git")
            add("developerConnection", "scm:git:ssh://github.com/MonkeyPatchIo/kaval.git")
        }
        node("developers") {
            node("developer") {
                add("name", "Igor Laborie")
            }
        }
    }
}

publishing {
    publications.withType<MavenPublication>().all {
        customizeForMavenCentral(pom)
    }
}

// Sign the publications:
publishing {
    publications.withType<MavenPublication>().all {
        signing.sign(this@all)
    }
}
