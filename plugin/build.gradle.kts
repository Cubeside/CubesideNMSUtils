plugins {
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `java-library`
}

group = "de.cubeside.nmsutils"
version = "0.0.1-SNAPSHOT"
description = "nms plugin"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.fabricmc.net/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation(project(":standalone", "shadow"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}

tasks {
    shadowJar {
        archiveFileName = "CubesideNMSUtils.jar"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            project.shadow.component(this)
            groupId = "de.cubeside.nmsutils"
            artifactId = "nmsutils-plugin"
        }
    }
    repositories {
        maven {
            url = uri("https://www.iani.de/nexus/content/repositories/snapshots")
            credentials {
                username = System.getenv('NEXUS_USR')
                password = System.getenv('NEXUS_PSW')
            }
        }
    }
}