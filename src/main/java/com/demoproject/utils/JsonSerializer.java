package com.demoproject.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonSerializer {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting().create();

    public static String serialize(Object object) {
        return gson.toJson(object);
    }

    public static <Type> Type deserialize(String json, Class<Type> classOfType) {
        return gson.fromJson(json, classOfType);
    }
}
