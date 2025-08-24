package com.wiross.logic;

import com.wiross.panel.GamePanelUpdateEvent;
import com.wiross.utilities.RandomProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MineBoardStateTest {
    private static final int X_SIZE = 11;
    private static final int Y_SIZE = 12;
    private static final int BOMBS = 13;

    private RandomProvider randomProvider;
    private Consumer<GamePanelUpdateEvent> gamePanelUpdater;
    private MineBoardState mineBoardState;


    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        randomProvider = mock(RandomProvider.class);
        gamePanelUpdater = mock(Consumer.class);
        mineBoardState = new MineBoardStateImpl(X_SIZE, Y_SIZE, BOMBS, gamePanelUpdater, randomProvider);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_Not_Allow_First_Click_On_Bomb() {
        // GIVEN
        when(randomProvider.shuffle((List<Integer>)any())).thenReturn(
                new ArrayList<>(IntStream.range(0, 2* BOMBS)
                        .boxed().collect(Collectors.toList())));

        // WHEN
        int result = mineBoardState.countBombsAroundAndUncover(0, 0);

        // THEN
        assertNotEquals(-1, result, "First bomb shall not be clickable if possible.");
    }

    @Test
    void Should_Allow_First_Click_On_Empty_If_Possible() {
        // GIVEN
        mineBoardState = new MineBoardStateImpl(4, 4, 1, gamePanelUpdater, randomProvider);
        when(randomProvider.shuffle((List<Integer>)any())).thenReturn(
                new ArrayList<>(List.of(0, 1, 2, 4, 5, 6, 8, 9, 10, 16)));


        // WHEN
        int result = mineBoardState.countBombsAroundAndUncover(1, 1);

        // THEN
        assertEquals(0, result, "First click shall be empty if possible.");
    }

    @Test
    void countBombsAroundAndUncover() {
    }

    @Test
    void Is_Around_Test() {
        // GIVEN

        // WHEN
        boolean result = ((MineBoardStateImpl)mineBoardState).isAround(0, 0, 0);

        // THEN
        assertTrue(result);
    }

    @Test
    void getGameState() {
    }
}