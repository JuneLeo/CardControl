package com.poseidon.control.view;
import android.view.View;
import android.view.ViewGroup;

import com.poseidon.control.control.ILifecycle;

public interface ICardView extends ILifecycle {

    View onCreateView(ViewGroup parent);

    void onUpdateView(View view, ViewGroup parent);

    /**
     * 默认复用，不重建
     * @return
     */
    boolean useRecycledView();

    /**
     * 默认 true
     * @return
     */
    boolean isAvailable();

    /**
     * 是否可见
     * @return
     */
    int getVisibility();
}