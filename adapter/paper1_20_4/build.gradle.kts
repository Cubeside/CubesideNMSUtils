plugins {
  `maven-publish`
  `java-library`
  id("io.papermc.paperweight.userdev")
}

group = "de.cubeside.nmsutils"
version = "0.0.1-SNAPSHOT"
description = "nms adapter"

repositories {
    mavenLocal()
}

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
  toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {
  implementation(project(":core"))
  // implementation("de.cubeside.nmsutils:nmsutils-core:0.0.1-SNAPSHOT")
  paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
  // paperweight.foliaDevBundle("1.20.4-R0.1-SNAPSHOT")
  // paperweight.devBundle("com.example.paperfork", "1.20.4-R0.1-SNAPSHOT")
}

tasks {
  // Configure reobfJar to run when invoking the build task
  assemble {
    dependsOn(reobfJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
    options.release = 17
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }

  /*
  reobfJar. {
    // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
    // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
    outputJar = layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar")
  }
   */
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.reobfJar)
            groupId = "de.cubeside.nmsutils"
            artifactId = "nmsutils-paper1_20_4"
            version = "0.0.1-SNAPSHOT"

            from(components["java"])
        }
    }
}
