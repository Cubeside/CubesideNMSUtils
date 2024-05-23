package de.cubeside.nmsutils.nbt;

import java.util.UUID;

public interface ListTag extends Cloneable {
    int size();

    boolean isEmpty();

    TagType getElementType();

    CompoundTag getCompound(int index);

    CompoundTag addCompound();

    CompoundTag addCompound(int index);

    CompoundTag setCompound(int index);

    ListTag getList(int index);

    ListTag addList();

    ListTag addList(int index);

    ListTag setList(int index);

    byte getByte(int index);

    byte getByte(int index, byte defaultValue);

    boolean addByte(byte v);

    boolean addByte(int index, byte v);

    boolean setByte(int index, byte v);

    short getShort(int index);

    short getShort(int index, short defaultValue);

    boolean addShort(short v);

    boolean addShort(int index, short v);

    boolean setShort(int index, short v);

    int getInt(int index);

    int getInt(int index, int defaultValue);

    boolean addInt(int v);

    boolean addInt(int index, int v);

    boolean setInt(int index, int v);

    long getLong(int index);

    long getLong(int index, long defaultValue);

    boolean addLong(long v);

    boolean addLong(int index, long v);

    boolean setLong(int index, long v);

    float getFloat(int index);

    float getFloat(int index, float defaultValue);

    boolean addFloat(float v);

    boolean addFloat(int index, float v);

    boolean setFloat(int index, float v);

    double getDouble(int index);

    double getDouble(int index, double defaultValue);

    boolean addDouble(double v);

    boolean addDouble(int index, double v);

    boolean setDouble(int index, double v);

    byte[] getByteArray(int index);

    byte[] getByteArray(int index, byte[] defaultValue);

    boolean addByteArray(byte[] v);

    boolean addByteArray(int index, byte[] v);

    boolean setByteArray(int index, byte[] v);

    int[] getIntArray(int index);

    int[] getIntArray(int index, int[] defaultValue);

    boolean addIntArray(int[] v);

    boolean addIntArray(int index, int[] v);

    boolean setIntArray(int index, int[] v);

    long[] getLongArray(int index);

    long[] getLongArray(int index, long[] defaultValue);

    boolean addLongArray(long[] v);

    boolean addLongArray(int index, long[] v);

    boolean setLongArray(int index, long[] v);

    String getString(int index);

    String getString(int index, String defaultValue);

    boolean addString(String v);

    boolean addString(int index, String v);

    boolean setString(int index, String v);

    UUID getUUID(int index);

    UUID getUUID(int index, UUID defaultValue);

    boolean addUUID(UUID v);

    boolean addUUID(int index, UUID v);

    boolean setUUID(int index, UUID v);

    public void remove(int index);

    public void clear();

    public ListTag clone();
}
