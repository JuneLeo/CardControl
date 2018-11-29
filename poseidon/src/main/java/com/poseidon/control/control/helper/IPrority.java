package com.poseidon.control.control.helper;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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


    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {
            IPrority.MIN,IPrority.LOW,IPrority.DEFAULT,IPrority.HIGH,IPrority.MAX,
    })
    @interface Priority { }
}
