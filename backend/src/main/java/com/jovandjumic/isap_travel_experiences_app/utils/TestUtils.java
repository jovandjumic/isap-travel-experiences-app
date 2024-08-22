package com.jovandjumic.isap_travel_experiences_app.utils;

import java.lang.reflect.Field;

public class TestUtils {
    public static void setId(Object entity, Long idValue) throws Exception {
        Field idField = entity.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, idValue);
    }
}
