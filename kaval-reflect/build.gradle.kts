plugins {
    id("java")
    id("kotlin-multiplatform")
    id("maven-publish")
    id("java-library")

    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
    id("com.adarshr.test-logger")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    // js disable: This reflection API is not supported yet in JavaScript

    // HOWTO build with all available (compatible) platform ?
    // iosX64()
    // ...

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("reflect"))
                api(project(":kaval-core"))
            }
        }


        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                }
            }
            compilations["test"].defaultSourceSet {
                dependencies {
                    api(project(":kaval-core"))
                    implementation(project(":kaval-kotest"))
                    implementation(Libs.Kotest.core)
                    implementation(Libs.Kotest.runnerJunit5)
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
