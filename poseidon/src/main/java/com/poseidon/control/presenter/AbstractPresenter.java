package com.poseidon.control.presenter;

import com.poseidon.control.CardControl;
import com.poseidon.control.card.ICard;
import com.poseidon.control.model.CardVM;
import com.poseidon.control.view.ICardView;

/**
 * Created by spf on 2018/11/15.
 */
public abstract class AbstractPresenter<VM extends CardVM> implements ICardPresenter {
    private ICardView iCardView;
    protected CardControl mControl;
    protected VM iCardVM;

    public AbstractPresenter() {

    }

    @Override
    public void onBindCard(ICard card) {
        iCardView = card.getCardView();
        mControl = card.getCardControl();
        registEvent();
    }

    protected abstract void registEvent();

    @Override
    public ICardView getCardView() {
        return iCardView;
    }

    public void onBindCardVM(VM iCardVM) {
        this.iCardVM = iCardVM;
    }

    protected void updateView(){
        ICardView cardView = getCardView();
        if (cardView != null) {
            if (cardView.getView() != null && cardView.getRootView() != null)
                cardView.onUpdateView(cardView.getView(), cardView.getRootView());
        }
    }
}
