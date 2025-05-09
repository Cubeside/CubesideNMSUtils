package de.cubeside.nmsutils;

import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public interface BlockUtils {
    public NMSUtils getNMSUtils();

    /**
     * Fakes a use of a block by a player.
     *
     * @param block
     *            the block to use
     * @param player
     *            the player who uses the block
     * @return true if the block could be used
     */
    public default boolean useBlock(Block block, Player player) {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
        return false;
    }

    /**
     * Returns a set of players who previously accessed a vault. The block must be a vault block.
     * The returned set is backed by the internal set, so it should be copied if you want to store it.
     *
     * @param block
     *            a block that contains a vault
     * @return a set of players who have accessed this vault
     * @deprecated Bukkit api is now available
     */
    @Deprecated(forRemoval = true)
    public default Set<UUID> getVaultRewardedPlayers(Block block) {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
        return Set.of();
    }

    public default int getNetworkBlockDataId(BlockData block) {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
        return -1;
    }

    public default void setTrialSpawnerCooldown(Block block, int ticks) {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
    }

    public default void setTrialSpawnerConfig(Block block, NamespacedKey key) {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
    }

    public default Set<NamespacedKey> getTrialSpawnerConfigs() {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
        return Set.of();
    }
}
