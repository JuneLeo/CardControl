package com.leo.events.cardcontrol.card.tab;

import com.leo.events.cardcontrol.card.DefaultCard;
import com.poseidon.control.CardControl;
import com.poseidon.control.control.model.CardVM;
import com.poseidon.control.control.presenter.AbstractPresenter;
import com.poseidon.control.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class TabCard extends DefaultCard {

    public TabCard(CardControl control) {
        super(control);
    }

    @Override
    protected CardVM instanceCardVM() {
        return new TabCardVM();
    }

    @Override
    protected AbstractView instanceCardView() {
        return new TabCardView();
    }

    @Override
    protected AbstractPresenter instancePresenter() {
        return new TabPresenter();
    }
}
