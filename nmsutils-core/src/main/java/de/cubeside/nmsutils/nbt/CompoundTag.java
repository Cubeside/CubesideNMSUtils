package de.cubeside.nmsutils.nbt;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public final class CompoundTag extends Tag {
    private final Map<String, Tag> value = new HashMap<>();
    private Map<String, Tag> unmodifiableValue;

    public CompoundTag() {
    }

    public CompoundTag(Map<String, Tag> value) {
        for (Entry<String, Tag> e : value.entrySet()) {
            String key = Preconditions.checkNotNull(e.getKey());
            Tag v = Preconditions.checkNotNull(e.getValue());
            this.value.put(key, v);
        }
    }

    public boolean containsKey(String key) {
        return value.containsKey(key);
    }

    public boolean containsKey(String key, TagType type) {
        Tag t = value.get(key);
        return t == null ? false : t.getType() == type;
    }

    public boolean containsNumeric(String key) {
        Tag entry = value.get(key);
        return entry instanceof NumericTag;
    }

    public int getNumericAsInt(String key) {
        Tag entry = value.get(key);
        if (!(entry instanceof NumericTag t)) {
            throw new IllegalArgumentException("Tag is not numeric");
        }
        return t.getValueAsInt();
    }

    public long getNumericAsLong(String key) {
        Tag entry = value.get(key);
        if (!(entry instanceof NumericTag t)) {
            throw new IllegalArgumentException("Tag is not numeric");
        }
        return t.getValueAsLong();
    }

    public double getNumericAsDouble(String key) {
        Tag entry = value.get(key);
        if (!(entry instanceof NumericTag t)) {
            throw new IllegalArgumentException("Tag is not numeric");
        }
        return t.getValueAsDouble();
    }

    public void clear() {
        value.clear();
    }

    public void remove(String string) {
        value.remove(string);
    }

    public Tag get(String name) {
        return value.get(name);
    }

    public CompoundTag getCompound(String name) {
        return (CompoundTag) value.get(name);
    }

    public CompoundTag setCompound(String name) {
        Preconditions.checkNotNull(name);
        CompoundTag tag = new CompoundTag();
        setCompound(name, tag);
        return tag;
    }

    public void setCompound(String name, CompoundTag c) {
        Preconditions.checkNotNull(name);
        value.put(name, c);
    }

    public ListTag getList(String name) {
        return (ListTag) value.get(name);
    }

    public ListTag setList(String name) {
        ListTag tag = new ListTag();
        setList(name, tag);
        return tag;
    }

    public void setList(String name, ListTag l) {
        Preconditions.checkNotNull(name);
        value.put(name, l);
    }

    public byte getByte(String name) {
        ByteTag tag = (ByteTag) value.get(name);
        return tag == null ? 0 : tag.getValue();
    }

    public byte getByte(String name, byte defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof ByteTag t ? t.getValue() : defaultValue;
    }

    public void setByte(String name, byte b) {
        Preconditions.checkNotNull(name);
        value.put(name, new ByteTag(b));
    }

    public short getShort(String name) {
        ShortTag tag = (ShortTag) value.get(name);
        return tag == null ? 0 : tag.getValue();
    }

    public short getShort(String name, short defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof ShortTag t ? t.getValue() : defaultValue;
    }

    public void setShort(String name, short s) {
        Preconditions.checkNotNull(name);
        value.put(name, new ShortTag(s));
    }

    public int getInt(String name) {
        IntTag tag = (IntTag) value.get(name);
        return tag == null ? 0 : tag.getValue();
    }

    public int getInt(String name, int defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof IntTag t ? t.getValue() : defaultValue;
    }

    public void setInt(String name, int v) {
        Preconditions.checkNotNull(name);
        value.put(name, new IntTag(v));
    }

    public long getLong(String name) {
        LongTag tag = (LongTag) value.get(name);
        return tag == null ? 0 : tag.getValue();
    }

    public long getLong(String name, long defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof LongTag t ? t.getValue() : defaultValue;
    }

    public void setLong(String name, long l) {
        Preconditions.checkNotNull(name);
        value.put(name, new LongTag(l));
    }

    public float getFloat(String name) {
        FloatTag tag = (FloatTag) value.get(name);
        return tag == null ? 0 : tag.getValue();
    }

    public float getFloat(String name, float defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof FloatTag t ? t.getValue() : defaultValue;
    }

    public void setFloat(String name, float f) {
        Preconditions.checkNotNull(name);
        value.put(name, new FloatTag(f));
    }

    public double getDouble(String name) {
        DoubleTag tag = (DoubleTag) value.get(name);
        return tag == null ? 0 : tag.getValue();
    }

    public double getDouble(String name, double defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof DoubleTag t ? t.getValue() : defaultValue;
    }

    public void setDouble(String name, double d) {
        Preconditions.checkNotNull(name);
        value.put(name, new DoubleTag(d));
    }

    public byte[] getByteArray(String name) {
        ByteArrayTag tag = (ByteArrayTag) value.get(name);
        return tag == null ? null : tag.getValue();
    }

    public byte[] getByteArray(String name, byte[] defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof ByteArrayTag t ? t.getValue() : defaultValue;
    }

    public void setByteArray(String name, byte[] b) {
        Preconditions.checkNotNull(name);
        value.put(name, new ByteArrayTag(b));
    }

    public int[] getIntArray(String name) {
        IntArrayTag tag = (IntArrayTag) value.get(name);
        return tag == null ? null : tag.getValue();
    }

    public int[] getIntArray(String name, int[] defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof IntArrayTag t ? t.getValue() : defaultValue;
    }

    public void setIntArray(String name, int[] v) {
        Preconditions.checkNotNull(name);
        value.put(name, new IntArrayTag(v));
    }

    public long[] getLongArray(String name) {
        LongArrayTag tag = (LongArrayTag) value.get(name);
        return tag == null ? null : tag.getValue();
    }

    public long[] getLongArray(String name, long[] defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof LongArrayTag t ? t.getValue() : defaultValue;
    }

    public void setLongArray(String name, long[] v) {
        Preconditions.checkNotNull(name);
        value.put(name, new LongArrayTag(v));
    }

    public String getString(String name) {
        StringTag tag = (StringTag) value.get(name);
        return tag == null ? null : tag.getValue();
    }

    public String getString(String name, String defaultValue) {
        Tag tag = value.get(name);
        return tag instanceof StringTag t ? t.getValue() : defaultValue;
    }

    public void setString(String name, String v) {
        Preconditions.checkNotNull(name);
        value.put(name, new StringTag(v));
    }

    public void setUUID(String name, UUID value) {
        long uuidMost = value.getMostSignificantBits();
        long uuidLeast = value.getLeastSignificantBits();
        setIntArray(name, new int[] { (int) (uuidMost >> 32), (int) uuidMost, (int) (uuidLeast >> 32), (int) uuidLeast });
    }

    public UUID getUUID(String name) {
        int[] array = this.getIntArray(name, null);
        if (array == null) {
            if (this.containsNumeric(name + "Most") && this.containsNumeric(name + "Least")) {
                return new UUID(this.getNumericAsLong(name + "Most"), this.getNumericAsLong(name + "Least"));
            }
        } else if (array.length == 4) {
            return new UUID((long) array[0] << 32 | (array[1] & 0xFFFF_FFFFL), (long) array[2] << 32 | (array[3] & 0xFFFF_FFFFL));
        }
        return null;
    }

    public boolean containsUUID(String name) {
        int[] array = this.getIntArray(name, null);
        if (array == null) {
            return this.containsNumeric(name + "Most") && this.containsNumeric(name + "Least");
        } else {
            return array.length == 4;
        }
    }

    public Map<String, Tag> getValue() {
        if (unmodifiableValue == null) {
            unmodifiableValue = Collections.unmodifiableMap(this.value);
        }
        return unmodifiableValue;
    }

    @Override
    public TagType getType() {
        return TagType.COMPOUND;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompoundTag o && value.equals(o.value);
    }
}
