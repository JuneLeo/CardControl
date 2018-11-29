package com.leo.events.cardcontrol.card.toolbar;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.events.cardcontrol.R;
import com.poseidon.control.control.view.AbstractView;

/**
 * Created by spf on 2018/11/15.
 */
public class ToolbarCardView extends AbstractView<ToolbarPresenter, ToolbarVM> {

    @Override
    public View onCreateView(final ViewGroup parent) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_tool_bar, null);
        return v;
    }

    @Override
    public void onUpdateView(final View view, ViewGroup parent) {
        Context context = getContext(view);
        ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("卡片布局");
        }
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
    }

    private Context getContext(View view) {
        Context context = view.getContext();
        if (context instanceof ContextWrapper){
            return ((ContextWrapper) context).getBaseContext();
        }
        return context;
    }
}
