package com.poseidon.control.control.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.poseidon.control.control.model.CardVM;
import com.poseidon.control.control.presenter.ICardPresenter;

/**
 * Created by spf on 2018/11/15.
 */
public abstract class AbstractView<T extends ICardPresenter, VM extends CardVM> implements ICardView {

    protected T iCardPresenter;
    protected VM iCardVM;
    protected View view;

    protected ViewGroup rootView;

    @Override
    public void onBindView(View view, ViewGroup parent) {
        this.view = view;
        this.rootView = parent;
    }

    @Override
    public void onCreate(Bundle bundle) {
        //nothing
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean useRecycledView() {
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    public void onBindPresenter(T iCardPresenter) {
        this.iCardPresenter = iCardPresenter;
    }

    public void onBindCardVM(VM cardVM) {
        this.iCardVM = cardVM;
    }


    @Override
    public View getView() {
        return view;
    }

    @Override
    public ViewGroup getRootView() {
        return rootView;
    }
}
