package com.github.devnied.emvnfccard.utils;

import org.apache.commons.io.Charsets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by mmetral on 9/17/14.
 */
public class ApduCommand {
    private ByteArrayOutputStream bytes;
    private byte commandClass = 0;
    private byte instruction;
    private byte p1;
    private byte p2;
    private byte le;
    private boolean emptyData;

    public enum StringInputFormat {
        Hex,
        Ascii;
    };

    public ApduCommand() {
        bytes = new ByteArrayOutputStream();
    }

    public ApduCommand(String str, StringInputFormat format) throws IOException {
        this();
        append(str, format);
    }

    public ApduCommand append(String str, StringInputFormat format) throws IOException {
        switch (format) {
            case Hex:
                int len = str.length();
                if (len % 2 != 0) {
                    throw new IllegalArgumentException("Hex string must be a multiple of 2");
                }
                for (int i = 0; i < len; i+=2) {
                    bytes.write((byte) ((Character.digit(str.charAt(i), 16) << 4)
                            + Character.digit(str.charAt(i + 1), 16)));
                }
                break;
            case Ascii:
                bytes.write(str.getBytes(Charsets.US_ASCII));
                break;
            default:
                throw new IllegalArgumentException("Unknown string input format.");
        }
        return this;
    }

    public ApduCommand appendHex(String hex) throws IOException {
        return this.append(hex, StringInputFormat.Hex);
    }

    public ApduCommand appendAscii(String ascii) throws IOException {
        return this.append(ascii, StringInputFormat.Ascii);
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(commandClass);
        baos.write(instruction);
        baos.write(p1);
        baos.write(p2);
        if (bytes.size() > 256) {
            throw new IllegalArgumentException("Message size is too long.");
        }
        if (!emptyData) {
            baos.write(bytes.size());
            if (bytes.size() > 0) {
                baos.write(bytes.toByteArray());
            }
        }
        baos.write(this.le);
        return baos.toByteArray();
    }


    public byte getCommandClass() {
        return commandClass;
    }

    public ApduCommand CommandClass(int commandClass) {
        this.commandClass = (byte) commandClass;
        return this;
    }

    public byte getInstruction() {
        return instruction;
    }

    public ApduCommand Instruction(int instruction) {
        this.instruction = (byte) instruction;
        return this;
    }

    public byte getP1() {
        return p1;
    }

    public ApduCommand P1(int p1) {
        this.p1 = (byte) p1;
        return this;
    }

    public byte getP2() {
        return p2;
    }

    public ApduCommand P2(int p2) {
        this.p2 = (byte) p2;
        return this;
    }

    public byte getLe() { return le; }

    public ApduCommand Le(int le) {
        this.le = (byte) le;
        return this;
    }

    public boolean isEmptyData() {
        return emptyData;
    }

    public ApduCommand NoData() {
        emptyData = true;
        return this;
    }
}
