package com.leo.events.cardcontrol.card.vip;

import com.poseidon.control.event.obsever.Subscriber;
import com.poseidon.control.control.presenter.AbstractPresenter;

/**
 * Created by spf on 2018/11/15.
 */
public class VipPresenter extends AbstractPresenter<VipCardVM> {
    @Override
    protected void registEvent() {

        mControl.getObservable("key_person", String.class).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                iCardVM.name = s;
                updateView();

            }
        });

        String key_person = mControl.getData("key_person", String.class);
    }
}
