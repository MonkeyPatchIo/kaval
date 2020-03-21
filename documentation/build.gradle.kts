import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.eden.orchidPlugin") version Libs.orchid
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "https://jitpack.io")
}

dependencies {
    orchidImplementation(Libs.Orchid.core)
    orchidRuntimeOnly(Libs.Orchid.admin)
    orchidRuntimeOnly(Libs.Orchid.docs)
    orchidRuntimeOnly(Libs.Orchid.sourceDocs)
    orchidRuntimeOnly(Libs.Orchid.github)
}

orchid {
    baseUrl = "https://MonkeyPatchIo.github.io/kaval/"
//    baseUrl = "http://localhost:8080"

    environment = if (findProperty("env") == "prod") "prod" else "debug"
    args = listOf("--experimentalSourceDoc")

    githubToken =
        if (hasProperty("github_token")) property("github_token").toString()
        else System.getenv("GITHUB_TOKEN")
}
