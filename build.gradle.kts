import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version Libs.kotlinVersion
    id("org.jetbrains.dokka") version Libs.dokkaVersion
    id("maven-publish")
    id("java-library")

    id("com.github.ben-manes.versions") version Libs.versions
    id("org.jmailen.kotlinter") version Libs.kotlinter
    id("com.adarshr.test-logger") version Libs.testLogger
    id("net.researchgate.release") version Libs.release
    id("com.jfrog.bintray") version Libs.bintray
}

repositories {
    jcenter()
    mavenCentral()
}

publishing {
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

allprojects {
    group = "io.monkeypatch.kaval"

    repositories {
        mavenCentral()
        jcenter()
        google()
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}

release {
    preTagCommitMessage = ":bookmark: release new version: "
    tagCommitMessage = ":bookmark: release new version: "
    newVersionCommitMessage = ":bookmark: prepare new version: "
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("metadata", "jvm", "js")
    publish = true //[Default: false] Whether version should be auto published after an upload
    pkg.apply {
        repo = "kaval"
        name = "kaval"
        userOrg = "monkeypatchio"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/MonkeyPatchIo/kaval"
        websiteUrl = "https://github.com/MonkeyPatchIo/kaval"
        issueTrackerUrl = "https://github.com/MonkeyPatchIo/kaval/issues"

        githubRepo = "monkeypatchio/kaval"
        version.apply {
            name = "${project.version}"
            desc = "A POJO validation DSL in Kotlin"
//            released = "${java.util.Date()}"
            gpg.sign = true //Determines whether to GPG sign the files. The default is false
//            mavenCentralSync.apply {
//                sync = true //[Default: true] Determines whether to sync the version to Maven Central.
//                user = "token" //OSS user token: mandatory
//                password = "pass" //OSS user password: mandatory
//                close = "1" //Optional property. By default the staging repository is closed and artifacts are released to Maven Central. You can optionally turn this behaviour off (by puting 0 as value) and release the version manually.
//            }
        }
    }
}
