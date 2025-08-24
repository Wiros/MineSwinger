package com.wiross.logic;

import com.wiross.panel.GamePanelUpdateEvent;

public enum GameState implements GamePanelUpdateEvent {
    WON,
    LOST,
    IN_PROGRESS,
    NOT_STARTED;

    public boolean hasEnded() {
        return this == WON || this == LOST;
    }
}
