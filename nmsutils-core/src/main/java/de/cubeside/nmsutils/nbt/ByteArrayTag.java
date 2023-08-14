package de.cubeside.nmsutils.nbt;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public final class ByteArrayTag extends Tag {
    private byte[] value;

    public ByteArrayTag(byte[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    @Override
    public TagType getType() {
        return TagType.BYTE_ARRAY;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ByteArrayTag o && Arrays.equals(value, o.value);
    }
}