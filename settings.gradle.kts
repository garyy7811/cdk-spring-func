pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://plugins.gradle.org/m2") }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {

        id("com.github.johnrengelman.shadow") version "6.1.0"
        id("org.springframework.boot") version "2.5.1"
        id("org.springframework.boot.experimental.thin-launcher") version "1.0.27.RELEASE"
        id("io.spring.dependency-management") version "1.0.11.RELEASE"
    }
}

include("sample-func", "cdk")

rootProject.name = "cdk-spring-func-nine"
