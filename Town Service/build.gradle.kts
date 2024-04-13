import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"

    kotlin("jvm") version "1.9.22"
    kotlin("kapt") version "1.9.22"

    // open spring components
    kotlin("plugin.spring") version "1.9.22"

    // empty constructors and open models
    kotlin("plugin.jpa") version "1.9.22"
}

allOpen {
    annotation("jakarta.persistence.Entity")
}

group = "com.example"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2023.0.0"
//extra["micrometerVersion"] = "1.2.4"

//val springBootAdminVersion by extra("3.2.3")
val mapStructVersion by extra("1.5.5.Final")
val springDocVersion by extra("2.2.0")
val mockitoKotlinVersion by extra("5.2.1")
val kLoggingVersion by extra("2.0.11")
val logbackVersion by extra("1.4.14")
val liquibaseFixVersion by extra("1.5.0")

dependencies {

    //spring modules
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    //implementation("de.codecentric:spring-boot-admin-starter-client:$springBootAdminVersion")

    // bd
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.liquibase:liquibase-core")
    implementation("net.lbruun.springboot:preliquibase-spring-boot-starter:$liquibaseFixVersion")
    runtimeOnly("org.postgresql:postgresql")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // logging ??
    //implementation("org.zalando:logbook-spring-boot-starter:3.7.2")
    //implementation("org.zalando:logbook-jaxrs:3.7.2")
    implementation("io.github.microutils:kotlin-logging-jvm:${kLoggingVersion}")
    implementation("ch.qos.logback:logback-classic:${logbackVersion}")
    implementation("ch.qos.logback:logback-core:${logbackVersion}")
    //implementation ("io.micrometer:micrometer-tracing")
    //implementation ("io.micrometer:micrometer-tracing-bridge-brave")

    // mapstruct
    implementation("org.mapstruct:mapstruct:$mapStructVersion")
    kapt("org.mapstruct:mapstruct-processor:$mapStructVersion")

    // doc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}


dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        //mavenBom("io.micrometer:micrometer-tracing-bom:${property("micrometerVersion")}")
    }
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
        arg("mapstruct.unmappedTargetPolicy", "IGNORE")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
