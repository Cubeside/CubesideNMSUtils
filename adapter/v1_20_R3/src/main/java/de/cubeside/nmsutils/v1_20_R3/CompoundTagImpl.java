package de.cubeside.nmsutils.v1_20_R3;

import com.google.common.base.Preconditions;
import de.cubeside.nmsutils.nbt.TagType;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public final class CompoundTagImpl implements de.cubeside.nmsutils.nbt.CompoundTag {
    final CompoundTag handle;

    public CompoundTagImpl(CompoundTag handle) {
        this.handle = handle;
    }

    @Override
    public boolean containsKey(String name) {
        return handle.contains(name);
    }

    @Override
    public boolean containsKey(String name, TagType type) {
        return handle.contains(name, type.internalId());
    }

    @Override
    public void clear() {
        handle.getAllKeys().clear();
    }

    @Override
    public void remove(String name) {
        handle.remove(name);
    }

    @Override
    public Set<String> getAllKeys() {
        return Collections.unmodifiableSet(handle.getAllKeys());
    }

    @Override
    public void size() {
        handle.size();
    }

    @Override
    public void merge(de.cubeside.nmsutils.nbt.CompoundTag source) {
        handle.merge(((CompoundTagImpl) source).handle);
    }

    @Override
    public CompoundTagImpl getCompound(String name) {
        return getCompound(name, false);
    }

    @Override
    public CompoundTagImpl getCompound(String name, boolean createIfMissing) {
        Preconditions.checkNotNull(name);
        Tag tag = handle.get(name);
        if (!(tag instanceof CompoundTag)) {
            if (!createIfMissing) {
                return null;
            }
            tag = new CompoundTag();
            handle.put(name, tag);
        }
        return new CompoundTagImpl((CompoundTag) tag);
    }

    @Override
    public ListTagImpl getList(String name) {
        return getList(name, false);
    }

    @Override
    public ListTagImpl getList(String name, boolean createIfMissing) {
        Preconditions.checkNotNull(name);
        Tag tag = handle.get(name);
        if (!(tag instanceof ListTag)) {
            if (!createIfMissing) {
                return null;
            }
            tag = new ListTag();
            handle.put(name, tag);
        }
        return new ListTagImpl((ListTag) tag);
    }

    @Override
    public byte getByte(String name) {
        return getByte(name, (byte) 0);
    }

    @Override
    public byte getByte(String name, byte defaultValue) {
        if (handle.get(name) instanceof NumericTag tag) {
            return tag.getAsByte();
        }
        return defaultValue;
    }

    @Override
    public void setByte(String name, byte b) {
        Preconditions.checkNotNull(name);
        handle.putByte(name, b);
    }

    @Override
    public short getShort(String name) {
        return getShort(name, (short) 0);
    }

    @Override
    public short getShort(String name, short defaultValue) {
        if (handle.get(name) instanceof NumericTag tag) {
            return tag.getAsShort();
        }
        return defaultValue;
    }

    @Override
    public void setShort(String name, short s) {
        Preconditions.checkNotNull(name);
        handle.putShort(name, s);
    }

    @Override
    public int getInt(String name) {
        return getInt(name, 0);
    }

    @Override
    public int getInt(String name, int defaultValue) {
        if (handle.get(name) instanceof NumericTag tag) {
            return tag.getAsInt();
        }
        return defaultValue;
    }

    @Override
    public void setInt(String name, int v) {
        Preconditions.checkNotNull(name);
        handle.putInt(name, v);
    }

    @Override
    public long getLong(String name) {
        return getLong(name, 0L);
    }

    @Override
    public long getLong(String name, long defaultValue) {
        if (handle.get(name) instanceof NumericTag tag) {
            return tag.getAsLong();
        }
        return defaultValue;
    }

    @Override
    public void setLong(String name, long l) {
        Preconditions.checkNotNull(name);
        handle.putLong(name, l);
    }

    @Override
    public float getFloat(String name) {
        return getFloat(name, 0.0f);
    }

    @Override
    public float getFloat(String name, float defaultValue) {
        if (handle.get(name) instanceof NumericTag tag) {
            return tag.getAsFloat();
        }
        return defaultValue;
    }

    @Override
    public void setFloat(String name, float f) {
        Preconditions.checkNotNull(name);
        handle.putFloat(name, f);
    }

    @Override
    public double getDouble(String name) {
        return getDouble(name, 0.0);
    }

    @Override
    public double getDouble(String name, double defaultValue) {
        if (handle.get(name) instanceof NumericTag tag) {
            return tag.getAsDouble();
        }
        return defaultValue;
    }

    @Override
    public void setDouble(String name, double d) {
        Preconditions.checkNotNull(name);
        handle.putDouble(name, d);
    }

    @Override
    public byte[] getByteArray(String name) {
        return getByteArray(name, null);
    }

    @Override
    public byte[] getByteArray(String name, byte[] defaultValue) {
        if (handle.get(name) instanceof ByteArrayTag tag) {
            return tag.getAsByteArray();
        }
        return defaultValue;
    }

    @Override
    public void setByteArray(String name, byte[] b) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(b);
        handle.putByteArray(name, b);
    }

    @Override
    public int[] getIntArray(String name) {
        return getIntArray(name, null);
    }

    @Override
    public int[] getIntArray(String name, int[] defaultValue) {
        if (handle.get(name) instanceof IntArrayTag tag) {
            return tag.getAsIntArray();
        }
        return defaultValue;
    }

    @Override
    public void setIntArray(String name, int[] v) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(v);
        handle.putIntArray(name, v);
    }

    @Override
    public long[] getLongArray(String name) {
        return getLongArray(name, null);
    }

    @Override
    public long[] getLongArray(String name, long[] defaultValue) {
        if (handle.get(name) instanceof LongArrayTag tag) {
            return tag.getAsLongArray();
        }
        return defaultValue;
    }

    @Override
    public void setLongArray(String name, long[] v) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(v);
        handle.putLongArray(name, v);
    }

    @Override
    public String getString(String name) {
        return getString(name, null);
    }

    @Override
    public String getString(String name, String defaultValue) {
        if (handle.get(name) instanceof StringTag tag) {
            return tag.getAsString();
        }
        return defaultValue;
    }

    @Override
    public void setString(String name, String v) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(v);
        handle.putString(name, v);
    }

    @Override
    public UUID getUUID(String name) {
        return handle.hasUUID(name) ? handle.getUUID(name) : null;
    }

    @Override
    public boolean containsUUID(String name) {
        return handle.hasUUID(name);
    }

    @Override
    public void setUUID(String name, UUID v) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(v);
        handle.putUUID(name, v);
    }

    @Override
    public int hashCode() {
        return handle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompoundTagImpl o && handle.equals(o.handle);
    }

    @Override
    public CompoundTagImpl clone() {
        return new CompoundTagImpl(handle.copy());
    }
}
