// rootProject.name = "nmsutils_parent"
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
include("core")

// Include all adapters
val adapterDirName = "adapter";
// Get all directories inside the adapters directory

val subprojectDirs = File(settings.settingsDir, adapterDirName).listFiles().filter { it.isDirectory && !it.name.startsWith(".")}
// Add them as subprojects
subprojectDirs.forEach { subprojectDir ->
    include(adapterDirName + ":" + subprojectDir.name)
}

include("standalone")
include("plugin")