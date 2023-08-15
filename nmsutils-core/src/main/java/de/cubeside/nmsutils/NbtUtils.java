package de.cubeside.nmsutils;

import de.cubeside.nmsutils.nbt.CompoundTag;

public interface NbtUtils {
    public CompoundTag parseBinary(byte[] in);

    public byte[] writeBinary(CompoundTag in);

    public CompoundTag parseString(String in);

    public String writeString(CompoundTag in);
}
