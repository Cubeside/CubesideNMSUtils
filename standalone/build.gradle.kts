import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `maven-publish`
    id("com.gradleup.shadow") version "9.0.2"
    `java-library`
}

group = "de.cubeside.nmsutils"
version = "0.0.1-SNAPSHOT"
description = "nms adapter"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.fabricmc.net/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("net.fabricmc:mapping-io:0.3.0")
    implementation(project(":core"))
    project.project(":adapter").subprojects.forEach {
        implementation(project(it.path, "reobf"))
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    jar {
        archiveClassifier = "noshadow"
    }
   shadowJar {
       archiveClassifier = ""
       relocate("net.fabricmc.mappingio", "de.cubeside.nmsutils.libs.net.fabricmc.mappingio")
       relocate("org.objectweb.asm", "de.cubeside.nmsutils.libs.org.objectweb.asm")
   }
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.shadowJar)
            groupId = "de.cubeside.nmsutils"
            artifactId = "nmsutils-standalone"
        }
    }
    repositories {
        maven {
            url = uri("https://www.iani.de/nexus/content/repositories/snapshots")
            credentials {
                username = System.getenv("NEXUS_USR")
                password = System.getenv("NEXUS_PSW")
            }
        }
    }
}