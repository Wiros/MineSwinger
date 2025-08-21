package com.wiross.logic;

public interface MineBoardState {
    Integer countBombsAroundAndUncover(int x, int y);
    GameState getGameState();
}
