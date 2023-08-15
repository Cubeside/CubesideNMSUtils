package de.cubeside.nmsutils.nbt;

public enum TagType {
    BYTE(1),
    SHORT(2),
    INT(3),
    LONG(4),
    FLOAT(5),
    DOUBLE(6),
    BYTE_ARRAY(7),
    STRING(8),
    LIST(9),
    COMPOUND(10),
    INT_ARRAY(11),
    LONG_ARRAY(12),
    ANY_NUMERIC(99),
    UUID(-1);

    private final int internalId;
    private static final TagType[] byInternal = new TagType[12];
    static {
        for (TagType t : values()) {
            if (t.internalId >= 0 && t.internalId < byInternal.length) {
                byInternal[t.internalId] = t;
            }
        }
    }

    TagType(int internalId) {
        this.internalId = internalId;
    }

    public int internalId() {
        return internalId;
    }

    public static TagType ofInternal(int id) {
        return id >= 0 && id < byInternal.length ? byInternal[id] : null;
    }
}
