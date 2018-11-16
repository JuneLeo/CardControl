package com.poseidon.control.obsever;

/**
 * Created by spf on 2018/11/15.
 */
public interface Observer<T> {

    void onNext(T t);

    void onError(Throwable e);
}
