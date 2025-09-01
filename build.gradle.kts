plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("org.springframework.boot") version "2.7.4" apply false
    id("maven-publish")
}

group = "com.aakash"
version = "0.0.4-SNAPSHOT"
description = "VaultUtility - reusable library"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Core Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")

    // Networking
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // JSON handling
    implementation("org.json:json:20240303")

    // Optional Spring support
    implementation("org.springframework.boot:spring-boot-starter:2.7.4")
    implementation("org.springframework.boot:spring-boot-starter-json:2.7.4")
}

kotlin {
    jvmToolchain {
        // Use Java 11
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "vault-utility"
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/AakashSingh007/VaultUtility")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: ""
                password = project.findProperty("gpr.key") as String? ?: ""
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
