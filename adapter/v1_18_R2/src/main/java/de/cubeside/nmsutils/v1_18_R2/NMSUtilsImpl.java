package de.cubeside.nmsutils.v1_18_R2;

import de.cubeside.nmsutils.EntityUtils;
import de.cubeside.nmsutils.MiscUtils;
import de.cubeside.nmsutils.NMSUtils;
import de.cubeside.nmsutils.WorldUtils;
import org.bukkit.plugin.Plugin;

public class NMSUtilsImpl implements NMSUtils {
    private final Plugin plugin;
    private EntityUtils entityUtilsImpl;
    private WorldUtilsImpl worldUtilsImpl;
    private MiscUtilsImpl miscUtilsImpl;

    public NMSUtilsImpl(Plugin plugin) {
        this.plugin = plugin;
        this.entityUtilsImpl = new EntityUtilsImpl(this);
        this.worldUtilsImpl = new WorldUtilsImpl(this);
        this.miscUtilsImpl = new MiscUtilsImpl(this);
        // Remapper.foo(net.minecraft.world.entity.ambient.Bat.class);
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

    @Override
    public MiscUtils getMiscUtils() {
        return miscUtilsImpl;
    }
}
