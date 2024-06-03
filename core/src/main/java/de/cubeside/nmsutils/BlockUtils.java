package de.cubeside.nmsutils;

import java.util.logging.Level;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface BlockUtils {
    public NMSUtils getNMSUtils();

    public default boolean useBlock(Block block, Player player) {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
        return false;
    }
}
