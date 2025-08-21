package com.wiross.logic;

public enum GameState {
    WON,
    LOST,
    IN_PROGRESS,
    NOT_STARTED;

    public boolean hasEnded() {
        return this == WON || this == LOST;
    }
}
