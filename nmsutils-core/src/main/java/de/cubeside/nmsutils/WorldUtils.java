package de.cubeside.nmsutils;

import org.bukkit.Location;
import org.bukkit.World;

public interface WorldUtils {
    /**
     * Forces an instant syncronous save of all chunks in this world. This may cause lags on the server.
     *
     * @param world
     *            the world to be saved
     */
    public void saveWorldNow(World world);

    /**
     * Unloads a world without saving the loaded chunks. All players in this world are teleported out of the world.
     *
     * @param world
     *            The world to be unloaded
     * @param playerTarget
     *            The target location for the players currently in this world
     */
    public void forceUnloadWorldWithoutSaving(World world, Location playerTarget);
}
