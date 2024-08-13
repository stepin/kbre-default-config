// https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin
jib {
    from {
        image = "eclipse-temurin:21-jre"
    }
    to {
        image = "stepin/%ARTIFACT%"
        tags = setOf("$version")
    }
    container {
        labels.set(
            mapOf(
                "org.opencontainers.image.title" to "%ARTIFACT%",
                "org.opencontainers.image.version" to "$version",
            ),
        )
        creationTime.set("USE_CURRENT_TIMESTAMP")
        ports = listOf("8080", "8081")
    }
}
