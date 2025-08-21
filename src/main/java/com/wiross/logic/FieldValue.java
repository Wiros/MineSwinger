package com.wiross.logic;

public enum FieldValue {
    BOMB("X"),
    EMPTY("");

    private final String val;

    FieldValue(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
