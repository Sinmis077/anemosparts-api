plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.ioannis"
version = "0.0.1-SNAPSHOT"
description = "anemosparts"

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

    // Annotation Processors
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Compile Only
    compileOnly("org.projectlombok:lombok")
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")

    // Runtime Only
    runtimeOnly("com.mysql:mysql-connector-j")

    // Tests Only
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
