rootProject.name = "spring-boot-3-kotlin-multimodule-boilerplate"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val ktlintVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion

        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion

        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
    }
}

include(":app:app-api")

include(":core")

include(":domain:rds")

include(":framework:cache")
include(":framework:client:base")
include(":framework:client:kakao")
//
// include(":framework:provider:jwt")
