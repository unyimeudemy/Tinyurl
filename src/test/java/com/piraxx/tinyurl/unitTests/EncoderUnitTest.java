package com.piraxx.tinyurl.unitTests;

import com.piraxx.tinyurl.utils.Base58Map;
import com.piraxx.tinyurl.utils.Encoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EncoderUnitTest {

    @Test
    public void test_that_getRemainder_returns_correct_value(){
        long id = 2468135791013L;
        ArrayList<Long> expectedRemainders = new ArrayList<>();
        expectedRemainders.add(17L);
        expectedRemainders.add(6L);
        expectedRemainders.add(4L);
        expectedRemainders.add(41L);
        expectedRemainders.add(20L);
        expectedRemainders.add(48L);
        expectedRemainders.add(6L);
        expectedRemainders.add(1L);

        ArrayList<Long> remainder = Encoder.getRemainders(id);
        assertThat(remainder).isEqualTo(expectedRemainders);
    }

    @Test
    public void test_that_mapToBase58Value_correctly_maps_remainders_to_values(){
        long id = 2468135791013L;
        String mappedResult = new Encoder().mapToBase58Value(id);
        assertThat(mappedResult).isEqualTo("27qMi57J");
    }
}
