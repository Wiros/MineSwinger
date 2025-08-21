package com.wiross.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MineBoardStateImpl implements MineBoardState {
    private final List<FieldValue> listMap = new ArrayList<>();
    private final List<Integer> countMap = new ArrayList<>();
    private final int initX;
    private final int initY;
    private final int initBombs;
    private int leftFields;
    private GameState gameState;

    public MineBoardStateImpl(int x, int y, int bombs) {
        this.initX = x;
        this.initY = y;
        this.initBombs = bombs;
        this.leftFields = x * y;
        this.gameState = GameState.NOT_STARTED;

        List<Integer> intList = IntStream.range(0, x * y).boxed().collect(Collectors.toList());
        Collections.shuffle(intList);
        List<Integer> bombList = intList.stream().limit(bombs).sorted().toList();

        IntStream.range(0, x * y)
                .mapToObj(n -> bombList.contains(n) ? FieldValue.BOMB : FieldValue.EMPTY)
                .forEach(listMap::add);
        IntStream.range(0, x).forEach(nx ->
                IntStream.range(0, y).forEach(ny ->
                    countMap.add(countBombsAround(nx, ny))
                )
        );
    }

    public FieldValue getField(int x, int y) {
        return listMap.get(initX * y + x);
    }

    @Override
    public Integer countBombsAroundAndUncover(int x, int y) {
        if (gameState.hasEnded()) {
            return null;
        } else {
            Integer returnValue = countMap.get(initX * y + x);
            updateGameStateUncoveredBomb(-1 == returnValue);
            return returnValue;
        }
    }

    private void updateGameStateUncoveredBomb(boolean uncoveredBomb) {
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
