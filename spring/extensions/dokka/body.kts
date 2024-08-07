tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            // used as project name in the header
            moduleName.set("%NAME%")

            // contains descriptions for the module and the packages
            // more info: https://kotlinlang.org/docs/dokka-module-and-package-docs.html
            includes.from("packages.md")

            // adds source links that lead to this repository, allowing readers
            // to easily find source code for inspected declarations
            val repo =
                "%REPO%"
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(URI(repo).toURL())
                remoteLineSuffix.set("#L")
            }
        }
    }
}
