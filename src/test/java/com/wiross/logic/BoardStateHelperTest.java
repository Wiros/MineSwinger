package com.wiross.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardStateHelperTest {
    private BoardStateHelper boardStateHelper;
    private static final int INIT_X = 2;
    private static final int INIT_Y = 3;

    @BeforeEach
    void setup() {
        boardStateHelper = new BoardStateHelperImpl(INIT_X, INIT_Y);
    }

    @Test
    void Should_Calculate_Bomb_Counters() {
        // GIVEN
        List<Integer> expected = List.of(0, 0, 2, 2, -1, -1);

        // WHEN
        List<Integer> result = boardStateHelper.calculateBombCounters(List.of(4, 5));

        // THEN
        assertEquals(expected, result);
    }

    @Test
    void Should_Find_Around_Exact() {
        // GIVEN

        // WHEN
        boolean result = boardStateHelper.isAround(0, 1, 1);
        // THEN
        assertTrue(result);
    }

    @Test
    void Should_Find_Around_Close() {
        // GIVEN

        // WHEN
        boolean result = boardStateHelper.isAround(1, 1, 0);
        // THEN
        assertTrue(result);
    }

    @Test
    void Should_Not_Find_Around_Far() {
        // GIVEN

        // WHEN
        boolean result = boardStateHelper.isAround(0, 0, 5);
        // THEN
        assertFalse(result);
    }
}
