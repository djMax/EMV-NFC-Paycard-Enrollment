package com.github.devnied.emvnfccard.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mmetral on 9/17/14.
 */
public class EmvTemplateResponse {
    private byte templateId;
    private Map<Tag,TLV> tlvs;
    private byte sw1;
    private byte sw2;

    public EmvTemplateResponse(byte[] buf) {
        if (buf.length < 4) {
            throw new IllegalArgumentException("Invalid response.");
        }
        if (buf[1] != buf.length - 4) {
            throw new IllegalArgumentException("Invalid response length.");
        }
        tlvs = new HashMap<Tag, TLV>();
        for (int i = 2; i < buf.length - 2; i++) {
            int tagCode = buf[i];
            byte[] data = new byte[buf[i+1]];
            i += 2;
            for (int j = 0; i < buf.length && j < data.length; j++) {
                data[j] = buf[i++];
            }
            TLV tlv = new TLV((byte) tagCode, data);
            tlvs.put(tlv.getTag(),tlv);
        }
        sw2 = buf[buf.length-1];
        sw1 = buf[buf.length-2];
        templateId = buf[0];
    }

    public byte getTemplateId() {
        return templateId;
    }

    public TLV getTag(Tag t) {
        return tlvs.get(t);
    }

    public Map<Tag,TLV> getTLVs() {
        return tlvs;
    }

    public byte getSw1() {
        return sw1;
    }

    public byte getSw2() {
        return sw2;
    }
}
