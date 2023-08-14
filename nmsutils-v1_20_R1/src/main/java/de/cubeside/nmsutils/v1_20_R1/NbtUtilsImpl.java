package de.cubeside.nmsutils.v1_20_R1;

import de.cubeside.nmsutils.nbt.ByteArrayTag;
import de.cubeside.nmsutils.nbt.ByteTag;
import de.cubeside.nmsutils.nbt.CompoundTag;
import de.cubeside.nmsutils.nbt.DoubleTag;
import de.cubeside.nmsutils.nbt.FloatTag;
import de.cubeside.nmsutils.nbt.IntArrayTag;
import de.cubeside.nmsutils.nbt.IntTag;
import de.cubeside.nmsutils.nbt.ListTag;
import de.cubeside.nmsutils.nbt.LongArrayTag;
import de.cubeside.nmsutils.nbt.LongTag;
import de.cubeside.nmsutils.nbt.NbtUtils;
import de.cubeside.nmsutils.nbt.ShortTag;
import de.cubeside.nmsutils.nbt.StringTag;
import de.cubeside.nmsutils.nbt.Tag;
import de.cubeside.nmsutils.nbt.TagType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NbtUtilsImpl implements NbtUtils {
    // private final NMSUtilsImpl nmsUtils;

    public NbtUtilsImpl(NMSUtilsImpl nmsUtils) {
        // this.nmsUtils = nmsUtils;
    }

    @Override
    public CompoundTag parseBinary(byte[] in) {
        try {
            return fromNativeCompound(net.minecraft.nbt.NbtIo.read(new DataInputStream(new ByteArrayInputStream(in))));
        } catch (IOException e) {
            throw new RuntimeException("invalid nbt");
        }
    }

    @Override
    public byte[] writeBinary(CompoundTag in) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            net.minecraft.nbt.NbtIo.write(toNativeCompound(in), new DataOutputStream(os));
        } catch (IOException e) {
            throw new RuntimeException("could not serialize nbt");
        }
        return os.toByteArray();
    }

    @Override
    public CompoundTag parseString(String in) {
        try {
            return fromNativeCompound(net.minecraft.nbt.NbtUtils.snbtToStructure(in));
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse snbt string", e);
        }
    }

    @Override
    public String writeString(CompoundTag in) {
        return net.minecraft.nbt.NbtUtils.structureToSnbt(toNativeCompound(in));
    }

    net.minecraft.nbt.CompoundTag toNativeCompound(CompoundTag tag) {
        net.minecraft.nbt.CompoundTag result = new net.minecraft.nbt.CompoundTag();
        for (Map.Entry<String, Tag> e : tag.getValue().entrySet()) {
            net.minecraft.nbt.Tag nativeTag;
            Tag t = e.getValue();
            switch (t.getType()) {
                case BYTE:
                    nativeTag = net.minecraft.nbt.ByteTag.valueOf(((ByteTag) t).getValue());
                    break;
                case BYTE_ARRAY:
                    nativeTag = new net.minecraft.nbt.ByteArrayTag(((ByteArrayTag) t).getValue());
                    break;
                case COMPOUND:
                    nativeTag = toNativeCompound((CompoundTag) t);
                    break;
                case DOUBLE:
                    nativeTag = net.minecraft.nbt.DoubleTag.valueOf(((DoubleTag) t).getValue());
                    break;
                case FLOAT:
                    nativeTag = net.minecraft.nbt.FloatTag.valueOf(((FloatTag) t).getValue());
                    break;
                case INT:
                    nativeTag = net.minecraft.nbt.IntTag.valueOf(((IntTag) t).getValue());
                    break;
                case INT_ARRAY:
                    nativeTag = new net.minecraft.nbt.IntArrayTag(((IntArrayTag) t).getValue());
                    break;
                case LIST:
                    nativeTag = toNativeList((ListTag) t);
                    break;
                case LONG:
                    nativeTag = net.minecraft.nbt.LongTag.valueOf(((LongTag) t).getValue());
                    break;
                case SHORT:
                    nativeTag = net.minecraft.nbt.ShortTag.valueOf(((ShortTag) t).getValue());
                    break;
                case STRING:
                    nativeTag = net.minecraft.nbt.StringTag.valueOf(((StringTag) t).getValue());
                    break;
                case LONG_ARRAY:
                    nativeTag = new net.minecraft.nbt.LongArrayTag(((LongArrayTag) t).getValue());
                    break;
                default:
                    throw new IllegalArgumentException("impossible?");
            }
            result.put(e.getKey(), nativeTag);
        }
        return result;
    }

    net.minecraft.nbt.ListTag toNativeList(ListTag tag) {
        net.minecraft.nbt.ListTag result = new net.minecraft.nbt.ListTag();
        switch (tag.getListType()) {
            case BYTE:
                for (Tag t : tag.getValue()) {
                    result.add(net.minecraft.nbt.ByteTag.valueOf(((ByteTag) t).getValue()));
                }
                break;
            case BYTE_ARRAY:
                for (Tag t : tag.getValue()) {
                    result.add(new net.minecraft.nbt.ByteArrayTag(((ByteArrayTag) t).getValue()));
                }
                break;
            case COMPOUND:
                for (Tag t : tag.getValue()) {
                    result.add(toNativeCompound((CompoundTag) t));
                }
                break;
            case DOUBLE:
                for (Tag t : tag.getValue()) {
                    result.add(net.minecraft.nbt.DoubleTag.valueOf(((DoubleTag) t).getValue()));
                }
                break;
            case FLOAT:
                for (Tag t : tag.getValue()) {
                    result.add(net.minecraft.nbt.FloatTag.valueOf(((FloatTag) t).getValue()));
                }
                break;
            case INT:
                for (Tag t : tag.getValue()) {
                    result.add(net.minecraft.nbt.IntTag.valueOf(((IntTag) t).getValue()));
                }
                break;
            case INT_ARRAY:
                for (Tag t : tag.getValue()) {
                    result.add(new net.minecraft.nbt.IntArrayTag(((IntArrayTag) t).getValue()));
                }
                break;
            case LIST:
                for (Tag t : tag.getValue()) {
                    result.add(toNativeList((ListTag) t));
                }
            case LONG:
                for (Tag t : tag.getValue()) {
                    result.add(net.minecraft.nbt.LongTag.valueOf(((LongTag) t).getValue()));
                }
                break;
            case SHORT:
                for (Tag t : tag.getValue()) {
                    result.add(net.minecraft.nbt.ShortTag.valueOf(((ShortTag) t).getValue()));
                }
                break;
            case STRING:
                for (Tag t : tag.getValue()) {
                    result.add(net.minecraft.nbt.StringTag.valueOf(((StringTag) t).getValue()));
                }
                break;
            case LONG_ARRAY:
                for (Tag t : tag.getValue()) {
                    result.add(new net.minecraft.nbt.LongArrayTag(((LongArrayTag) t).getValue()));
                }
                break;
        }
        return result;
    }

    CompoundTag fromNativeCompound(net.minecraft.nbt.CompoundTag tag) {
        Map<String, Tag> result = new HashMap<>();
        for (String name : tag.getAllKeys()) {
            net.minecraft.nbt.Tag nativeTag = tag.get(name);
            switch (TagType.values()[nativeTag.getId()]) {
                case BYTE:
                    result.put(name, new ByteTag(((net.minecraft.nbt.ByteTag) nativeTag).getAsByte()));
                    break;
                case BYTE_ARRAY:
                    result.put(name, new ByteArrayTag(((net.minecraft.nbt.ByteArrayTag) nativeTag).getAsByteArray()));
                    break;
                case COMPOUND:
                    result.put(name, fromNativeCompound((net.minecraft.nbt.CompoundTag) nativeTag));
                    break;
                case DOUBLE:
                    result.put(name, new DoubleTag(((net.minecraft.nbt.DoubleTag) nativeTag).getAsDouble()));
                    break;
                case FLOAT:
                    result.put(name, new FloatTag(((net.minecraft.nbt.FloatTag) nativeTag).getAsFloat()));
                    break;
                case INT:
                    result.put(name, new IntTag(((net.minecraft.nbt.IntTag) nativeTag).getAsInt()));
                    break;
                case INT_ARRAY:
                    result.put(name, new IntArrayTag(((net.minecraft.nbt.IntArrayTag) nativeTag).getAsIntArray()));
                    break;
                case LIST:
                    result.put(name, fromNativeList((net.minecraft.nbt.ListTag) nativeTag));
                    break;
                case LONG:
                    result.put(name, new LongTag(((net.minecraft.nbt.LongTag) nativeTag).getAsLong()));
                    break;
                case SHORT:
                    result.put(name, new ShortTag(((net.minecraft.nbt.ShortTag) nativeTag).getAsShort()));
                    break;
                case STRING:
                    result.put(name, new StringTag(((net.minecraft.nbt.StringTag) nativeTag).getAsString()));
                    break;
                case LONG_ARRAY:
                    result.put(name, new LongArrayTag(((net.minecraft.nbt.LongArrayTag) nativeTag).getAsLongArray()));
                    break;
            }
        }
        return new CompoundTag(result);
    }

    ListTag fromNativeList(net.minecraft.nbt.ListTag tag) {
        ListTag result = new ListTag();
        TagType type = TagType.values()[tag.getElementType()];
        switch (type) {
            case BYTE:
                tag.forEach((t) -> result.addElement(new ByteTag(((net.minecraft.nbt.ByteTag) t).getAsByte())));
                break;
            case BYTE_ARRAY:
                tag.forEach((t) -> result.addElement(new ByteArrayTag(((net.minecraft.nbt.ByteArrayTag) t).getAsByteArray())));
                break;
            case COMPOUND:
                tag.forEach((t) -> result.addElement(fromNativeCompound((net.minecraft.nbt.CompoundTag) t)));
                break;
            case DOUBLE:
                tag.forEach((t) -> result.addElement(new DoubleTag(((net.minecraft.nbt.DoubleTag) t).getAsDouble())));
                break;
            case FLOAT:
                tag.forEach((t) -> result.addElement(new FloatTag(((net.minecraft.nbt.FloatTag) t).getAsFloat())));
                break;
            case INT:
                tag.forEach((t) -> result.addElement(new IntTag(((net.minecraft.nbt.IntTag) t).getAsInt())));
                break;
            case INT_ARRAY:
                tag.forEach((t) -> result.addElement(new IntArrayTag(((net.minecraft.nbt.IntArrayTag) t).getAsIntArray())));
                break;
            case LIST:
                tag.forEach((t) -> result.addElement(fromNativeList((net.minecraft.nbt.ListTag) t)));
                break;
            case LONG:
                tag.forEach((t) -> result.addElement(new LongTag(((net.minecraft.nbt.LongTag) t).getAsLong())));
                break;
            case SHORT:
                tag.forEach((t) -> result.addElement(new ShortTag(((net.minecraft.nbt.ShortTag) t).getAsShort())));
                break;
            case STRING:
                tag.forEach((t) -> result.addElement(new StringTag(((net.minecraft.nbt.StringTag) t).getAsString())));
                break;
            case LONG_ARRAY:
                tag.forEach((t) -> result.addElement(new LongArrayTag(((net.minecraft.nbt.LongArrayTag) t).getAsLongArray())));
                break;
        }
        return result;
    }
}
