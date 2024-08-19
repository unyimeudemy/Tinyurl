package com.piraxx.tinyurl.services.ServicesImpl;

import java.util.List;
import java.util.function.ToIntFunction;

public class BloomFilter {
    private final long[] bitArray;
    private final List<ToIntFunction<String>>  hashHouse = new HashHouse().build();

    public BloomFilter() {
        int SIZE = 958506;
        this.bitArray = new long[SIZE];
        HashHouse.setBitArraySize(SIZE);
    }

    public void add(String spamURL){
        for(ToIntFunction<String> function: hashHouse){
            int hashPosition = function.applyAsInt(spamURL);
            bitArray[hashPosition] = 1;
        }
    }

    public boolean mightContain(String unIdentifiedURL){
        long[] bitPositions = new long[2];
        int i = 0;
        for(ToIntFunction<String> function: hashHouse){
            int hashPosition = function.applyAsInt(unIdentifiedURL);
            bitPositions[i] = bitArray[hashPosition];
            i++;
        }
        return (bitPositions[0] == 1 && bitPositions[1] == 1);
    }
}
