package com.wiross.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

    private static List<Integer> getCloseValues() {
        return List.of(0, 1, 2, 3, 4, 5, 6, 7, 8);
    }
    @ParameterizedTest
    @MethodSource("getCloseValues")
    void Should_Find_Around_Close(Integer value) {
        // GIVEN
        boardStateHelper = new BoardStateHelperImpl(3, 4);

        // WHEN
        boolean result = boardStateHelper.isAround(1, 1, value);
        // THEN
        assertTrue(result);
    }

    private static List<Integer> getAwayValues() {
        return List.of(9, 10, 11);
    }
    @ParameterizedTest
    @MethodSource("getAwayValues")
    void Should_Find_Away_Not_Close(Integer value) {
        // GIVEN
        boardStateHelper = new BoardStateHelperImpl(3, 4);

        // WHEN
        boolean result = boardStateHelper.isAround(1, 1, value);
        // THEN
        assertFalse(result);
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
