package de.cubeside.nmsutils.v1_20_R1;

import com.mojang.serialization.Lifecycle;
import de.cubeside.nmsutils.BiomeUtils;
import de.cubeside.nmsutils.NMSUtils;
import de.cubeside.nmsutils.biome.CustomBiome;
import de.cubeside.nmsutils.util.ReobfHelper;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.BiomeSpecialEffects.Builder;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

public class BiomeUtilsImpl implements BiomeUtils {
    private final NMSUtilsImpl nmsUtils;

    private HashMap<NamespacedKey, CustomBiomeImpl> customBiomes;
    private HashMap<Biome, CustomBiomeImpl> customBiomesByBiome;
    private Collection<? extends CustomBiome> unmodifiableCustomBiomes;

    private static final Field FIELD_MAPPED_REGISTRY_FROZEN = ReobfHelper.getFieldByMojangName(MappedRegistry.class, "frozen");
    private static final Field FIELD_MAPPED_REGISTRY_UNREGISTERED_INTRUSIVE_HOLDERS = ReobfHelper.getFieldByMojangName(MappedRegistry.class, "unregisteredIntrusiveHolders");

    public BiomeUtilsImpl(NMSUtilsImpl nmsUtils) {
        this.nmsUtils = nmsUtils;
        this.customBiomes = new HashMap<>();
        this.customBiomesByBiome = new HashMap<>();
        this.unmodifiableCustomBiomes = Collections.unmodifiableCollection(customBiomes.values());
    }

    public NMSUtils getNMSUtils() {
        return nmsUtils;
    }

    @Override
    public CustomBiome registerCustomBiome(NamespacedKey id, float downfall, float temperature, de.cubeside.nmsutils.biome.Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor,
            GrassColorModifier grassColorModifier) {
        Server server = nmsUtils.getPlugin().getServer();
        CraftServer craftserver = (CraftServer) server;
        DedicatedServer dedicatedserver = craftserver.getServer();
        ResourceKey<Biome> newKey = ResourceKey.create(Registries.BIOME, new ResourceLocation(id.getNamespace(), id.getKey()));

        ResourceKey<Biome> oldKey = Biomes.FOREST;
        WritableRegistry<Biome> registrywritable = (WritableRegistry<Biome>) dedicatedserver.registryAccess().registryOrThrow(Registries.BIOME);

        try {
            FIELD_MAPPED_REGISTRY_FROZEN.set(registrywritable, false);
            FIELD_MAPPED_REGISTRY_UNREGISTERED_INTRUSIVE_HOLDERS.set(registrywritable, new IdentityHashMap<>());
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Biome forestbiome = registrywritable.get(oldKey);

        BiomeBuilder builder = new Biome.BiomeBuilder();
        builder.downfall(downfall);
        builder.temperature(temperature);
        boolean mojangPrecipitation = false;

        if (precipitation == de.cubeside.nmsutils.biome.Precipitation.RAIN) {
            mojangPrecipitation = true;
        } else if (precipitation == de.cubeside.nmsutils.biome.Precipitation.SNOW) {
            mojangPrecipitation = true;
        }
        builder.hasPrecipitation(mojangPrecipitation);
        builder.mobSpawnSettings(forestbiome.getMobSettings());
        builder.generationSettings(forestbiome.getGenerationSettings());
        builder.temperatureAdjustment(TemperatureModifier.NONE);
        Builder effects = new BiomeSpecialEffects.Builder();
        effects.waterColor(waterColor == null ? forestbiome.getWaterColor() : waterColor);
        effects.waterFogColor(waterFogColor == null ? forestbiome.getWaterFogColor() : waterFogColor);
        effects.fogColor(fogColor == null ? forestbiome.getFogColor() : fogColor);
        effects.skyColor(skyColor == null ? forestbiome.getSkyColor() : skyColor);
        if (foliageColor != null) {
            effects.foliageColorOverride(foliageColor);
        }
        if (grassColor != null) {
            effects.grassColorOverride(grassColor);
        }
        effects.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS);
        net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier nativeGrassColorModifier = switch (grassColorModifier) {
            case DARK_FOREST -> net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier.DARK_FOREST;
            case SWAMP -> net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier.SWAMP;
            default -> net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier.NONE;
        };
        effects.grassColorModifier(nativeGrassColorModifier);
        builder.specialEffects(effects.build());

        Biome newbiome = builder.build();

        registrywritable.createIntrusiveHolder(newbiome);
        Reference<Biome> biomeHolder = registrywritable.register(newKey, newbiome, Lifecycle.stable());

        try {
            FIELD_MAPPED_REGISTRY_UNREGISTERED_INTRUSIVE_HOLDERS.set(registrywritable, null);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        CustomBiomeImpl impl = new CustomBiomeImpl(id, newKey, newbiome, biomeHolder);
        customBiomes.put(id, impl);
        customBiomesByBiome.put(newbiome, impl);

        return new CustomBiomeImpl(id, newKey, newbiome, biomeHolder);
    }

    @Override
    public Collection<? extends CustomBiome> getAllCustomBiomes() {
        return unmodifiableCustomBiomes;
    }

    @Override
    public CustomBiome getCustomBiome(NamespacedKey id) {
        return customBiomes.get(id);
    }

    @Override
    public CustomBiome getCustomBiomeAt(Location location) {
        location.getWorld().getChunkAt(location);
        Level level = ((CraftWorld) location.getWorld()).getHandle();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        BlockPos pos = new BlockPos(x, 0, z);
        if (level.isLoaded(pos)) {
            LevelChunk chunk = level.getChunkAt(pos);
            if (chunk != null) {

                Holder<Biome> biomeHolder = chunk.getNoiseBiome(x >> 2, y >> 2, z >> 2);
                if (biomeHolder.isBound()) {
                    Biome biome = biomeHolder.value();
                    return customBiomesByBiome.get(biome);
                }
            }
        }
        return null;
    }
}
