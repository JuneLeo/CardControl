package com.leo.events.cardcontrol.card.func;

import com.leo.events.cardcontrol.card.DefaultCard;
import com.poseidon.control.CardControl;
import com.poseidon.control.control.model.CardVM;
import com.poseidon.control.control.presenter.AbstractPresenter;
import com.poseidon.control.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class FuncCard extends DefaultCard {

    public FuncCard(CardControl control) {
        super(control);
    }

    @Override
    protected CardVM instanceCardVM() {
        return new FuncCardVM();
    }

    @Override
    protected AbstractView instanceCardView() {
        return new FuncCardView();
    }

    @Override
    protected AbstractPresenter instancePresenter() {
        return new FuncPresenter();
    }
}
