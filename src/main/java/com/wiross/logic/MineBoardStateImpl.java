package com.wiross.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MineBoardStateImpl implements MineBoardState {
    private final List<FieldValue> listMap = new ArrayList<>();
    private final int initX;
    private final int initY;

    public MineBoardStateImpl(int x, int y, int bombs) {
        this.initX = x;
        this.initY = y;
        List<Integer> intList = IntStream.range(0, x * y).boxed().collect(Collectors.toList());
        Collections.shuffle(intList);
        List<Integer> bombList = intList.stream().limit(bombs).sorted().toList();

        IntStream.range(0, x * y)
                .mapToObj(n -> bombList.contains(n) ? FieldValue.BOMB : FieldValue.EMPTY)
                .forEach(listMap::add);
    }

    public FieldValue getField(int x, int y) {
        return listMap.get(initX * y + x);
    }

    @Override
    public int countBombsAround(int x, int y) {
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
