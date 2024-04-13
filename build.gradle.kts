plugins {
    `maven-publish`
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.5.13" apply false
}

group = "de.cubeside.nmsutils"
version = "0.0.1-SNAPSHOT"
description = "nms adapter"

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {

}