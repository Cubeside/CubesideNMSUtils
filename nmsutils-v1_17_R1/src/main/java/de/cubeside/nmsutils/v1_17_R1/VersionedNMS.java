package de.cubeside.nmsutils.v1_17_R1;

import de.cubeside.nmsutils.NMSUtils;
import org.bukkit.plugin.Plugin;

public class VersionedNMS implements de.cubeside.nmsutils.NMSUtils.VersionedNMS {
    @Override
    public NMSUtils createNMSUtils(Plugin plugin) {
        String serverVersion = NMSUtils.getServerVersion();
        if (serverVersion.equals("1.17")) {
            return new de.cubeside.nmsutils.v1_17_R1_0.NMSUtilsImpl(plugin);
        } else if (serverVersion.equals("1.17.1")) {
            return new de.cubeside.nmsutils.v1_17_R1_1.NMSUtilsImpl(plugin);
        }
        return null;
    }
}
