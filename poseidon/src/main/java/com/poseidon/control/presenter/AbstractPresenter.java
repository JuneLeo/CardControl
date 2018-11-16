package com.poseidon.control.presenter;

import com.poseidon.control.CardControl;
import com.poseidon.control.card.ICard;
import com.poseidon.control.model.CardVM;
import com.poseidon.control.view.ICardView;

/**
 * Created by spf on 2018/11/15.
 */
public abstract class AbstractPresenter<VM extends CardVM> implements ICardPresenter {
    ICardView iCardView;
    protected CardControl mControl;
    protected VM iCardVM;

    public AbstractPresenter() {

    }

    @Override
    public void onBindCard(ICard card) {
        iCardView = card.getCardView();
        mControl = card.getPoseidonControl();
        registEvent();
    }

    protected abstract void registEvent();

    @Override
    public ICardView getICardView() {
        return iCardView;
    }

    public void setCardVM(VM iCardVM) {
        this.iCardVM = iCardVM;
    }
}
