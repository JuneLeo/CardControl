package com.poseidon.control.obsever;

/**
 * Created by spf on 2018/11/15.
 */
public interface Subscription {

    void unsubscribe();

    boolean isUnsubscribed();
}
