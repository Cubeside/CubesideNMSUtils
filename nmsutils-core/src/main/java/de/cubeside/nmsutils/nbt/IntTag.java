package de.cubeside.nmsutils.nbt;

public final class IntTag extends Tag implements NumericTag {
    private int value;

    public IntTag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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
        return TagType.INT;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntTag o && value == o.value;
    }
}
