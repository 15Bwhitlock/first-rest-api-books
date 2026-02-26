package com.brayden.firstrestapibooks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    // This method tests the sum of two integers.
    @Test
    public void testSum() {
        // Perform the sum of 2 and 2 and store the result in a variable.
        int result = 2 + 2;

        // Assert that the result is equal to 4.
        assertEquals(4, result);
    }
}
