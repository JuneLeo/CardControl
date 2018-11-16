package com.poseidon.control.obsever;

import com.poseidon.control.debug.Logger;

/**
 * Created by spf on 2018/11/15.
 */
public abstract class Subscriber<T> implements Observer<T>, Subscription {
    private boolean unsubscribed;

    @Override
    public void unsubscribe() {
        //释放东西
        unsubscribed = true;
    }

    @Override
    public boolean isUnsubscribed() {
        return unsubscribed;
    }

    @Override
    public void onError(Throwable e) {
        Logger.d(e.getMessage());
    }
}
