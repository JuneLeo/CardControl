package com.leo.events.cardcontrol.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.card.annotation.CardMap;
import com.card.control.CardAdapter;
import com.card.control.ItemViewProvider;
import com.leo.events.cardcontrol.model.TwoModel;

@CardMap(TwoModel.class)
public class TwoProvider extends ItemViewProvider<TwoModel, TwoProvider.TwoViewHolder> {


    public TwoProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public TwoViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new TwoViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull final TwoViewHolder holder, @NonNull TwoModel card,final int position) {
        holder.itemView.setBackgroundColor(0xff00ff00);
        if (holder.itemView instanceof TextView) {
            ((TextView) holder.itemView).setText(position + "");
            ((TextView) holder.itemView).setGravity(Gravity.CENTER);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class TwoViewHolder extends RecyclerView.ViewHolder {

        public TwoViewHolder(View itemView) {
            super(itemView);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 150);
            itemView.setLayoutParams(lp);
        }
    }
}
