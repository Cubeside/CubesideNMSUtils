package de.cubeside.nmsutils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

public interface NMSUtils {
    /**
     * Gets the plugin that created this NMSUtils instance.
     *
     * @return the plugin
     */
    public Plugin getPlugin();

    /**
     * Gets an instance of EntityUtils for manipulating entities.
     *
     * @return the EntityUtils
     */
    public EntityUtils getEntityUtils();

    /**
     * Gets an instance of WorldUtils for manipulating worlds.
     *
     * @return the WorldUtils
     */
    public WorldUtils getWorldUtils();

    public static final String CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit";

    public static NMSUtils createInstance(Plugin plugin) {
        String version = getNmsVersion(plugin);
        try {
            String className = NMSUtils.class.getPackage().getName() + '.' + version + ".VersionedNMS";
            Class<?> versioningType = Class.forName(className);
            if (VersionedNMS.class.isAssignableFrom(versioningType)) {
                NMSUtils result = ((VersionedNMS) versioningType.getDeclaredConstructor().newInstance()).createNMSUtils(plugin);
                if (result != null) {
                    return result;
                }
                // fallthrough, not versioned
            }
        } catch (ClassNotFoundException ex) {
            // fallthrough, does not exist
        } catch (ReflectiveOperationException ex) {
            throwUnsupportedVersion(plugin, ex);
        }

        try {
            String className = NMSUtils.class.getPackage().getName() + '.' + version + ".NMSUtilsImpl";
            Class<?> numUtilsType = Class.forName(className);
            if (NMSUtils.class.isAssignableFrom(numUtilsType)) {
                return ((NMSUtils) numUtilsType.getDeclaredConstructor(Plugin.class).newInstance(plugin));
            }
        } catch (ClassNotFoundException ex) {
            throwUnsupportedVersion(plugin);
        } catch (ReflectiveOperationException ex) {
            throwUnsupportedVersion(plugin, ex);
        }
        throwUnsupportedVersion(plugin);
        return null; // unreachable
    }

    private static String getNmsVersion(Plugin plugin) {
        Server server = plugin.getServer();
        Class<?> serverClass = server.getClass();
        while (!serverClass.getPackage().getName().startsWith(CRAFTBUKKIT_PACKAGE)) {
            serverClass = serverClass.getSuperclass();
            if (serverClass == null) {
                throwUnsupportedVersion(plugin);
            }
        }
        String packageName = serverClass.getPackage().getName();
        int i = packageName.lastIndexOf(".");
        if (i == -1) {
            throwUnsupportedVersion(plugin);
        }
        return packageName.substring(i + 1);
    }

    private static void throwUnsupportedVersion(Plugin plugin) {
        throwUnsupportedVersion(plugin, null);
    }

    private static void throwUnsupportedVersion(Plugin plugin, Exception ex) {
        String msg = "Unsupported CraftBukkit version: " + plugin.getServer().getBukkitVersion();
        plugin.getLogger().severe(msg);
        throw new UnsupportedOperationException(msg, ex);
    }

    public static String getServerVersion() {
        String version = Bukkit.getServer().getVersion();
        int start = version.indexOf("(MC: ");
        if (start >= 0) {
            start += 5;
            int end = version.indexOf(")");
            if (end > 0) {
                return version.substring(start, end);
            }
        }
        throw new RuntimeException("Could not detect minecraft server version! Version is: " + version);
    }

    public static interface VersionedNMS {
        NMSUtils createNMSUtils(Plugin plugin);
    }
}
