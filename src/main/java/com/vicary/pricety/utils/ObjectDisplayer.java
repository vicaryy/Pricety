package com.vicary.pricety.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectDisplayer {
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void display(Object ob) {
        System.out.println(gson.toJson(ob));
    }

    public static String getString(Object ob) {
        return gson.toJson(ob);
    }
}
