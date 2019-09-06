package com.nitrowise.basesrvr;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SampleTest {

    @Test
    void testAddition_shouldReturnSum() {
        // given
        int a = 1;
        int b = 3;

        // when
        int result = a + b;

        // then
        assertThat(result).isEqualTo(4);
    }

}
