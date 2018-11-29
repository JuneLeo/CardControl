package com.leo.events.cardcontrol.card.func;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.events.cardcontrol.R;
import com.poseidon.control.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class FuncCardView extends AbstractView<FuncPresenter, FuncCardVM> {

    @Override
    public View onCreateView(ViewGroup parent) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_func, null);
        return v;
    }

    @Override
    public void onUpdateView(View view, ViewGroup parent) {
    }
}
