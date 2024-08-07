    implementation("org.postgresql:postgresql")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("org.springframework:spring-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:r2dbc")
    jooqCodegen(libs.postgresql)