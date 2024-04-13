package de.cubeside.nmsutils;

import de.cubeside.nmsutils.biome.CustomBiome;
import de.cubeside.nmsutils.biome.Precipitation;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public interface BiomeUtils {
    public enum GrassColorModifier {
        NONE,
        DARK_FOREST,
        SWAMP,
    }

    default CustomBiome registerCustomBiome(NamespacedKey id, float downfall, float temperature, Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor) {
        return registerCustomBiome(id, downfall, temperature, precipitation, fogColor, waterColor, waterFogColor, skyColor, foliageColor, grassColor, GrassColorModifier.NONE);
    }

    CustomBiome registerCustomBiome(NamespacedKey id, float downfall, float temperature, Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor, GrassColorModifier grassColorModifier);

    CustomBiome getCustomBiome(NamespacedKey id);

    CustomBiome getCustomBiomeAt(Location location);

    Collection<? extends CustomBiome> getAllCustomBiomes();
}
