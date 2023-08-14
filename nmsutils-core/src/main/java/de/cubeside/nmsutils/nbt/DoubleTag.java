package de.cubeside.nmsutils.nbt;

public final class DoubleTag extends Tag implements NumericTag {
    private double value;

    public DoubleTag(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int getValueAsInt() {
        return (int) value;
    }

    @Override
    public long getValueAsLong() {
        return (long) value;
    }

    @Override
    public double getValueAsDouble() {
        return value;
    }

    @Override
    public TagType getType() {
        return TagType.DOUBLE;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DoubleTag o && Double.doubleToLongBits(value) == Double.doubleToLongBits(o.value);
    }
}
