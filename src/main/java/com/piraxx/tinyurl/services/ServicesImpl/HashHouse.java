package com.piraxx.tinyurl.services.ServicesImpl;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;


@Component
public class HashHouse {

    private static int BIT_ARRAY_SIZE ;

    public static void setBitArraySize(int size) {
        BIT_ARRAY_SIZE = size;
    }

    private static class HashFunc_1 implements ToIntFunction<String> {
        @Override
        public int applyAsInt(String value) {
            int hash = Objects.hash(value);
            return Math.abs(hash % BIT_ARRAY_SIZE);
        }
    }

    private static class HashFunc_2 implements ToIntFunction<String> {
        @Override
        public int applyAsInt(String value) {
            int hash = Objects.hash(value);
            return Math.abs((hash / 31) % BIT_ARRAY_SIZE);
        }
    }

    public List<ToIntFunction<String>> build(){
        return Arrays.asList(new HashFunc_1(), new HashFunc_2());
    }

}
