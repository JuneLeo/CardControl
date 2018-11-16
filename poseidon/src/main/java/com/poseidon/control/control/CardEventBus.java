package com.poseidon.control.control;

import android.os.Looper;
import android.util.Pair;

import com.poseidon.control.debug.DebugOption;
import com.poseidon.control.debug.Logger;
import com.poseidon.control.obsever.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by spf on 2018/11/15.
 */
public class CardEventBus {

    private Map<String, List<Observable>> observableMap;

    private BlockingQueue<EventMessage> eventMessages;


    private boolean inSpread = false;

    public CardEventBus() {
        observableMap = new HashMap<>();
        eventMessages = new LinkedBlockingQueue<>();
    }

    public void notify(List<Pair<String, Object>> data) {
        EventMessage bean = new EventMessage();
        bean.pairList = data;
        eventMessages.add(bean);
        spread();
    }

    public void notify(String key, Object data) {
        EventMessage bean = new EventMessage();
        bean.pair = new Pair<String, Object>(key, data);
        eventMessages.add(bean);
        spread();
    }

    /**
     * 扩散消息
     */
    private void spread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            if (DebugOption.isDebug()) {
                throw new IllegalStateException("this method must run on UI thread");
            } else {
                return;
            }
        }
        if (!inSpread) {
            try {
                Logger.e("spread start");
                inSpread = true;
                EventMessage currentBean;
                while ((currentBean = eventMessages.poll()) != null) {
                    if (currentBean.pair != null) {
                        Logger.e("spread:" + currentBean.pair.first);
                        notifyKey(currentBean.pair.first, currentBean.pair.second);
                    } else if (currentBean.pairList != null) {
                        //这里不可能有default的，暂时这么假定
                        for (Pair<String, Object> pair : currentBean.pairList) {
                            Logger.e("spread:" + pair.first);
                            notifyKey(pair.first, pair.second);
                        }
                    }
                }
            } finally {
                inSpread = false;
            }

        }
    }

    /**
     * 订阅者回调
     *
     * @param key
     * @param data
     */
    private void notifyKey(String key, Object data) {
        Logger.markStart();
        if (observableMap.get(key) == null) {
            observableMap.put(key, new ArrayList<Observable>());
        }
        List<Observable> observables = observableMap.get(key);
        if (observables.isEmpty()) {
            return;
        }
        for (Observable observable : observables) {
            try {
                observable.onNext(data);
            } catch (Throwable t) {
                if (DebugOption.isDebug()) {
                    throw t;
                }
                observable.onError(t);

            }
        }
        Logger.markEnd();
    }

    /**
     * 获取Observable
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Observable<T> getObservable(String key) {
        if (observableMap.get(key) == null) {
            observableMap.put(key, new ArrayList<Observable>());
        }
        Observable observable = Observable.create();
        observableMap.get(key).add(observable);
        return observable;
    }
}
