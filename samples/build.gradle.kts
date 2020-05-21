plugins {
    kotlin("jvm")
    id("org.jmailen.kotlinter")
    id("com.adarshr.test-logger")
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // Kaval
    implementation(project(":kaval-core"))
    implementation(project(":kaval-reflect"))
    implementation(project(":kaval-arrow"))

    // Arrow
    implementation(Libs.Arrow.core)

    // Test
    testImplementation(project(":kaval-kotest"))
    testImplementation(Libs.Kotest.assertionsJvm)
    testImplementation(Libs.Kotest.runnerJunit5)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
