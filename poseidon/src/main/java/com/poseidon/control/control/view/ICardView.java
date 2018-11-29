package com.poseidon.control.control.view;

import android.view.View;
import android.view.ViewGroup;

import com.poseidon.control.control.helper.ILifecycle;

public interface ICardView extends ILifecycle {

    View onCreateView(ViewGroup parent);

    void onUpdateView(View view, ViewGroup parent);

    void onBindView(View view, ViewGroup parent);

    /**
     * 默认复用，不重建
     *
     * @return
     */
    boolean useRecycledView();

    /**
     * 默认 true
     *
     * @return
     */
    boolean isAvailable();

    /**
     * Child
     *
     * @return
     */
    View getView();

    /**
     * parent
     *
     * @return
     */
    ViewGroup getRootView();
}