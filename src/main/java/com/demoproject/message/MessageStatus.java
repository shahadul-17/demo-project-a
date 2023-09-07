package com.demoproject.message;

import java.util.HashMap;
import java.util.Map;

public enum MessageStatus {

    /**
     * @implNote NONE value shall only be used for initialization.
     */
    NONE('\0'),
    UNPROCESSED('N'),
    SUCCEEDED('S'),
    FAILED('F');

    private final char value;
    private static final Map<Character, MessageStatus> map = new HashMap<>();

    static {
        // populates a map from values because every time
        // the static method values() is called, it initializes a new array...
        for (MessageStatus status : MessageStatus.values()) {
            map.put(status.value, status);
        }
    }

    MessageStatus(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public static MessageStatus from(char value) {
        return map.get(value);
    }
}
