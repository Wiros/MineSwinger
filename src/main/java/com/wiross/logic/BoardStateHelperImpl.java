package com.wiross.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardStateHelperImpl implements BoardStateHelper {
    private final int initX;
    private final int initY;

    public BoardStateHelperImpl(int initX, int initY) {
        this.initX = initX;
        this.initY = initY;
    }

    @Override
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

    @Override
    public List<Integer> calculateBombCounters(List<Integer> bombsList) {
        List<Integer> countMap = new ArrayList<>(Collections.nCopies(initX * initY, 0));
        for (int bomb : bombsList) {
            int x = bomb % initX;
            int y = bomb / initX;
            for (int a = x - 1; a < x + 2; ++a) {
                for (int b = y - 1; b < y + 2; ++b) {
                    if (a >= 0 && b >= 0 && a < initX && b < initY) {
                        int index = initX * b + a;
                        countMap.set(index, 1 + countMap.get(index));
                    }
                }
            }
        }
        bombsList.forEach(index -> countMap.set(index, -1));
        return countMap;
    }

}
