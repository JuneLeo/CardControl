package com.poseidon.control.control;

/**
 * Created by spf on 2018/11/16.
 */
public interface IPrority extends Comparable<IPrority> {

    int MIN = -2;
    int LOW = -1;
    int DEFAULT = 0;
    int HIGH = 1;
    int MAX = 2;

    @Priority
    int getPriority();
}
