package com.leo.events.cardcontrol.card.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.events.cardcontrol.R;
import com.poseidon.control.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class TabCardView extends AbstractView<TabPresenter, TabCardVM> {

    @Override
    public View onCreateView(ViewGroup parent) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_tab, null);
        return v;
    }

    @Override
    public void onUpdateView(View view, ViewGroup parent) {
    }
}
