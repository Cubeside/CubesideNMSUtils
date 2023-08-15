package de.cubeside.nmsutils.nbt;

import java.util.Set;
import java.util.UUID;

public interface CompoundTag {

    boolean containsKey(String key);

    boolean containsKey(String key, TagType type);

    void clear();

    void remove(String string);

    Set<String> getAllKeys();

    void size();

    CompoundTag getCompound(String name);

    CompoundTag getCompound(String name, boolean createIfMissing);

    ListTag getList(String name);

    ListTag getList(String name, boolean createIfMissing);

    byte getByte(String name);

    byte getByte(String name, byte defaultValue);

    void setByte(String name, byte b);

    short getShort(String name);

    short getShort(String name, short defaultValue);

    void setShort(String name, short s);

    int getInt(String name);

    int getInt(String name, int defaultValue);

    void setInt(String name, int v);

    long getLong(String name);

    long getLong(String name, long defaultValue);

    void setLong(String name, long l);

    float getFloat(String name);

    float getFloat(String name, float defaultValue);

    void setFloat(String name, float f);

    double getDouble(String name);

    double getDouble(String name, double defaultValue);

    void setDouble(String name, double d);

    byte[] getByteArray(String name);

    byte[] getByteArray(String name, byte[] defaultValue);

    void setByteArray(String name, byte[] b);

    int[] getIntArray(String name);

    int[] getIntArray(String name, int[] defaultValue);

    void setIntArray(String name, int[] v);

    long[] getLongArray(String name);

    long[] getLongArray(String name, long[] defaultValue);

    void setLongArray(String name, long[] v);

    String getString(String name);

    String getString(String name, String defaultValue);

    void setString(String name, String v);

    UUID getUUID(String name);

    void setUUID(String name, UUID value);

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);

}