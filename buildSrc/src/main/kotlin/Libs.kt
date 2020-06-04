object Libs {

    // Plugins
    const val kotlinVersion = "1.3.72"
    const val dokkaVersion = "0.10.1"
    const val testLogger = "2.0.0"
    const val kotlinter = "2.3.2"
    const val release = "2.8.1"
    const val versions = "0.28.0"
    const val orchid = "0.20.0"

    object Arrow {
        private const val version = "0.10.5"
        const val core = "io.arrow-kt:arrow-core:$version"
        const val syntax = "io.arrow-kt:arrow-syntax:$version"
        const val optics = "io.arrow-kt:arrow-optics:$version"
        const val fx = "io.arrow-kt:arrow-fx:$version"
        const val meta = "io.arrow-kt:arrow-meta:$version"
    }

    object Kotest {
        private const val version = "4.0.6"
        const val assertionsMpp = "io.kotest:kotest-assertions:$version"
        const val assertionsJvm = "io.kotest:kotest-assertions-core-jvm:$version"
        const val assertionsArrow = "io.kotest:kotest-assertions-arrow:$version"
        const val propertyJvm = "io.kotest:kotest-property-jvm:$version"
        const val runnerJunit5 = "io.kotest:kotest-runner-junit5-jvm:$version"
    }

    object Orchid {
        const val core = "io.github.javaeden.orchid:OrchidCore:$orchid"

        //        const val search = "io.github.javaeden.orchid:OrchidSearch:$orchid"
        const val docs = "io.github.javaeden.orchid:OrchidDocs:$orchid"
        const val admin = "io.github.javaeden.orchid:OrchidPluginDocs:$orchid"
        const val sourceDocs = "io.github.javaeden.orchid:OrchidSourceDoc:$orchid"
        const val github = "io.github.javaeden.orchid:OrchidGithub:$orchid"
    }
}
