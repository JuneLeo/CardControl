package com.leo.events.cardcontrol.card;

import com.poseidon.control.CardControl;
import com.poseidon.control.card.AbstractCard;
import com.poseidon.control.card.CardManager;
import com.poseidon.control.model.CardVM;
import com.poseidon.control.presenter.AbstractPresenter;
import com.poseidon.control.presenter.ICardPresenter;
import com.poseidon.control.view.AbstractView;
import com.poseidon.control.view.ICardView;

/**
 * Created by spf on 2018/11/15.
 */
public abstract class DefaultCard extends AbstractCard {

    private CardControl mControl;
    private AbstractView mCardView;
    private AbstractPresenter mCardPresenter;
    private CardManager mCardManager;

    public DefaultCard(CardControl control) {
        this.mControl = control;
        // view 取 vm中数据
        // presenter给vm设置数据，
        // view事件在presenter中

        //vm
        CardVM cardVM = instanceCardVM();
        //presenter
        mCardPresenter = instancePresenter();
        mCardPresenter.onBindCardVM(cardVM);
        //view
        mCardView = instanceCardView();
        mCardView.onBindCardVM(cardVM);
        //card
        mCardPresenter.onBindCard(this);
        //view绑定presenter
        mCardView.onBindPresenter(mCardPresenter);

    }

    /**
     * vm
     * @return
     */
    protected abstract CardVM instanceCardVM();

    /**
     * view
     * @return
     */
    protected abstract AbstractView instanceCardView();

    /**
     * presenter
     * @return
     */
    protected abstract AbstractPresenter instancePresenter();


    @Override
    public ICardPresenter getCardPresenter() {
        return mCardPresenter;
    }

    @Override
    public CardControl getCardControl() {
        return mControl;
    }

    @Override
    public ICardView getCardView() {
        return mCardView;
    }


}
