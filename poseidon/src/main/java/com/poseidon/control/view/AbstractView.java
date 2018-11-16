package com.poseidon.control.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.poseidon.control.card.ICard;
import com.poseidon.control.model.CardVM;
import com.poseidon.control.presenter.ICardPresenter;

/**
 * Created by spf on 2018/11/15.
 */
public abstract class AbstractView<T extends ICardPresenter,VM extends CardVM> implements ICardView {

    protected T iCardPresenter;
    protected VM iCardVM;

    @Override
    public void onCreate(Bundle bundle) {
        //activity onCreate完成后回调
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

    @Override
    public int getVisibility() {
        return ICard.DEFAULT;
    }

    public void onBindPresenter(T iCardPresenter){
        this.iCardPresenter = iCardPresenter;
    }

    public void setCardVM(VM cardVM){
        this.iCardVM = cardVM;
    }
}
