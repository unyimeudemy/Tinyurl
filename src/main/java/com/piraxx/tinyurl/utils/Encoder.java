package com.piraxx.tinyurl.utils;

import java.util.ArrayList;
import java.util.Map;

public class Encoder {

    public String mapToBase58Value(long id){
        ArrayList<Long> remainders = getRemainders(id);
        Map<String, String> map = Base58Map.getBase58Map();
        StringBuilder result = new StringBuilder();

        for(int i=remainders.size()-1; i>=0; i--){
            String key = String.valueOf(remainders.get(i));
            String characterOrDigit = map.get(key);
            result.append(characterOrDigit);
        }
        return result.toString();
    }

    public static ArrayList<Long> getRemainders(long id){
        ArrayList<Long> remainders = new ArrayList<>();
        long curr = id;
        long base = 58L;
        while(curr % base > 0){
            long ans = curr / base;
            long remainder = curr % base;
            curr = ans;
            remainders.add(remainder);
        }
        return remainders;
    }
}
