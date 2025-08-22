package de.cubeside.nmsutils.paper1_21_8;

import de.cubeside.nmsutils.BiomeUtils;
import de.cubeside.nmsutils.NMSUtils;
import de.cubeside.nmsutils.biome.CustomBiome;
import de.cubeside.nmsutils.biome.Precipitation;
import de.cubeside.nmsutils.util.ReobfHelper;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.BiomeSpecialEffects.Builder;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.biome.Biomes;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.generator.WorldInfo;

public class BiomeUtilsImpl implements BiomeUtils {
    private final NMSUtilsImpl nmsUtils;

    private static final Field FIELD_MAPPED_REGISTRY_FROZEN = ReobfHelper.getFieldByMojangName(MappedRegistry.class, "frozen");
    private static final Field FIELD_MAPPED_REGISTRY_UNREGISTERED_INTRUSIVE_HOLDERS = ReobfHelper.getFieldByMojangName(MappedRegistry.class, "unregisteredIntrusiveHolders");
    private static final Field FIELD_MAPPED_HOLDER_REFERENCE_TAGS = ReobfHelper.getFieldByMojangName(Holder.Reference.class, "tags");

    public BiomeUtilsImpl(NMSUtilsImpl nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    public NMSUtils getNMSUtils() {
        return nmsUtils;
    }

    @Override
    public org.bukkit.block.Biome registerBiome(NamespacedKey id, float downfall, float temperature, de.cubeside.nmsutils.biome.Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor,
            GrassColorModifier grassColorModifier) {
        Server server = nmsUtils.getPlugin().getServer();
        CraftServer craftserver = (CraftServer) server;
        DedicatedServer dedicatedserver = craftserver.getServer();
        ResourceKey<Biome> newKey = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getKey()));

        ResourceKey<Biome> oldKey = Biomes.FOREST;
        WritableRegistry<Biome> registrywritable = (WritableRegistry<Biome>) dedicatedserver.registryAccess().lookupOrThrow(Registries.BIOME);

        try {
            FIELD_MAPPED_REGISTRY_FROZEN.set(registrywritable, false);
            FIELD_MAPPED_REGISTRY_UNREGISTERED_INTRUSIVE_HOLDERS.set(registrywritable, new IdentityHashMap<>());
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Biome forestbiome = registrywritable.getValue(oldKey);

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
        Reference<Biome> biomeHolder = registrywritable.register(newKey, newbiome, RegistrationInfo.BUILT_IN);

        try {
            FIELD_MAPPED_HOLDER_REFERENCE_TAGS.set(biomeHolder, Set.of());
            FIELD_MAPPED_REGISTRY_UNREGISTERED_INTRUSIVE_HOLDERS.set(registrywritable, null);
            FIELD_MAPPED_REGISTRY_FROZEN.set(registrywritable, true);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).get(id);
    }

    @Override
    public CustomBiome registerCustomBiome(NamespacedKey id, float downfall, float temperature, Precipitation precipitation, Integer fogColor, Integer waterColor, Integer waterFogColor, Integer skyColor, Integer foliageColor, Integer grassColor, GrassColorModifier grassColorModifier) {
        registerBiome(id, downfall, temperature, precipitation, fogColor, waterColor, waterFogColor, skyColor, foliageColor, grassColor, grassColorModifier);
        return null;
    }

    @Override
    public Collection<? extends CustomBiome> getAllCustomBiomes() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public CustomBiome getCustomBiomeAt(Location location) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public CustomBiome getCustomBiome(NamespacedKey id) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public org.bukkit.generator.BiomeProvider getVanillaOverworldBiomeProvider(long seed) {
        LevelStem stem = MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.LEVEL_STEM).getValue(LevelStem.OVERWORLD);

        final net.minecraft.world.level.levelgen.RandomState randomState;
        if (stem.generator() instanceof net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator noiseBasedChunkGenerator) {
            randomState = net.minecraft.world.level.levelgen.RandomState.create(noiseBasedChunkGenerator.generatorSettings().value(),
                    MinecraftServer.getServer().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.NOISE), seed);
        } else {
            randomState = net.minecraft.world.level.levelgen.RandomState.create(net.minecraft.world.level.levelgen.NoiseGeneratorSettings.dummy(),
                    MinecraftServer.getServer().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.NOISE), seed);
        }

        final java.util.List<org.bukkit.block.Biome> possibleBiomes = stem.generator().getBiomeSource().possibleBiomes().stream()
                .map(biome -> org.bukkit.craftbukkit.block.CraftBiome.minecraftHolderToBukkit(biome))
                .toList();
        return new org.bukkit.generator.BiomeProvider() {
            @Override
            public org.bukkit.block.Biome getBiome(final WorldInfo worldInfo, final int x, final int y, final int z) {
                return org.bukkit.craftbukkit.block.CraftBiome.minecraftHolderToBukkit(
                        stem.generator().getBiomeSource().getNoiseBiome(x >> 2, y >> 2, z >> 2, randomState.sampler()));
            }

            @Override
            public java.util.List<org.bukkit.block.Biome> getBiomes(final org.bukkit.generator.WorldInfo worldInfo) {
                return possibleBiomes;
            }
        };
    }
}
