package de.cubeside.nmsutils.v1_19_R1;

import de.cubeside.nmsutils.NMSUtils;
import org.bukkit.plugin.Plugin;

public class VersionedNMS implements de.cubeside.nmsutils.NMSUtils.VersionedNMS {
    @Override
    public NMSUtils createNMSUtils(Plugin plugin) throws ReflectiveOperationException {
        String serverVersion = NMSUtils.getServerVersion();
        Class<?> pluginClass = null;
        if (serverVersion.equals("1.19")) {
            pluginClass = Class.forName("de.cubeside.nmsutils.v1_19_R1_0.NMSUtilsImpl");
        } else if (serverVersion.equals("1.19.1") || serverVersion.equals("1.19.2")) {
            pluginClass = Class.forName("de.cubeside.nmsutils.v1_19_R1_1.NMSUtilsImpl");
        }
        if (pluginClass != null) {
            return (NMSUtils) pluginClass.getConstructor(Plugin.class).newInstance(plugin);
        }
        return null;
    }
}
