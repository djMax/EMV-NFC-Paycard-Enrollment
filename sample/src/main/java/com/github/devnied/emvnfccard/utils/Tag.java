package com.github.devnied.emvnfccard.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mmetral on 9/17/14.
 */
public enum Tag {
    FciProprietaryTemplate(0xA5, FciTemplate.class),
    DfName(0x84, String.class);

    private byte value;
    private Class<?> valueClass;

    private static final Map<Integer,Tag> lookup
            = new HashMap<Integer, Tag>();

    static {
        for(Tag w : EnumSet.allOf(Tag.class)) {
            lookup.put((int) w.getTag(), w);
        }
    }

    private Tag(int tagNumber, Class<?> valueClass) {
        value = (byte) tagNumber;
        this.valueClass = valueClass;
    }

    public byte getTag() {
        return value;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

    public static Tag valueOf(int code) {
        Tag t = lookup.get(code);
        if (t == null) {
            throw new IllegalArgumentException(String.format("Unknown tag %d", code));
        }
        return t;
    }
}
