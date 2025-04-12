package de.cubeside.nmsutils.paper1_21_5;

import ca.spottedleaf.moonrise.patches.chunk_system.scheduling.NewChunkHolder;
import de.cubeside.nmsutils.WorldUtils;
import java.util.ArrayList;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

public class WorldUtilsImpl implements WorldUtils {
    private final NMSUtilsImpl nmsUtils;

    public WorldUtilsImpl(NMSUtilsImpl nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    @Override
    public void saveWorldNow(World world) {
        world.save();

        CraftWorld craftWorld = (CraftWorld) world;
        ServerLevel handle = craftWorld.getHandle();
        try {
            handle.save(null, true, false);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while saving world", e);
        }
    }

    @Override
    public void forceUnloadWorldWithoutSaving(World world, Location playerTarget) {
        // final long t0 = System.currentTimeMillis();
        // String worldName = world.getName();

        if (playerTarget.getWorld() == null || playerTarget.getWorld() == world) {
            throw new IllegalArgumentException("Valid target world required");
        }
        // move players out of this world
        try {
            for (Player p : world.getPlayers()) {
                if (p.isDead()) {
                    p.spigot().respawn();
                }
                if (p.getWorld() == world) {
                    p.teleport(playerTarget);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // remove broken players
        CraftWorld craftWorld = (CraftWorld) world;
        ServerLevel handle = craftWorld.getHandle();
        if (!handle.players().isEmpty()) {
            for (ServerPlayer human : new ArrayList<>(handle.players())) {
                human.getBukkitEntity().kick(Component.text("Connection lost"));
            }
            handle.players().clear();
        }

        // unload the now empty world from the server
        if (!nmsUtils.getPlugin().getServer().unloadWorld(world, false)) {
            throw new IllegalStateException("Could not unload world");
        }

        // long t = System.currentTimeMillis();
        // nmsUtils.getPlugin().getLogger().info("Unloading world " + worldName + " completed in " + (t - t0) + "ms.");
    }

    @Override
    public void saveChunkNow(Chunk chunk) {
        NewChunkHolder chunkHolder = ((CraftChunk) chunk).getCraftWorld().getHandle().moonrise$getChunkTaskScheduler().chunkHolderManager.getChunkHolder(chunk.getX(), chunk.getZ());
        if (chunkHolder != null) {
            chunkHolder.save(false);
        } else {
            throw new IllegalStateException("ChunkHolder is null");
        }
    }
}
