package com.wiross.logic;

import com.wiross.panel.GamePanelUpdateEvent;
import com.wiross.utilities.RandomProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MineBoardStateImpl implements MineBoardState {
    private final BoardStateHelper boardStateHelper;
    private final RandomProvider randomProvider;
    private final List<FieldValue> listMap = new ArrayList<>();
    private List<Integer> countMap = new ArrayList<>();
    private final int initX;
    private final int initY;
    private final int initBombs;
    private final Consumer<GamePanelUpdateEvent> stateChangeConsumer;
    private int leftFields;
    private GameState gameState;
    boolean initialized;

    public MineBoardStateImpl(int x, int y, int bombs,
                              Consumer<GamePanelUpdateEvent> stateChangeConsumer,
                              RandomProvider randomProvider, BoardStateHelper boardStateHelper) {
        this.boardStateHelper = boardStateHelper;
        this.initX = x;
        this.initY = y;
        this.initBombs = bombs;
        this.leftFields = x * y;
        this.gameState = GameState.NOT_STARTED;
        this.stateChangeConsumer = stateChangeConsumer;
        this.initialized = false;
        this.randomProvider = randomProvider;

        stateChangeConsumer.accept(gameState);
    }

    @Override
    public Integer countBombsAroundAndUncover(int x, int y) {
        if (! initialized) {
            initializeBoardWithClick(x, y);
            initialized = true;
        }
        if (gameState.hasEnded()) {
            return null;
        } else {
            Integer returnValue = countMap.get(initX * y + x);
            updateGameStateUncoveredBomb(-1 == returnValue);
            return returnValue;
        }
    }

    private void initializeBoardWithClick(int x, int y) {
        System.out.println("Clicked on x: " + x + ", y: " + y);
        List<Integer> intList = IntStream.range(0, initX * initY).boxed().collect(Collectors.toList());
        System.out.println("intList: " + intList);
        intList = randomProvider.shuffle(intList);
        System.out.println("intList shuffled: " + intList);
        List<Integer> bombList;

        if (initX * initY < 9 + initBombs) {
            bombList = intList.stream().limit(initBombs).sorted().toList();
        } else {
            bombList = intList.stream()
                    .filter(number -> ! boardStateHelper.isAround(x, y, number))
                    .limit(initBombs).sorted()
                    .toList();
        }
        System.out.println("bombList: " + bombList);

        IntStream.range(0, initX * initY)
                .mapToObj(n -> bombList.contains(n) ? FieldValue.BOMB : FieldValue.EMPTY)
                .forEach(listMap::add);

        countMap = boardStateHelper.calculateBombCounters(bombList);

        System.out.println(countMap);
        System.out.println(listMap);
    }



    private void updateGameStateUncoveredBomb(boolean uncoveredBomb) {
        GameState previousState = gameState;
        if (!gameState.hasEnded()) {
            --leftFields;

            if (gameState == GameState.NOT_STARTED) {
                gameState = GameState.IN_PROGRESS;
            }

            if (uncoveredBomb) {
                gameState = GameState.LOST;
            } else if (leftFields == initBombs) {
                gameState = GameState.WON;
            }
        }

        if (previousState != gameState) {
            stateChangeConsumer.accept(gameState);
        }
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }
}
