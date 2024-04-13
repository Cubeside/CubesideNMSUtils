package de.cubeside.nmsutils.biome;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public interface CustomBiome {

    NamespacedKey getId();

    boolean setBiome(Location location);

}
