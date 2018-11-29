package com.leo.events.cardcontrol.card.box;

import com.leo.events.cardcontrol.card.DefaultCard;
import com.poseidon.control.CardControl;
import com.poseidon.control.control.model.CardVM;
import com.poseidon.control.control.presenter.AbstractPresenter;
import com.poseidon.control.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class BoxCard extends DefaultCard {

    public BoxCard(CardControl control) {
        super(control);
    }

    @Override
    protected CardVM instanceCardVM() {
        return new BoxCardVM();
    }

    @Override
    protected AbstractView instanceCardView() {
        return new BoxCardView();
    }

    @Override
    protected AbstractPresenter instancePresenter() {
        return new BoxPresenter();
    }
}
