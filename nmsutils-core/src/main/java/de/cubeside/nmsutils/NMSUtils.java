package de.cubeside.nmsutils;

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
                return ((VersionedNMS) versioningType.getDeclaredConstructor().newInstance()).createNMSUtils(plugin);
            }
        } catch (ClassNotFoundException ex) {
            // fallthrough, does not exist
        } catch (ReflectiveOperationException ex) {
            throwUnsupportedVersion(plugin.getServer(), ex);
        }

        try {
            String className = NMSUtils.class.getPackage().getName() + '.' + version + ".NMSUtilsImpl";
            Class<?> numUtilsType = Class.forName(className);
            if (NMSUtils.class.isAssignableFrom(numUtilsType)) {
                return ((NMSUtils) numUtilsType.getDeclaredConstructor(Plugin.class).newInstance(plugin));
            }
        } catch (ReflectiveOperationException ex) {
            throwUnsupportedVersion(plugin.getServer(), ex);
        }
        throwUnsupportedVersion(plugin.getServer());
        return null; // unreachable
    }

    private static String getNmsVersion(Plugin plugin) {
        Server server = plugin.getServer();
        Class<?> serverClass = server.getClass();
        while (!serverClass.getPackage().getName().startsWith(CRAFTBUKKIT_PACKAGE)) {
            serverClass = serverClass.getSuperclass();
            if (serverClass == null) {
                throwUnsupportedVersion(server);
            }
        }
        String packageName = serverClass.getPackage().getName();
        int i = packageName.lastIndexOf(".");
        if (i == -1) {
            throwUnsupportedVersion(server);
        }
        return packageName.substring(i + 1);
    }

    private static void throwUnsupportedVersion(Server server) {
        throwUnsupportedVersion(server, null);
    }

    private static void throwUnsupportedVersion(Server server, Exception ex) {
        throw new UnsupportedOperationException("Unsupported CraftBukkit version: " + server.getBukkitVersion(), ex);
    }

    public static interface VersionedNMS {
        NMSUtils createNMSUtils(Plugin plugin);
    }
}
