import org.hidetake.gradle.swagger.generator.GenerateSwaggerUI
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
    id("com.epages.restdocs-api-spec")
    id("org.hidetake.swagger.generator")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain:rds"))
    implementation(project(":framework:cache"))
    implementation(project(":framework:client:kakao"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.117.Final:osx-aarch_64")

    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.4")

//    testImplementation(testFixtures(project(":domain:rds")))
}

springBoot {
    mainClass.value("com.weproud.AppApiApplicationKt")
}

openapi3 {
    this.setServer("http://localhost:8080")
    title = "APP API"
    description = "APP API"
    version = "1.0.0"
    format = "yaml"
    snippetsDirectory = "build/generated-snippets"
    outputDirectory = "build/api-spec"
}

tasks {
    withType<GenerateSwaggerUI> {
        dependsOn("openapi3")
        delete { delete("src/main/resources/static/docs/") }
        copy {
            from("build/api-spec/")
            into("src/main/resources/static/docs/")
        }
    }
    withType<BootJar> {
        dependsOn("openapi3")
    }
}
