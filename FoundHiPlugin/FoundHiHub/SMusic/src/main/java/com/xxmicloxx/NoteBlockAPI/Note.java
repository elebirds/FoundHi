package com.xxmicloxx.NoteBlockAPI;

public class Note {

    private byte instrument;
    private byte key;

    public Note(byte instrument, byte key) {
        this.instrument = instrument;
        this.key = key;
    }

    public byte getInstrument() {
        return instrument;
    }

    public byte getKey() {
        return key;
    }
}
