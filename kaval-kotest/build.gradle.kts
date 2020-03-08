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
    metadata {
        mavenPublication {
            artifactId = "${project.name}-common"
        }
    }
    jvm {
        mavenPublication {
            // make a name of jvm artifact backward-compatible, default "-jvm"
            artifactId = project.name
        }
    }
    // js disable: Waiting to switch to kotest 4.x

    // HOWTO build with all available (compatible) platform ?
    // iosX64()
    // ...

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
                    implementation(Libs.Kotest.core)
                    implementation(Libs.Kotest.assertions)
                }
                compilations["test"].defaultSourceSet {
                    dependencies {
                        implementation(Libs.Kotest.runnerJunit5)
                    }
                }
            }
        }
    }
}

tasks {
    register<Jar>("javadocJar") {
        val dokkaTask = getByName<org.jetbrains.dokka.gradle.DokkaTask>("dokka")
        from(dokkaTask.outputDirectory)
        dependsOn(dokkaTask)
        archiveClassifier.set("javadoc")
    }
    dokka {
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/dokka"

    }

    named<Test>("jvmTest") {
        useJUnitPlatform()
    }
}

publishing {
    repositories {
        maven {
            name = "Maven Central"
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

    publications {
        create<MavenPublication>("kotest") {
            from(components["java"])
        }
    }

    publications.withType<MavenPublication>() {
        //artifact(tasks["javadocJar"])
        pom {
            group = "io.monkeypatch.kaval"

            name.set("Kaval")
            description.set("A POJO validation DSL in Kotlin")
            inceptionYear.set("2020")

            licenses {
                license {
                    name.set("The Apache Software License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    name.set("Igor Laborie")
                    email.set("igor@monkeypatch.io")
                    organization.set("MonkeyPatch")
                    organizationUrl.set("https://monkeypatch.io/")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/MonkeyPatchIo/kaval")
                developerConnection.set("scm:git:ssh://github.com:MonkeyPatchIo/kaval.git")
                url.set("http://github.com/MonkeyPatchIo/kaval/tree/master")
            }
        }
    }
}

signing {
    sign(configurations.archives.get())
}
