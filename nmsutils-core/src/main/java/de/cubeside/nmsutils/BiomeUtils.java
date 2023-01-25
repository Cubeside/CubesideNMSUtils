package de.cubeside.nmsutils;

import de.cubeside.nmsutils.biome.CustomBiome;
import de.cubeside.nmsutils.biome.Precipitation;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public interface BiomeUtils {
    CustomBiome registerCustomBiome(NamespacedKey id, float downfall, float temperature, Precipitation precipitation, int fogColor, int waterColor, int waterFogColor, int skyColor, Integer foliageColor, Integer grassColor);

    CustomBiome getCustomBiome(NamespacedKey id);

    CustomBiome getCustomBiomeAt(Location location);

    Collection<? extends CustomBiome> getAllCustomBiomes();
}
