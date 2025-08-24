package com.wiross.panel;

public record FlaggedBombsCounterUpdateEvent(int counter) implements GamePanelUpdateEvent {
}
