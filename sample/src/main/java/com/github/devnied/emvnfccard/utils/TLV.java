package com.github.devnied.emvnfccard.utils;

import org.apache.commons.io.Charsets;

/**
 * Created by mmetral on 9/17/14.
 */
public class TLV {
    private Tag tag;
    private Object value;

    public TLV(byte tag, byte[] content) {
        this.tag = Tag.valueOf(tag);
        if (this.tag.getValueClass() == String.class) {
            this.value = new String(content, Charsets.US_ASCII);
        } else if (this.tag.getValueClass() == FciTemplate.class) {
            // TODO make this generic
        }
    }

    public Tag getTag() {
        return tag;
    }

    public Object getValue() {
        return value;
    }
}
