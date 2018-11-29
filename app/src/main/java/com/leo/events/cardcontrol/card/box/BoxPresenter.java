package com.leo.events.cardcontrol.card.box;

import com.poseidon.control.event.obsever.Subscriber;
import com.poseidon.control.control.presenter.AbstractPresenter;

/**
 * Created by spf on 2018/11/15.
 */
public class BoxPresenter extends AbstractPresenter<BoxCardVM> {

    @Override
    protected void registEvent() {
        mControl.getObservable("aaaa",BoxCard.class).subscribe(new Subscriber<BoxCard>() {
            @Override
            public void onNext(BoxCard appleCard) {

            }
        });
    }

    public void btnClick() {
        mControl.notifyUI("key_person","你好");
    }

    public void btnClickClear() {

    }
}
