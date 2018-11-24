package com.poseidon.control.obsever;

import com.poseidon.control.debug.DebugOption;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spf on 2018/11/24.
 */
public class ObservableGroup {
    public Map<String, Observable> mObserverMap = new HashMap<>();

    public void put(String key, Observable observer) {
        mObserverMap.put(key, observer);
    }


    public void notify(String key, Object object) {
        Observer observer = mObserverMap.get(key);
        if (observer != null) {
            try {
                observer.onNext(object);
            } catch (Exception e) {
                if (DebugOption.isDebug()) {
                    throw e;
                }
                observer.onError(e);
            }
        }
    }

    public void dispose() {
        for (Map.Entry<String, Observable> entry : mObserverMap.entrySet()) {
            Observable observable = entry.getValue();
            Subscriber subscriber = observable.subscriber;
            if (subscriber != null) {
                if (subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe();
                }
            }
        }
    }

}
