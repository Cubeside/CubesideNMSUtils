pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
  }
}

// rootProject.name = "nmsutils_parent"
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
include("core")

// Include all adapters
val adapterDirName = "adapter";
// Get all directories inside the adapters directory

val filter = extra.has("adapter").let { if (it) extra.get("adapter") as String else ""};
println("Adapter Pattern: " + filter);
val subprojectDirs = File(settings.settingsDir, adapterDirName).listFiles().filter { it.isDirectory && !it.name.startsWith(".")}
// Add them as subprojects
subprojectDirs.forEach { subprojectDir -> run {
        if (subprojectDir.name.contains(filter)) {
            println("Including Adapter: " + subprojectDir.name);
            include(adapterDirName + ":" + subprojectDir.name);
        }
    }
}

include("standalone")
include("plugin")