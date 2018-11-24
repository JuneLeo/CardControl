package com.poseidon.control.event;

/**
 * Created by spf on 2018/11/24.
 */
public interface IDispatcher<T> {
    /**
     * 分发事件
     * @param t
     */
    void onDispatch(T t);
}
