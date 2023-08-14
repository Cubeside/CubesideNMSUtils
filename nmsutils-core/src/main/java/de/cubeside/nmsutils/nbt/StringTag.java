package de.cubeside.nmsutils.nbt;

import com.google.common.base.Preconditions;

public final class StringTag extends Tag {
    private String value;

    public StringTag(String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    @Override
    public TagType getType() {
        return TagType.STRING;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringTag o && value.equals(o.value);
    }
}
