package de.cubeside.nmsutils.v1_17_R1_1;

import de.cubeside.nmsutils.WorldUtils;
import java.util.ArrayList;
import java.util.logging.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
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
                kickPlayer((human.getBukkitEntity()), "Connection lost");
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

    @SuppressWarnings("deprecation")
    private void kickPlayer(Player player, String message) {
        player.kickPlayer(message);
    }
}
