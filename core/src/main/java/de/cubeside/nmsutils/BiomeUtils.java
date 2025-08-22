package de.cubeside.nmsutils;

import de.cubeside.nmsutils.biome.CustomBiome;
import de.cubeside.nmsutils.biome.Precipitation;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;

public interface BiomeUtils {
    public enum GrassColorModifier {
        NONE,
        DARK_FOREST,
        SWAMP,
    }

    @Deprecated(forRemoval = true)
    default CustomBiome registerCustomBiome(NamespacedKey id, float downfall, float temperature, Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor) {
        return registerCustomBiome(id, downfall, temperature, precipitation, fogColor, waterColor, waterFogColor, skyColor, foliageColor, grassColor, GrassColorModifier.NONE);
    }

    @Deprecated(forRemoval = true)
    CustomBiome registerCustomBiome(NamespacedKey id, float downfall, float temperature, Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor, GrassColorModifier grassColorModifier);

    @Deprecated(forRemoval = true)
    CustomBiome getCustomBiome(NamespacedKey id);

    @Deprecated(forRemoval = true)
    CustomBiome getCustomBiomeAt(Location location);

    @Deprecated(forRemoval = true)
    Collection<? extends CustomBiome> getAllCustomBiomes();

    default Biome registerBiome(NamespacedKey id, float downfall, float temperature, Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor) {
        return registerBiome(id, downfall, temperature, precipitation, fogColor, waterColor, waterFogColor, skyColor, foliageColor, grassColor, GrassColorModifier.NONE);
    }

    default Biome registerBiome(NamespacedKey id, float downfall, float temperature, Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor, GrassColorModifier grassColorModifier) {
        throw new IllegalStateException("not implemented");
    }
    
    default org.bukkit.generator.BiomeProvider getVanillaOverworldBiomeProvider(long seed) {
        throw new IllegalStateException("not implemented");
    }
}
