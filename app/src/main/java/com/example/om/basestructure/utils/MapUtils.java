package com.example.om.basestructure.utils;

import java.util.Map;

/**
 * Created by android on 28.12.17.
 */

public class MapUtils {

    public static boolean isDataChanged(Map<String, Object> origin, Map<String, Object> source) {
        for (String key : origin.keySet()) {
            if (source.containsKey(key) && source.get(key) != null) {
                if (origin.get(key) instanceof String) {
                    if (!origin.get(key).equals(source.get(key))) {
                        return true;
                    }
                } else if (origin.get(key) instanceof Integer) {
                    if (((Integer) origin.get(key)).intValue() != ((Integer) source.get(key)).intValue()) {
                        return true;
                    }
                } else if (origin.get(key) instanceof Map) {
                    if (isDataChanged((Map<String, Object>) origin.get(key), (Map<String, Object>) source.get(key))) {
                        return true;
                    }
                } else if (origin.get(key) instanceof Boolean) {
                    if (!origin.get(key).equals(source.get(key))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    private static boolean processList(List<Object> originList, List<Object> sourceList) {
//        boolean isChanged = false;
//        for (Object o : originList) {
//
//            isChanged = isDataSimpleType(o, sourceList);
//        }
//        return isChanged;
//    }
//
//    private static boolean isDataSimpleType(Object o,) {
//        if (o instanceof String || o instanceof Number) {
//            return true;
//        }
//        return false;
//    }
}
