package com.poseidon.control.event;

import android.os.Handler;
import android.os.Looper;

import com.poseidon.control.obsever.Observable;
import com.poseidon.control.obsever.ObservableGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by spf on 2018/11/24.
 */
public class EventManager {
    private EventDispatcher mDispatcher = new EventDispatcher();
    private Map<String, ObservableGroup> mObserverGroups;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static EventManager mInstance = new EventManager();

    public static EventManager get() {
        return mInstance;
    }


    public EventManager() {
        mDispatcher.start(); //启动分发
        mObserverGroups = new HashMap<>();
    }

    class EventDispatcher extends Dispatcher<Event> {
        private static final int DEFAULT_THREAD_SIZE = 4;
        private int mWorkThreadSize = DEFAULT_THREAD_SIZE;

        EventDispatcher() {
            mWorker = Executors.newFixedThreadPool(mWorkThreadSize);
            mQueue = new LinkedBlockingQueue<>();
        }

        @Override
        public void onDispatch(final Event event) {
            //dispatch
            if (event.checkNull()) {
                return;
            }
            final ObservableGroup observerGroup = mObserverGroups.get(event.group);
            if (observerGroup != null) {
                if (event.isUiThread) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            observerGroup.notify(event.key, event.data);
                        }
                    });
                    return;
                }
                observerGroup.notify(event.key, event.data);
            }
        }
    }


    /**
     * 添加分发任务
     *
     * @param event
     */
    public void add(Event event) {
        mDispatcher.add(event);
    }

    /**
     * 销毁Group的回调
     *
     * @param group
     */
    public void dispose(String group) {
        ObservableGroup observerGroup = mObserverGroups.get(group);
        if (observerGroup != null) {
            observerGroup.dispose();
        }
    }

    /**
     * 观察者式 回调
     *
     * @param group
     * @param key
     * @param <T>
     * @return
     */
    public <T> Observable<T> getObservable(String group, String key) {
        ObservableGroup groups = mObserverGroups.get(group);
        if (groups == null) {
            groups = new ObservableGroup();
            mObserverGroups.put(group, groups);
        }
        Observable observable = Observable.create();
        groups.put(key, observable);
        return observable;
    }
}
