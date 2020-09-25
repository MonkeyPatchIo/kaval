object Libs {

    object Plugins {
        const val kotlinVersion = "1.4.10"
        const val dokkaVersion = "1.4.10"
        const val testLogger = "2.1.0"
        const val kotlinter = "3.2.0"
        const val release = "2.8.1"
        const val versions = "0.33.0"
        const val orchid = "0.21.1"
    }

    object Coroutine {
        private const val version = "1.3.9"
        const val jvmCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        const val jsCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$version"
    }

    object Arrow {
        private const val version = "0.11.0"
        const val core = "io.arrow-kt:arrow-core:$version"
        const val syntax = "io.arrow-kt:arrow-syntax:$version"
        const val optics = "io.arrow-kt:arrow-optics:$version"
        const val fx = "io.arrow-kt:arrow-fx:$version"
        const val meta = "io.arrow-kt:arrow-meta:$version"
    }

    object Kotest {
        private const val version = "4.2.5"
        const val assertionsMpp = "io.kotest:kotest-assertions-core:$version"
        const val assertionsJvm = "io.kotest:kotest-assertions-core-jvm:$version"
        const val assertionsArrow = "io.kotest:kotest-assertions-arrow:$version"
        const val propertyJvm = "io.kotest:kotest-property-jvm:$version"
        const val runnerJunit5 = "io.kotest:kotest-runner-junit5-jvm:$version"
    }

    object Orchid {
        private const val version = Libs.Plugins.orchid
        const val core = "io.github.javaeden.orchid:OrchidCore:$version"

        //        const val search = "io.github.javaeden.orchid:OrchidSearch:version"
        const val docs = "io.github.javaeden.orchid:OrchidDocs:$version"
        const val admin = "io.github.javaeden.orchid:OrchidPluginDocs:$version"
        const val sourceDocs = "io.github.javaeden.orchid:OrchidSourceDoc:$version"
        const val github = "io.github.javaeden.orchid:OrchidGithub:$version"
    }
}
