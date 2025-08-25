package com.wiross.logic;

import java.util.List;

public interface BoardStateHelper {
    boolean isAround(int x, int y, int number);
    List<Integer> calculateBombCounters(List<Integer> bombsList);
}
