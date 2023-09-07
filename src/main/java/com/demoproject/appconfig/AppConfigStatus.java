package com.demoproject.appconfig;

import java.util.HashMap;
import java.util.Map;

public enum AppConfigStatus {

    /**
     * @implNote NONE value shall only be used for initialization.
     */
    NONE(-1),
    STOPPED(0),
    IN_PROGRESS(1);

    private final int value;
    private static final Map<Integer, AppConfigStatus> map = new HashMap<>();

    static {
        // populates a map from values because every time
        // the static method values() is called, it initializes a new array...
        for (AppConfigStatus status : AppConfigStatus.values()) {
            map.put(status.value, status);
        }
    }

    AppConfigStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AppConfigStatus from(int value) {
        return map.get(value);
    }
}
