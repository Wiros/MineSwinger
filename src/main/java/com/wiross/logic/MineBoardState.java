package com.wiross.logic;

public interface MineBoardState {
    FieldValue getField(int x, int y);
    int countBombsAround(int x, int y);
}
