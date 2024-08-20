package com.piraxx.tinyurl.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Base58Map {

    private static final Map<String, String> BASE58_MAP;

    static {
        HashMap<String, String> map = new HashMap<>();

        map.put("0", "1");
        map.put("1", "2");
        map.put("2", "3");
        map.put("3", "4");
        map.put("4", "5");
        map.put("5", "6");
        map.put("6", "7");
        map.put("7", "8");
        map.put("8", "9");
        map.put("9", "A");

        map.put("10", "B");
        map.put("11", "C");
        map.put("12", "D");
        map.put("13", "E");
        map.put("14", "F");
        map.put("15", "G");
        map.put("16", "H");
        map.put("17", "J");
        map.put("18", "K");
        map.put("19", "L");

        map.put("20", "M");
        map.put("21", "N");
        map.put("22", "P");
        map.put("23", "Q");
        map.put("24", "R");
        map.put("25", "S");
        map.put("26", "T");
        map.put("27", "U");
        map.put("28", "V");
        map.put("29", "W");

        map.put("30", "X");
        map.put("31", "Y");
        map.put("32", "Z");
        map.put("33", "a");
        map.put("34", "b");
        map.put("35", "c");
        map.put("36", "d");
        map.put("37", "e");
        map.put("38", "f");
        map.put("39", "g");

        map.put("40", "h");
        map.put("41", "i");
        map.put("42", "j");
        map.put("43", "k");
        map.put("44", "m");
        map.put("45", "n");
        map.put("46", "o");
        map.put("47", "p");
        map.put("48", "q");
        map.put("49", "r");

        map.put("50", "s");
        map.put("51", "t");
        map.put("52", "u");
        map.put("53", "v");
        map.put("54", "w");
        map.put("55", "x");
        map.put("56", "y");
        map.put("57", "z");

        BASE58_MAP = Collections.unmodifiableMap(map);
    }


    private Base58Map(){}

    public static Map<String, String> getBase58Map(){
        return BASE58_MAP;
    }
}
