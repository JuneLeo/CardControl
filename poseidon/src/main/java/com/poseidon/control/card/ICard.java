package com.poseidon.control.card;

import com.poseidon.control.CardControl;
import com.poseidon.control.control.ILifecycle;
import com.poseidon.control.control.IPrority;
import com.poseidon.control.presenter.ICardPresenter;
import com.poseidon.control.view.ICardView;

/**
 * Created by spf on 2018/11/15.
 */
public interface ICard extends ILifecycle, IPrority {

    ICardView getCardView();

    ICardPresenter getCardPresenter();

    CardControl getPoseidonControl();

    void onAttachCardManager(CardManager manager);

    CardManager getCardManager();

}
