package de.cubeside.nmsutils.paper1_21_11;

import com.google.common.base.Preconditions;
import de.cubeside.nmsutils.nbt.TagType;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryOps;
import org.bukkit.craftbukkit.CraftRegistry;

public final class ListTagImpl implements de.cubeside.nmsutils.nbt.ListTag {
    final ListTag handle;

    public ListTagImpl(ListTag handle) {
        this.handle = handle;
    }

    @Override
    public int size() {
        return handle.size();
    }

    @Override
    public boolean isEmpty() {
        return handle.isEmpty();
    }

    @Override
    public TagType getElementType() {
        return TagType.ofInternal(handle.identifyRawElementType());
    }

    @Override
    public CompoundTagImpl getCompound(int index) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof CompoundTag t) {
            return new CompoundTagImpl(t);
        }
        return null;
    }

    @Override
    public CompoundTagImpl addCompound() {
        return addCompound(size());
    }

    @Override
    public CompoundTagImpl addCompound(int index) {
        CompoundTag tag = new CompoundTag();
        return handle.addTag(index, tag) ? new CompoundTagImpl(tag) : null;
    }

    @Override
    public CompoundTagImpl setCompound(int index) {
        CompoundTag tag = new CompoundTag();
        return handle.setTag(index, tag) ? new CompoundTagImpl(tag) : null;
    }

    @Override
    public ListTagImpl getList(int index) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof ListTag t) {
            return new ListTagImpl(t);
        }
        return null;
    }

    @Override
    public ListTagImpl addList() {
        return addList(size());
    }

    @Override
    public ListTagImpl addList(int index) {
        ListTag tag = new ListTag();
        return handle.addTag(index, tag) ? new ListTagImpl(tag) : null;
    }

    @Override
    public ListTagImpl setList(int index) {
        ListTag tag = new ListTag();
        return handle.setTag(index, tag) ? new ListTagImpl(tag) : null;
    }

    @Override
    public byte getByte(int index) {
        return getByte(index, (byte) 0);
    }

    @Override
    public byte getByte(int index, byte defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof NumericTag t) {
            return t.byteValue();
        }
        return defaultValue;
    }

    @Override
    public boolean addByte(byte v) {
        return addByte(size(), v);
    }

    @Override
    public boolean addByte(int index, byte v) {
        return handle.addTag(index, ByteTag.valueOf(v));
    }

    @Override
    public boolean setByte(int index, byte v) {
        return handle.setTag(index, ByteTag.valueOf(v));
    }

    @Override
    public short getShort(int index) {
        return getShort(index, (short) 0);
    }

    @Override
    public short getShort(int index, short defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof NumericTag t) {
            return t.shortValue();
        }
        return defaultValue;
    }

    @Override
    public boolean addShort(short v) {
        return addShort(size(), v);
    }

    @Override
    public boolean addShort(int index, short v) {
        return handle.addTag(index, ShortTag.valueOf(v));
    }

    @Override
    public boolean setShort(int index, short v) {
        return handle.setTag(index, ShortTag.valueOf(v));
    }

    @Override
    public int getInt(int index) {
        return getInt(index, 0);
    }

    @Override
    public int getInt(int index, int defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof NumericTag t) {
            return t.intValue();
        }
        return defaultValue;
    }

    @Override
    public boolean addInt(int v) {
        return addInt(size(), v);
    }

    @Override
    public boolean addInt(int index, int v) {
        return handle.addTag(index, IntTag.valueOf(v));
    }

    @Override
    public boolean setInt(int index, int v) {
        return handle.setTag(index, IntTag.valueOf(v));
    }

    @Override
    public long getLong(int index) {
        return getLong(index, 0);
    }

    @Override
    public long getLong(int index, long defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof NumericTag t) {
            return t.longValue();
        }
        return defaultValue;
    }

    @Override
    public boolean addLong(long v) {
        return addLong(size(), v);
    }

    @Override
    public boolean addLong(int index, long v) {
        return handle.addTag(index, LongTag.valueOf(v));
    }

    @Override
    public boolean setLong(int index, long v) {
        return handle.setTag(index, LongTag.valueOf(v));
    }

    @Override
    public float getFloat(int index) {
        return getFloat(index, 0);
    }

    @Override
    public float getFloat(int index, float defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof NumericTag t) {
            return t.floatValue();
        }
        return defaultValue;
    }

    @Override
    public boolean addFloat(float v) {
        return addFloat(size(), v);
    }

    @Override
    public boolean addFloat(int index, float v) {
        return handle.addTag(index, FloatTag.valueOf(v));
    }

    @Override
    public boolean setFloat(int index, float v) {
        return handle.setTag(index, FloatTag.valueOf(v));
    }

    @Override
    public double getDouble(int index) {
        return getDouble(index, 0);
    }

    @Override
    public double getDouble(int index, double defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof NumericTag t) {
            return t.doubleValue();
        }
        return defaultValue;
    }

    @Override
    public boolean addDouble(double v) {
        return addDouble(size(), v);
    }

    @Override
    public boolean addDouble(int index, double v) {
        return handle.addTag(index, DoubleTag.valueOf(v));
    }

    @Override
    public boolean setDouble(int index, double v) {
        return handle.setTag(index, DoubleTag.valueOf(v));
    }

    @Override
    public byte[] getByteArray(int index) {
        return getByteArray(index, null);
    }

    @Override
    public byte[] getByteArray(int index, byte[] defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof ByteArrayTag t) {
            return t.getAsByteArray();
        }
        return defaultValue;
    }

    @Override
    public boolean addByteArray(byte[] v) {
        return addByteArray(size(), v);
    }

    @Override
    public boolean addByteArray(int index, byte[] v) {
        return handle.addTag(index, new ByteArrayTag(v));
    }

    @Override
    public boolean setByteArray(int index, byte[] v) {
        return handle.setTag(index, new ByteArrayTag(v));
    }

    @Override
    public int[] getIntArray(int index) {
        return getIntArray(index, null);
    }

    @Override
    public int[] getIntArray(int index, int[] defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof IntArrayTag t) {
            return t.getAsIntArray();
        }
        return defaultValue;
    }

    @Override
    public boolean addIntArray(int[] v) {
        return addIntArray(size(), v);
    }

    @Override
    public boolean addIntArray(int index, int[] v) {
        return handle.addTag(index, new IntArrayTag(v));
    }

    @Override
    public boolean setIntArray(int index, int[] v) {
        return handle.setTag(index, new IntArrayTag(v));
    }

    @Override
    public long[] getLongArray(int index) {
        return getLongArray(index, null);
    }

    @Override
    public long[] getLongArray(int index, long[] defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof LongArrayTag t) {
            return t.getAsLongArray();
        }
        return defaultValue;
    }

    @Override
    public boolean addLongArray(long[] v) {
        return addLongArray(size(), v);
    }

    @Override
    public boolean addLongArray(int index, long[] v) {
        return handle.addTag(index, new LongArrayTag(v));
    }

    @Override
    public boolean setLongArray(int index, long[] v) {
        return handle.setTag(index, new LongArrayTag(v));
    }

    @Override
    public String getString(int index) {
        return getString(index, null);
    }

    @Override
    public String getString(int index, String defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof StringTag t) {
            return t.value();
        }
        return defaultValue;
    }

    @Override
    public boolean addString(String v) {
        return addString(size(), v);
    }

    @Override
    public boolean addString(int index, String v) {
        return handle.addTag(index, StringTag.valueOf(v));
    }

    @Override
    public boolean setString(int index, String v) {
        return handle.setTag(index, StringTag.valueOf(v));
    }

    @Override
    public UUID getUUID(int index) {
        return getUUID(index, null);
    }

    @Override
    public UUID getUUID(int index, UUID defaultValue) {
        if (index >= 0 && index < handle.size() && handle.get(index) instanceof IntArrayTag t && t.size() == 4) {
            return UUIDUtil.CODEC.decode(NbtOps.INSTANCE, handle.get(index)).getOrThrow().getFirst();
        }
        return defaultValue;
    }

    @Override
    public boolean addUUID(UUID v) {
        return addUUID(size(), v);
    }

    @Override
    public boolean addUUID(int index, UUID v) {
        if (!isEmpty() && getElementType() != TagType.INT_ARRAY) {
            return false;
        }
        handle.add(index, UUIDUtil.CODEC.encodeStart(NbtOps.INSTANCE, v).getOrThrow());
        return true;
    }

    @Override
    public boolean setUUID(int index, UUID v) {
        if (!isEmpty() && getElementType() != TagType.INT_ARRAY) {
            return false;
        }
        handle.set(index, UUIDUtil.CODEC.encodeStart(NbtOps.INSTANCE, v).getOrThrow());
        return true;
    }

    @Override
    public Component getTextComponent(int index) {
        return getTextComponent(index, null);
    }

    @Override
    public Component getTextComponent(int index, Component defaultValue) {
        if (index >= 0 && index < handle.size()) {
            Tag tag = handle.get(index);
            if (tag == null) {
                return defaultValue;
            }
            try {
                net.minecraft.network.chat.Component component = ComponentSerialization.CODEC.parse(
                        CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), tag).getOrThrow();
                return PaperAdventure.asAdventure(component);
            } catch (Exception e) {
                return defaultValue; // not parseable as component
            }
        }
        return defaultValue;
    }

    @Override
    public boolean setTextComponent(int index, Component value) {
        Preconditions.checkNotNull(value);
        net.minecraft.network.chat.Component component = PaperAdventure.asVanilla(value);
        RegistryOps<Tag> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE);
        Tag result = ComponentSerialization.CODEC.encodeStart(ops, component).getOrThrow();
        handle.set(index, result);
        return true;
    }

    @Override
    public void remove(int i) {
        handle.remove(i);
    }

    @Override
    public void clear() {
        handle.clear();
    }

    @Override
    public int hashCode() {
        return handle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ListTagImpl o && handle.equals(o.handle);
    }

    @Override
    public ListTagImpl clone() {
        return new ListTagImpl(handle.copy());
    }
}
