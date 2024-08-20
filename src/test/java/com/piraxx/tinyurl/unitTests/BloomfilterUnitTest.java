package com.piraxx.tinyurl.unitTests;

import com.piraxx.tinyurl.services.ServicesImpl.BloomFilter;
import com.piraxx.tinyurl.services.ServicesImpl.HashHouse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BloomfilterUnitTest {


    @Test
    public void test_that_added_url_is_present(){
        String url = "http://test_url.com";
        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.add(url);
        assertThat(bloomFilter.mightContain(url)).isTrue();
    }

    @Test
    public void test_nonExisting_url_returns_false(){
        String url = "http://test_url.com";
        BloomFilter bloomFilter = new BloomFilter();
        assertThat(bloomFilter.mightContain(url)).isFalse();
    }

    @Test
    public void test_that_algo_can_store_find_list_of_url(){
        ArrayList<String> urls = new ArrayList<>();
        BloomFilter bloomFilter = new BloomFilter();

        urls.add("https://www.example.com");
        urls.add("https://www.testsite.org");
        urls.add("https://www.myblog.net");
        urls.add("https://www.shopnow.com/products/item123");
        urls.add("https://www.newsportal.co/articles/top-stories");

        for(String url: urls){
            bloomFilter.add(url);
        }

        for(String url: urls){
            assertThat(bloomFilter.mightContain(url)).isTrue();
        }
    }

    @Test
    public void test_that_hash_func_produce_hash_position_less_than_bit_array_size(){
        List<ToIntFunction<String>> hashHouse = new HashHouse().build();
        int SIZE = 958506;
        String url = "http://test_url.com";
        HashHouse.setBitArraySize(SIZE);

        for(ToIntFunction<String> function: hashHouse){
            assertThat(function.applyAsInt(url)).isLessThan(SIZE);
        }
    }
}
