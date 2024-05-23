package de.cubeside.nmsutils;

import de.cubeside.nmsutils.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface NbtUtils {
    public CompoundTag parseBinary(byte[] in);

    public byte[] writeBinary(CompoundTag in);

    public CompoundTag parseString(String in);

    public String writeString(CompoundTag in);

    @SuppressWarnings("deprecation")
    public default int getCurrentDataVersion() {
        return Bukkit.getUnsafe().getDataVersion();
    }

    public default CompoundTag updateEntity(CompoundTag in, int oldDataVersion) {
        return in;
    }

    public default CompoundTag updateBlockState(CompoundTag in, int oldDataVersion) {
        return in;
    }

    public default CompoundTag updateItem(CompoundTag in, int oldDataVersion) {
        return in;
    }

    public default String updateItemTypeName(String in, int oldDataVersion) {
        return in;
    }

    public default String updateBlockTypeName(String in, int oldDataVersion) {
        return in;
    }

    default CompoundTag getItemStackNbt(ItemStack stack) {
        throw new IllegalStateException("not implemented in this version");
    }

    default ItemStack createItemStack(CompoundTag tag) {
        throw new IllegalStateException("not implemented in this version");
    }

    default CompoundTag getTileEntityNbt(Block block) {
        throw new IllegalStateException("not implemented in this version");
    }
}
