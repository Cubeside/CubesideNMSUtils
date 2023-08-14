package de.cubeside.nmsutils.nbt;

public final class ByteTag extends Tag implements NumericTag {
    private byte value;

    public ByteTag(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public int getValueAsInt() {
        return value;
    }

    @Override
    public long getValueAsLong() {
        return value;
    }

    @Override
    public double getValueAsDouble() {
        return value;
    }

    @Override
    public TagType getType() {
        return TagType.BYTE;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ByteTag o && value == o.value;
    }
}