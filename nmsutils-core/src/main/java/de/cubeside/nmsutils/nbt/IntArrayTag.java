package de.cubeside.nmsutils.nbt;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public final class IntArrayTag extends Tag {
    private int[] value;

    public IntArrayTag(int[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    public int[] getValue() {
        return value;
    }

    public void setValue(int[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    @Override
    public TagType getType() {
        return TagType.INT_ARRAY;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntArrayTag o && Arrays.equals(value, o.value);
    }
}
