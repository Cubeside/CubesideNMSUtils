package de.cubeside.nmsutils.v1_19_R2;

import de.cubeside.nmsutils.biome.CustomBiome;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Holder.Reference;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

public class CustomBiomeImpl implements CustomBiome {
    private NamespacedKey bukkitKey;
    private Reference<Biome> biomeHolder;
    private MutableBlockPos pos = new MutableBlockPos();

    public CustomBiomeImpl(NamespacedKey bukkitKey, ResourceKey<Biome> key, Biome biome, Reference<Biome> biomeHolder) {
        this.bukkitKey = bukkitKey;
        this.biomeHolder = biomeHolder;
    }

    @Override
    public NamespacedKey getId() {
        return bukkitKey;
    }

    @Override
    public boolean setBiome(Location location) {
        location.getWorld().getChunkAt(location);
        Level level = ((CraftWorld) location.getWorld()).getHandle();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        pos.set(x, 0, z);
        if (level.isLoaded(pos)) {
            LevelChunk chunk = level.getChunkAt(pos);
            if (chunk != null) {

                chunk.setBiome(x >> 2, y >> 2, z >> 2, biomeHolder);
                chunk.setUnsaved(true);
                return true;
            }
        }
        return false;
    }
}
