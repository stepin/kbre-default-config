// FILE IS GENERATED AUTOMATICALLY BY kbre. DON'T EDIT MANUALLY.
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.yaml.snakeyaml.Yaml
%IMPORTS%

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("org.springframework.boot") version libs.versions.springBoot
    id("io.spring.dependency-management") version libs.versions.springDependencyManagement
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.spring") version libs.versions.kotlin
    alias(libs.plugins.kover)
    alias(libs.plugins.sonar)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.axion.release)
    alias(libs.plugins.task.tree)
%PLUGINS%
}

group = "%GROUP%"
version = scmVersion.version
java.sourceCompatibility = JavaVersion.VERSION_21

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

buildscript {
    dependencies {
        classpath("org.yaml:snakeyaml:2.0")
        classpath(libs.flyway.postgresql)
    }
}

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = libs.versions.testcontainers.get()

dependencies {
    // basic deps
    implementation("org.springframework.boot:spring-boot-starter-validation")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Kotlin deps
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation(libs.log4j.kotlin)

    // HTTP server
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // External information: health, metrics, apis
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation(libs.springdoc.webflux.ui)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation(libs.mockk.spring)
    testImplementation(libs.archunit)
    testImplementation(libs.wiremock)
%DEPS%
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

kover {
    useJacoco.set(true)
}
tasks.test {
    finalizedBy(tasks.koverXmlReport) // report is always generated after tests run
}
tasks.koverXmlReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

sonar {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/kover/xml/report.xml")
        property("sonar.host.url", "%SONAR_HOST_URL%")
        property("sonar.projectKey", "%SONAR_PROJECT_KEY%")
        property("sonar.projectName", "%SONAR_PROJECT_NAME%")
        property("sonar.token", "%SONAR_TOKEN%")
    }
}
tasks.sonar {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

ktlint {
    filter {
        exclude { element ->
            element.file.path.contains("generated-src")
        }
    }
}

val cfg: Map<String, Map<String, Map<String, String>>> =
    Yaml().load(
        File("src/main/resources/application.yml").bufferedReader(),
    )
val jdbcCfg: Map<String, String> = cfg["spring"]?.get("datasource") ?: emptyMap()

%BODY%
