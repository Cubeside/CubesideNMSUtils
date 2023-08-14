package de.cubeside.nmsutils.nbt;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public final class LongArrayTag extends Tag {
    private long[] value;

    public LongArrayTag(long[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    public long[] getValue() {
        return value;
    }

    public void setValue(long[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    @Override
    public TagType getType() {
        return TagType.LONG_ARRAY;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LongArrayTag o && Arrays.equals(value, o.value);
    }
}
