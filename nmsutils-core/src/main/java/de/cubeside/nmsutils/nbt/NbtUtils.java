package de.cubeside.nmsutils.nbt;

public interface NbtUtils {
    public CompoundTag parseBinary(byte[] in);

    public byte[] writeBinary(CompoundTag in);

    public CompoundTag parseString(String in);

    public String writeString(CompoundTag in);
}
