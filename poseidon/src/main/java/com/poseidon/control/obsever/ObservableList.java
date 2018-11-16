package com.poseidon.control.obsever;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by spf on 2018/11/15.
 */
public class ObservableList implements Subscription {
    private Set<Observable> subscriptions;
    private volatile boolean unsubscribed;

    public ObservableList() {
    }

    public Observable add(final Observable s) {
        if (!unsubscribed) {
            Set<Observable> subs = subscriptions;
            if (subs == null) {
                subs = new HashSet<>();
                subscriptions = subs;
            }
            subs.add(s);
        }
        return s;
    }

    public void remove(final Observable s) {
        if (!unsubscribed) {
            boolean unsubscribe;
            synchronized (this) {
                Set<Observable> subs = subscriptions;
                if (unsubscribed || subs == null) {
                    return;
                }
                unsubscribe = subs.remove(s);
            }
            if (unsubscribe) {
                if (s.subscriber == null)
                    return;
                if (s.subscriber.isUnsubscribed()) {
                    return;
                }
                s.subscriber.unsubscribe();
            }
        }
    }


    @Override
    public void unsubscribe() {

        if (unsubscribed) {
            return;
        }
        Set<Observable> list;
        unsubscribed = true;
        list = subscriptions;
        subscriptions = null;
        unsubscribeFromAll(list);
    }


    private static void unsubscribeFromAll(Collection<Observable> observables) {
        if (observables == null) {
            return;
        }
        for (Observable o : observables) {
            Subscription s = o.subscriber;
            if (s == null) {
                continue;
            }
            if (!s.isUnsubscribed()) {
                s.unsubscribe();
            }
        }
    }

    @Override
    public boolean isUnsubscribed() {
        return unsubscribed;
    }
}
