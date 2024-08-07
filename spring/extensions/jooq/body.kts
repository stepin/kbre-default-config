jooq {
    configurations {
        create("main") {
            configuration {
                logging = Logging.WARN
                jdbc {
                    driver = "org.postgresql.Driver"
                    url = jdbcCfg["url"]
                    user = jdbcCfg["username"]
                    password = jdbcCfg["password"]
                }
                generator {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target {
                        packageName = "name.stepin.db.sql"
                        directory = "build/generated-src/jooq/main"
                    }
                }
            }
        }
    }
}
tasks.named("jooqCodegen") {
    dependsOn(tasks.named("flywayMigrate"))
}
tasks.named("compileKotlin") {
    dependsOn(tasks.named("jooqCodegen"))
}
