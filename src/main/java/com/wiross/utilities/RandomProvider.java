package com.wiross.utilities;



import java.util.Collections;
import java.util.List;

public class RandomProvider {
    public <T> List<T> shuffle(List<T> list) {
        Collections.shuffle(list);
        return list;
    }
}
