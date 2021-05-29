package com.example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

public class TypeReferenceKest {
    public static void main(String[] args) {
        IntMap intMap = new IntMap();

        System.out.println(intMap.getClass().getSuperclass()); //获取父类类型

        Type type = intMap.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            for (Type t : p.getActualTypeArguments()) {
                System.out.println(t);
            }
        }

        System.out.println("=====newclass=====");
        HashMap<String, Integer> newIntMap = new HashMap<>();

        System.out.println(newIntMap.getClass().getSuperclass());

        Type newClassType = newIntMap.getClass().getGenericSuperclass();
        if (newClassType instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) newClassType;
            for (Type t : p.getActualTypeArguments()) {
                System.out.println(t);
            }
        }

        System.out.println("=====subclass=====");
        HashMap<String, Integer> subIntMap = new HashMap<String, Integer>() {
        };

        System.out.println(subIntMap.getClass().getSuperclass());

        Type subClassType = subIntMap.getClass().getGenericSuperclass();
        if (subClassType instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) subClassType;
            for (Type t : p.getActualTypeArguments()) {
                System.out.println(t);
            }
        }
    }


    public static class IntMap extends HashMap<String, Integer> {
    }
}
