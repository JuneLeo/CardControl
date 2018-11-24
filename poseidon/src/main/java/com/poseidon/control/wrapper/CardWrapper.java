package com.poseidon.control.wrapper;

import android.util.Log;
import android.widget.LinearLayout;

import com.poseidon.control.CardControl;
import com.poseidon.control.card.CardManager;
import com.poseidon.control.debug.Logger;
import com.poseidon.control.layout.LinearLayoutManager;
import com.poseidon.control.obsever.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spf on 2018/11/15.
 */
public class CardWrapper {

    private List<LinearLayoutManager> linearLayoutManagers = new ArrayList<>();
    private Map<CardManager, LinearLayout> cardmanagers = new HashMap<>();

    public CardWrapper(CardControl mControl) {
        registEvent(mControl);
    }

    public void add(LinearLayout linearLayout) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager();
        linearLayoutManagers.add(linearLayoutManager);
        linearLayoutManager.setContainer(linearLayout);
        CardManager cardManager = new CardManager();
        linearLayoutManager.setManager(cardManager);
        cardmanagers.put(cardManager, linearLayout);
    }


    private void registEvent(CardControl mControl) {
        mControl.getObservable(CardControl.CREATE_VIEW_KEY, Object.class).subscribe(new Subscriber<Object>() {
            @Override
            public void onNext(Object o) {
                if (o == null) {
                    for (LinearLayoutManager linearLayoutManager : linearLayoutManagers) {
                        linearLayoutManager.onCreateView();
                    }
                }
            }
        });
        mControl.getObservable(CardControl.UPDATE_VIEW_KEY, Object.class).subscribe(new Subscriber<Object>() {
            @Override
            public void onNext(Object o) {
                Logger.d("cardcontrol create view");
                if (o == null) {
                    for (LinearLayoutManager linearLayoutManager : linearLayoutManagers) {
                        linearLayoutManager.onUpdateView();
                    }
                }
            }
        });
    }


    public Map<CardManager, LinearLayout> getCardmanager() {
        return cardmanagers;
    }
}
