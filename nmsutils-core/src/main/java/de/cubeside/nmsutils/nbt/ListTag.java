package de.cubeside.nmsutils.nbt;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ListTag extends Tag {
    private final ArrayList<Tag> value = new ArrayList<>();
    private List<Tag> unmodifiableValue;

    public ListTag() {
    }

    public ListTag(Tag[] value) {
        if (value.length > 0) {
            TagType type = value[0].getType();
            this.value.add(value[0]);
            for (int i = 1; i < value.length; i++) {
                if (value[i].getType() != type) {
                    throw new IllegalArgumentException("All list elements must have the same type.");
                }
                this.value.add(value[i]);
            }
        }
    }

    public int size() {
        return value.size();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public List<Tag> getValue() {
        if (unmodifiableValue == null) {
            unmodifiableValue = Collections.unmodifiableList(value);
        }
        return unmodifiableValue;
    }

    public TagType getListType() {
        return value.isEmpty() ? null : value.get(0).getType();
    }

    public Tag getElement(int i) {
        return value.get(i);
    }

    public Tag setElement(int i, Tag tag) {
        if (tag.getType() != getListType()) {
            throw new IllegalArgumentException("All list elements must have the same type.");
        }
        return value.set(i, tag);
    }

    public void addElement(Tag tag) {
        Preconditions.checkNotNull(tag);
        if (!isEmpty() && tag.getType() != getListType()) {
            throw new IllegalArgumentException("All list elements must have the same type.");
        }
        value.add(tag);
    }

    public void addElement(int i, Tag tag) {
        Preconditions.checkNotNull(tag);
        if (!isEmpty() && (i != 0 || size() != 1) && tag.getType() != getListType()) {
            throw new IllegalArgumentException("All list elements must have the same type.");
        }
        value.add(i, tag);
    }

    public Tag removeElement(int i) {
        return value.remove(i);
    }

    public void clearElements() {
        value.clear();
    }

    @Override
    public TagType getType() {
        return TagType.LIST;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ListTag o && value.equals(o.value);
    }
}
