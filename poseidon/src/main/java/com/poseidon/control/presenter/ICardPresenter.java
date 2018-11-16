package com.poseidon.control.presenter;

import com.poseidon.control.card.ICard;
import com.poseidon.control.model.CardVM;
import com.poseidon.control.view.ICardView;

public interface ICardPresenter {

    void onBindCard(ICard card);

    ICardView getICardView();
}