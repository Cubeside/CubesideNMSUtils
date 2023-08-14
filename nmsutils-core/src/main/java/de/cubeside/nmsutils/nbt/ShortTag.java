package de.cubeside.nmsutils.nbt;

public final class ShortTag extends Tag implements NumericTag {
    private short value;

    public ShortTag(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
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
        return TagType.SHORT;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ShortTag o && value == o.value;
    }
}
