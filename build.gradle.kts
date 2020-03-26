import net.researchgate.release.GitAdapter
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version Libs.kotlinVersion
    id("org.jetbrains.dokka") version Libs.dokkaVersion
    id("java-library")
    id("base")

    id("com.github.ben-manes.versions") version Libs.versions
    id("org.jmailen.kotlinter") version Libs.kotlinter
    id("com.adarshr.test-logger") version Libs.testLogger
    id("net.researchgate.release") version Libs.release
}

repositories {
    jcenter()
    mavenCentral()
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

    with (propertyMissing("git") as GitAdapter.GitConfig) {
        pushToRemote = false
    }
}
