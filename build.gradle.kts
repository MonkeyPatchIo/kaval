import net.researchgate.release.GitAdapter
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version Libs.Plugins.kotlinVersion
    id("org.jetbrains.dokka") version Libs.Plugins.dokkaVersion
    id("java-library")
    id("base")

    id("com.github.ben-manes.versions") version Libs.Plugins.versions
    id("org.jmailen.kotlinter") version Libs.Plugins.kotlinter
    id("com.adarshr.test-logger") version Libs.Plugins.testLogger
    id("net.researchgate.release") version Libs.Plugins.release
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

    with(propertyMissing("git") as GitAdapter.GitConfig) {
        pushToRemote = false
    }
}
