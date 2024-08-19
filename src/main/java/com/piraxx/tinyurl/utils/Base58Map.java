package com.piraxx.tinyurl.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Base58Map {

    private static final Map<String, String> BASE58_MAP;

    static {
        HashMap<String, String> map = new HashMap<>();

        map.put("0", "0");
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        map.put("6", "6");
        map.put("7", "7");
        map.put("8", "8");
        map.put("9", "9");

        map.put("10", "A");
        map.put("11", "B");
        map.put("12", "C");
        map.put("13", "D");
        map.put("14", "E");
        map.put("15", "F");
        map.put("16", "G");
        map.put("17", "H");
        map.put("18", "J");
        map.put("19", "K");

        map.put("20", "L");
        map.put("21", "M");
        map.put("22", "N");
        map.put("23", "P");
        map.put("24", "Q");
        map.put("25", "R");
        map.put("26", "S");
        map.put("27", "T");
        map.put("28", "U");
        map.put("29", "V");

        map.put("30", "W");
        map.put("31", "X");
        map.put("32", "Y");
        map.put("33", "Z");
        map.put("34", "a");
        map.put("35", "b");
        map.put("36", "c");
        map.put("37", "d");
        map.put("38", "e");
        map.put("39", "f");

        map.put("40", "g");
        map.put("41", "h");
        map.put("42", "i");
        map.put("43", "j");
        map.put("44", "k");
        map.put("45", "m");
        map.put("46", "n");
        map.put("47", "o");
        map.put("48", "p");
        map.put("49", "q");

        map.put("50", "r");
        map.put("51", "s");
        map.put("52", "t");
        map.put("53", "u");
        map.put("54", "v");
        map.put("55", "w");
        map.put("56", "x");
        map.put("57", "y");
        map.put("58", "z");

        BASE58_MAP = Collections.unmodifiableMap(map);
    }


    private Base58Map(){}

    public static Map<String, String> getBase58Map(){
        return BASE58_MAP;
    }
}
