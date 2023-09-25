package de.cubeside.nmsutils.v1_20_R2;

import de.cubeside.nmsutils.BiomeUtils;
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
    private BiomeUtilsImpl biomeUtils;
    private NbtUtilsImpl nbtUtils;

    public NMSUtilsImpl(Plugin plugin) {
        this.plugin = plugin;
        this.entityUtilsImpl = new EntityUtilsImpl(this);
        this.worldUtilsImpl = new WorldUtilsImpl(this);
        this.miscUtilsImpl = new MiscUtilsImpl(this);
        this.biomeUtils = new BiomeUtilsImpl(this);
        this.nbtUtils = new NbtUtilsImpl(this);
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

    @Override
    public BiomeUtils getBiomeUtils() {
        return biomeUtils;
    }

    @Override
    public NbtUtilsImpl getNbtUtils() {
        return nbtUtils;
    }
}
