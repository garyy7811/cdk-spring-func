plugins {
    java
    "maven-publish"
    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("com.github.johnrengelman.shadow")
    id("org.springframework.boot.experimental.thin-launcher")
}


repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/release") }
    maven { url = uri("https://plugins.gradle.org/m2") }
    maven {
        url = uri("http://dynamodb-local.s3-website-us-west-2.amazonaws.com/release")
    }
    gradlePluginPortal()
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
    implementation("org.springframework.cloud:spring-cloud-starter-function-webflux")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.amazonaws:aws-lambda-java-events:3.9.0")
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
    implementation("com.amazonaws:aws-java-sdk-dynamodb:1.12.13")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.amazonaws:DynamoDBLocal:1.16.0")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "example.FunctionConfiguration"
    }
}
tasks.shadowJar {
    archiveFileName.set("sample-${classifier}-ns.r100-deploy.${extension}")
    dependencies {
        exclude(
            dependency("org.springframework.cloud:spring-cloud-function-web")
        )
    }
    // Required for Spring
    mergeServiceFiles()
    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")

    transform(com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer::class.java) {
        paths = listOf("META-INF/spring.factories")
        mergeStrategy = "append"
    }
}


tasks.test {
    useJUnitPlatform()
    filter {
        includeTestsMatching("*example.MapTests.test*")
    }
}


dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-function-dependencies:3.1.3")
    }
}
