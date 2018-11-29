package com.poseidon.control.event.obsever;

/**
 * Created by spf on 2018/11/15.
 */
public class Observable<T> implements Observer<T> {
    public Subscriber<T> subscriber;

    public static Observable create() {
        return new Observable();
    }

    public Subscription subscribe(Subscriber<T> subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("subscriber can not be null");
        }
        this.subscriber = subscriber;
        return subscriber;
    }


    @Override
    public void onNext(T t) {
        if (subscriber == null || subscriber.isUnsubscribed()) {
            return;
        }
        subscriber.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        if (subscriber == null || subscriber.isUnsubscribed()) {
            return;
        }
        subscriber.onError(e);
    }
}
