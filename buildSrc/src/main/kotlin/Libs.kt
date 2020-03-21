object Libs {

    // Plugins
    const val kotlinVersion = "1.3.70"
    const val dokkaVersion = "0.10.1"
    const val testLogger = "2.0.0"
    const val kotlinter = "2.3.2"
    const val release = "2.8.1"
    const val versions = "0.28.0"
    const val bintray = "1.8.4"
    const val orchid = "0.19.0"

    object Kotest {
        private const val version = "3.4.2"
        const val core = "io.kotlintest:kotlintest-core:$version"
        const val assertions = "io.kotlintest:kotlintest-assertions:$version"
        const val runnerJunit5 = "io.kotlintest:kotlintest-runner-junit5:$version"
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
