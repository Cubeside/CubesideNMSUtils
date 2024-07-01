package de.cubeside.nmsutils;

import de.cubeside.nmsutils.nbt.CompoundTag;
import de.cubeside.nmsutils.nbt.ListTag;
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

    public default CompoundTag updateBlockEntity(CompoundTag in, int oldDataVersion) {
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

    @Deprecated(forRemoval = true)
    default CompoundTag getTileEntityNbt(Block block) {
        return getBlockEntityNbt(block);
    }

    default CompoundTag getBlockEntityNbt(Block block) {
        throw new IllegalStateException("not implemented in this version");
    }

    default void setBlockEntityNbt(Block block, CompoundTag tag) {
        throw new IllegalStateException("not implemented in this version");
    }

    default CompoundTag createEmptyCompound() {
        throw new IllegalStateException("not implemented in this version");
    }

    default ListTag createEmptyList() {
        throw new IllegalStateException("not implemented in this version");
    }
}
