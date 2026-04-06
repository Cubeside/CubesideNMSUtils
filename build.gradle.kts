plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT" apply false
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<JavaExec> {
        systemProperty("file.encoding", "UTF-8")
    }
}