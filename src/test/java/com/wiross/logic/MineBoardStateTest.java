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
    private static final int X_SIZE = 3;
    private static final int Y_SIZE = 4;
    private static final int BOMBS = 2;

    private RandomProvider randomProvider;
    private BoardStateHelper boardStateHelper;
    private Consumer<GamePanelUpdateEvent> gamePanelUpdater;
    private MineBoardState mineBoardState;


    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        boardStateHelper = new BoardStateHelperImpl(X_SIZE, Y_SIZE);
        randomProvider = mock(RandomProvider.class);
        gamePanelUpdater = mock(Consumer.class);
        mineBoardState = new MineBoardStateImpl(X_SIZE, Y_SIZE, BOMBS, gamePanelUpdater, randomProvider, boardStateHelper);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_Not_Allow_First_Click_On_Bomb() {
        // GIVEN
        when(randomProvider.shuffle((List<Integer>)any())).thenReturn(
                new ArrayList<>(IntStream.range(0, 12)
                        .boxed().collect(Collectors.toList())));

        // WHEN
        int result = mineBoardState.countBombsAroundAndUncover(0, 0);

        // THEN
        assertNotEquals(-1, result, "First bomb shall not be clickable if possible.");
    }

    @Test
    void Should_Allow_First_Click_On_Empty_If_Possible() {
        // GIVEN
        boardStateHelper = new BoardStateHelperImpl(4, 4);
        mineBoardState = new MineBoardStateImpl(4, 4, 1, gamePanelUpdater, randomProvider, boardStateHelper);
        when(randomProvider.shuffle((List<Integer>)any())).thenReturn(
                new ArrayList<>(List.of(0, 1, 2, 4, 5, 6, 8, 9, 10, 15)));

        // WHEN
        int result = mineBoardState.countBombsAroundAndUncover(1, 1);

        // THEN
        assertEquals(0, result, "First click shall be empty if possible.");
        assertEquals(GameState.IN_PROGRESS, mineBoardState.getGameState());
    }


    @Test
    void Should_Get_Not_Started_Before_Click() {
        // GIVEN
        // WHEN
        var result = mineBoardState.getGameState();

        // THEN
        assertEquals(GameState.NOT_STARTED, result);
    }

    @Test
    void Should_Get_Game_Lost_On_Bomb_Click() {
        // GIVEN
        boardStateHelper = new BoardStateHelperImpl(3, 3);
        mineBoardState = new MineBoardStateImpl(3, 3, 1, gamePanelUpdater, randomProvider, boardStateHelper);
        when(randomProvider.shuffle((List<Integer>)any())).thenReturn(
                new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 15)));

        // WHEN
        GameState stateResult_0 = mineBoardState.getGameState();
        int result_1 = mineBoardState.countBombsAroundAndUncover(2, 2);
        GameState stateResult_1 = mineBoardState.getGameState();
        int result_2 = mineBoardState.countBombsAroundAndUncover(0, 0);
        GameState stateResult_2 = mineBoardState.getGameState();

        // THEN
        assertEquals(0, result_1, "First click shall be empty if possible.");
        assertEquals(-1, result_2, "Bomb click shall return -1.");
        assertEquals(GameState.NOT_STARTED, stateResult_0);
        assertEquals(GameState.IN_PROGRESS, stateResult_1);
        assertEquals(GameState.LOST, stateResult_2);
    }

    @Test
    void Should_Get_Game_Won_On_All_Fields_Clicked() {
        // GIVEN
        boardStateHelper = new BoardStateHelperImpl(2, 1);
        mineBoardState = new MineBoardStateImpl(2, 1, 1, gamePanelUpdater, randomProvider, boardStateHelper);
        when(randomProvider.shuffle((List<Integer>)any())).thenReturn(
                new ArrayList<>(List.of(0)));

        // WHEN
        GameState stateResult_0 = mineBoardState.getGameState();
        int result_1 = mineBoardState.countBombsAroundAndUncover(1, 0);
        GameState stateResult_1 = mineBoardState.getGameState();

        // THEN
        assertEquals(1, result_1, "First click is near one bomb.");
        assertEquals(GameState.NOT_STARTED, stateResult_0);
        assertEquals(GameState.WON, stateResult_1);
    }
}