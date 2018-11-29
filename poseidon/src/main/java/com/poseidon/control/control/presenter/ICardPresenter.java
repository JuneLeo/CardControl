package com.poseidon.control.control.presenter;

import com.poseidon.control.control.card.ICard;
import com.poseidon.control.control.view.ICardView;

public interface ICardPresenter {

    void onBindCard(ICard card);

    ICardView getCardView();
}