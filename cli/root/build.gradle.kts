// FILE IS GENERATED AUTOMATICALLY BY kbre. DON'T EDIT MANUALLY.
%IMPORTS%

plugins {
    kotlin("multiplatform") version libs.versions.kotlin
    alias(libs.plugins.sonar)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.task.tree)
%PLUGINS%
}

group = "%GROUP%"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    commonMainImplementation(libs.clikt)
    commonTestImplementation(kotlin("test"))
%DEPS%
}

kotlin {
    linuxX64().binaries.executable { entryPoint = "name.stepin.main" }
    linuxArm64().binaries.executable { entryPoint = "name.stepin.main" }
    macosX64().binaries.executable { entryPoint = "name.stepin.main" }
    macosArm64().binaries.executable { entryPoint = "name.stepin.main" }

    applyDefaultHierarchyTemplate()
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

%BODY%