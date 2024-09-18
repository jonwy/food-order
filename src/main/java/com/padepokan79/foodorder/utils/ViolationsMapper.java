package com.padepokan79.foodorder.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path.Node;

public class ViolationsMapper {

    public static <T> Map<String, String> map(Set<ConstraintViolation<T>> violations) {
        String field = "";
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<T> violation: violations) {
            for (Node node: violation.getPropertyPath()) {
                field = node.getName();
            }
            errorMap.put(field, violation.getMessage());
        }
        return errorMap;
    }

    public static Map<String, String> map(String key, String value) {
        return Collections.singletonMap(key, value);
    }
    
}
