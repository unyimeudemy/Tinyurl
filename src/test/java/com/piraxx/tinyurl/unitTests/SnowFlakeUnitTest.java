package com.piraxx.tinyurl.unitTests;

import com.piraxx.tinyurl.utils.SnowflakeIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SnowFlakeUnitTest {


    @Test
    public void testConstructorValidIds() {
        // Maximum workerId and datacenterId are 32. Beyond this is invalid
        assertDoesNotThrow(() -> new SnowflakeIdGenerator(1, 1));
        assertDoesNotThrow(() -> new SnowflakeIdGenerator(0, 0));
        assertDoesNotThrow(() -> new SnowflakeIdGenerator(31, 31));
    }

    @Test
    public void testConstructorInvalidWorkerId() {
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdGenerator(32, 0));
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdGenerator(-1, 0));
    }

    @Test
    public void testConstructorInvalidDatacenterId() {
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdGenerator(0, 32));
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdGenerator(0, -1));
    }

    @Test
    public void testNextIdUniqueness() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);
        long id1 = generator.nextId();
        long id2 = generator.nextId();
        assertNotEquals(id1, id2);
    }

    @Test
    public void testNextIdOrdering() throws InterruptedException {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);
        long id1 = generator.nextId();
        Thread.sleep(1);
        long id2 = generator.nextId();
        assertTrue(id1 < id2);
    }
}

