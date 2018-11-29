package com.leo.events.cardcontrol.card.title;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.events.cardcontrol.R;
import com.poseidon.control.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class TitleCardView extends AbstractView<TitlePresenter,TitleCardVM> {

    @Override
    public View onCreateView(final ViewGroup parent) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_float, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCardPresenter.btnClickClear();
            }
        });
        return v;
    }

    @Override
    public void onUpdateView(View view, ViewGroup parent) {
    }
}
