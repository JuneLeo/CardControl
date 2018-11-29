package com.leo.events.cardcontrol.card.vip;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leo.events.cardcontrol.R;
import com.poseidon.control.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class VipCardView extends AbstractView<VipPresenter, VipCardVM> {

    @Override
    public View onCreateView(ViewGroup parent) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_person, null);
        return v;
    }

    @Override
    public void onUpdateView(View view, ViewGroup parent) {
        TextView name = view.findViewById(R.id.tv_name);
        if (!TextUtils.isEmpty(iCardVM.name))
            name.setText(iCardVM.name);
    }
}
