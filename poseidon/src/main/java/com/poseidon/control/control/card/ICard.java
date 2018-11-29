package com.poseidon.control.control.card;

import com.poseidon.control.CardControl;
import com.poseidon.control.control.helper.ILifecycle;
import com.poseidon.control.control.helper.IPrority;
import com.poseidon.control.control.presenter.ICardPresenter;
import com.poseidon.control.control.view.ICardView;

/**
 * Created by spf on 2018/11/15.
 */
public interface ICard extends ILifecycle, IPrority {

    ICardView getCardView();

    ICardPresenter getCardPresenter();

    CardControl getCardControl();

}
