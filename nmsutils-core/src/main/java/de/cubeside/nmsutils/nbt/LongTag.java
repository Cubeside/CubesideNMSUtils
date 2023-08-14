package de.cubeside.nmsutils.nbt;

public final class LongTag extends Tag implements NumericTag {
    private long value;

    public LongTag(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public int getValueAsInt() {
        return (int) value;
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
        return TagType.LONG;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LongTag o && value == o.value;
    }
}
