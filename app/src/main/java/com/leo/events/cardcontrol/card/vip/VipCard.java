package com.leo.events.cardcontrol.card.vip;

import com.leo.events.cardcontrol.card.DefaultCard;
import com.poseidon.control.CardControl;
import com.poseidon.control.model.CardVM;
import com.poseidon.control.presenter.AbstractPresenter;
import com.poseidon.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class VipCard extends DefaultCard {

    public VipCard(CardControl control) {
        super(control);
    }

    @Override
    protected CardVM instanceCardVM() {
        return new VipCardVM();
    }

    @Override
    protected AbstractView instanceCardView() {
        return new VipCardView();
    }

    @Override
    protected AbstractPresenter instancePresenter() {
        return new VipPresenter();
    }
}
