package com.leo.events.cardcontrol.card.title;

import com.poseidon.control.control.Priority;
import com.poseidon.control.presenter.AbstractPresenter;

/**
 * Created by spf on 2018/11/15.
 */
@Priority
public class TitlePresenter extends AbstractPresenter<TitleCardVM> {

    @Override
    protected void registEvent() {

    }


    public void btnClickClear() {
        mControl.notify("box_click","点击了box");
    }
}
