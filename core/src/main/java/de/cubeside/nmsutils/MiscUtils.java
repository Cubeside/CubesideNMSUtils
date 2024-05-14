package de.cubeside.nmsutils;

import de.cubeside.nmsutils.nbt.CompoundTag;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team.OptionStatus;

public interface MiscUtils {

    NMSUtils getNMSUtils();

    /**
     * Sets the color on maps for this block to be transparent
     *
     * @param m
     *            the block
     */
    void setBlockMapColorTransparent(Material m);

    default Object createTeamParametersPacketObject(BaseComponent displayName, BaseComponent prefix, BaseComponent suffix, OptionStatus nameTagDisplay, OptionStatus collisionRule, ChatColor color, boolean seeFriendlyInvisibles, boolean allowFriendlyFire) {
        throw new IllegalStateException("not implemented in this version");
    }

    default Class<? extends Object> getNumberFormatClass() {
        throw new IllegalStateException("not implemented in this version");
    }

    default Object getBlankNumberFormatInstance() {
        throw new IllegalStateException("not implemented in this version");
    }

    default CompoundTag getItemStackNbt(ItemStack stack) {
        throw new IllegalStateException("not implemented in this version");
    }

    default CompoundTag getTileEntityNbt(Block block) {
        throw new IllegalStateException("not implemented in this version");
    }
}
