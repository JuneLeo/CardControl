package com.poseidon.control.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by spf on 2018/11/24.
 */
public class Dispatcher<T> extends Thread implements IDispatcher<T> {

    protected BlockingQueue<T> mQueue;
    protected ExecutorService mWorker;


    @Override
    public void onDispatch(T t) {
        //dispatch
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                dispatchLoop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchLoop() throws InterruptedException {
        T t = mQueue.take();
        realDispatch(t);

    }

    private void realDispatch(final T t) {
        mWorker.submit(new Runnable() {
            @Override
            public void run() {
                onDispatch(t);
            }
        });
    }

    public void add(T t) {
        try {
            mQueue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
