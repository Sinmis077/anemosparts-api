plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "7.0.1.6134"
    id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
}

group = "dev.ioannis"
version = "0.7.0-PHOENIX"
description = "anemosracing.parts.api"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

val mockitoAgent = configurations.create("mockitoAgent")

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Implementation
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("org.apache.commons:commons-lang3:3.19.0")
    implementation("org.apache.commons:commons-collections4:4.5.0")
    implementation("commons-io:commons-io:2.20.0")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("me.paulschwarz:spring-dotenv:4.0.0")
    implementation("com.stripe:stripe-java:31.0.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")

    // Annotation Processors
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Compile Only
    compileOnly("org.projectlombok:lombok")
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")

    // Runtime Only
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

    // Tests Only
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito:mockito-core:5.20.0")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    mockitoAgent("org.mockito:mockito-core:5.20.0") { isTransitive = false }
}

tasks {
    test {
        useJUnitPlatform()
        jvmArgs = listOf("-javaagent:${mockitoAgent.asPath}")
    }
}

sonar {
    properties {
        property("sonar.projectKey", "Sinmis077_anemosparts-api_1b92c5a9-19ac-4bc8-abc4-67d7495c34ef")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.projectName", "anemosparts-api")
    }
}