pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
  }
}

// rootProject.name = "nmsutils_parent"
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
include("core")

// Include all adapters
val adapterDirName = "adapter";
// Get all directories inside the adapters directory

val filters = providers.gradleProperty("adapter")
    .orNull
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

println("Adapter Patterns: $filters")

val subprojectDirs = File(settings.settingsDir, adapterDirName)
    .listFiles()
    ?.filter { it.isDirectory && !it.name.startsWith(".") }
    ?: emptyList()

subprojectDirs.forEach { subprojectDir ->
    if (filters.isEmpty() || filters.any { subprojectDir.name.contains(it) }) {
        println("Including Adapter: ${subprojectDir.name}")
        include("$adapterDirName:${subprojectDir.name}")
    }
}

include("standalone")
include("plugin")