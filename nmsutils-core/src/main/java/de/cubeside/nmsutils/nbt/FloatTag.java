package de.cubeside.nmsutils.nbt;

public final class FloatTag extends Tag implements NumericTag {
    private float value;

    public FloatTag(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
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
        return TagType.FLOAT;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FloatTag o && Float.floatToIntBits(value) == Float.floatToIntBits(o.value);
    }
}
