package com.leo.events.cardcontrol.provider;

import android.graphics.Color;
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
import com.leo.events.cardcontrol.model.FiveModel;
import com.leo.events.cardcontrol.layout.OverlayViewHolder;

@CardMap(FiveModel.class)
public class FiveProvider extends ItemViewProvider<FiveModel, FiveProvider.FiveViewHolder> {


    public FiveProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public FiveViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new FiveViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull final FiveViewHolder holder, @NonNull FiveModel card, final int position) {
        holder.itemView.setBackgroundColor(Color.GRAY);
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

    public static class FiveViewHolder extends OverlayViewHolder {

        public FiveViewHolder(View itemView) {
            super(itemView);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(150, 150);
            itemView.setLayoutParams(lp);
        }
    }
}
