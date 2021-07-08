package de.cubeside.nmsutils.plugin;

import de.cubeside.nmsutils.NMSUtils;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class NMSUtilsPlugin extends JavaPlugin {
    private NMSUtils nmsUtils;

    @Override
    public void onEnable() {
        nmsUtils = NMSUtils.createInstance(this);
        getLogger().info("Using implementation: " + nmsUtils.getClass().getName());
        getServer().getServicesManager().register(NMSUtils.class, nmsUtils, this, ServicePriority.Normal);
    }

    public NMSUtils getNmsUtils() {
        return nmsUtils;
    }
}
