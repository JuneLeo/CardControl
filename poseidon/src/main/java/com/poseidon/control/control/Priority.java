package com.poseidon.control.control;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by spf on 2018/11/15.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(flag = true, value = {
        IPrority.MIN,IPrority.LOW,IPrority.DEFAULT,IPrority.HIGH,IPrority.MAX,
})
public @interface Priority { }
