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
