package de.cubeside.nmsutils.v1_17_R1_1;

import de.cubeside.nmsutils.EntityUtils;
import de.cubeside.nmsutils.NMSUtils;
import de.cubeside.nmsutils.WorldUtils;
import org.bukkit.plugin.Plugin;

public class NMSUtilsImpl implements NMSUtils {
    private final Plugin plugin;
    private EntityUtils entityUtilsImpl;
    private WorldUtilsImpl worldUtilsImpl;

    public NMSUtilsImpl(Plugin plugin) {
        this.plugin = plugin;
        this.entityUtilsImpl = new EntityUtilsImpl(this);
        this.worldUtilsImpl = new WorldUtilsImpl(this);
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public EntityUtils getEntityUtils() {
        return entityUtilsImpl;
    }

    @Override
    public WorldUtils getWorldUtils() {
        return worldUtilsImpl;
    }

}
