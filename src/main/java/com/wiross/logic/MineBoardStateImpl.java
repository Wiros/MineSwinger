package com.wiross.logic;

import com.wiross.panel.GamePanelUpdateEvent;
import com.wiross.utilities.RandomProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MineBoardStateImpl implements MineBoardState {
    private final RandomProvider randomProvider;
    private final List<FieldValue> listMap = new ArrayList<>();
    private final List<Integer> countMap = new ArrayList<>();
    private final int initX;
    private final int initY;
    private final int initBombs;
    private final Consumer<GamePanelUpdateEvent> stateChangeConsumer;
    private int leftFields;
    private GameState gameState;
    boolean initialized;

    public MineBoardStateImpl(int x, int y, int bombs, Consumer<GamePanelUpdateEvent> stateChangeConsumer, RandomProvider randomProvider) {
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

    public FieldValue getField(int x, int y) {
        return listMap.get(initX * y + x);
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
        List<Integer> intList = IntStream.range(0, initX * initY).boxed().collect(Collectors.toList());
        intList = randomProvider.shuffle(intList);
        List<Integer> bombList;

        if (initX * initY < 9 + initBombs) {
            bombList = intList.stream().limit(initBombs).sorted().toList();
        } else {
            bombList = intList.stream().filter(number -> !isAround(x, y, number)).limit(initBombs).sorted().toList();
        }

        IntStream.range(0, initX * initY)
                .mapToObj(n -> bombList.contains(n) ? FieldValue.BOMB : FieldValue.EMPTY)
                .forEach(listMap::add);
        IntStream.range(0, initX).forEach(nx ->
                IntStream.range(0, initY).forEach(ny ->
                        countMap.add(countBombsAround(nx, ny))
                )
        );
    }

    public boolean isAround(int x, int y, int number) {
        return initX * y + x == number ||
                initX * (y - 1) + x == number ||
                initX * (y + 1) + x == number ||
                initX * y + (x - 1) == number ||
                initX * y + (x + 1) == number ||
                initX * (y - 1) + (x - 1) == number ||
                initX * (y - 1) + (x + 1) == number ||
                initX * (y + 1) + (x - 1) == number ||
                initX * (y + 1) + (x + 1) == number;
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

    private Integer countBombsAround(int x, int y) {
        if (getField(x, y) == FieldValue.BOMB) {
            return -1;
        }
        int bombs = 0;
        for (int a = x - 1; a < x + 2; ++a) {
            for (int b = y - 1; b < y + 2; ++b) {
                if (a >= 0 && b >= 0 && a < initX && b < initY) {
                    if (getField(a, b) == FieldValue.BOMB) {
                        ++bombs;
                    }
                }
            }
        }

        return bombs;
    }
}
