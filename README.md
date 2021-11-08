# CubesideNMSUtils
## Building
### Prerequisites
What you need for building this plugin:
- Spigot including the remapping data for every version: For example `java -jar BuildTools.jar --remapped --rev 1.17.1`
- Paper for every version: https://github.com/PaperMC/Paper#how-to-compiling-jar-from-source
### Build using maven
Now you can build the plugin:
- `mvn install` build for the latest supported version only
- `mvn install -Dall` build for all supported versions
- `mvn install -Pv1_17,v1_17_1` build for specific versions (in this case 1.17 and 1.17.1)
