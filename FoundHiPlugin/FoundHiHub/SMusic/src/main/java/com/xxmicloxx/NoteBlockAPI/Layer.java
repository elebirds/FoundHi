package com.xxmicloxx.NoteBlockAPI;

import java.util.HashMap;

public class Layer {
    private HashMap<Integer, Note> hashMap = new HashMap<>();

    public Note getNote(int tick) {
        return hashMap.get(tick);
    }

    public void setNote(int tick, Note note) {
        hashMap.put(tick, note);
    }
}
