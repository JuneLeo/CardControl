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
import com.leo.events.cardcontrol.model.ThreeModel;

@CardMap(ThreeModel.class)
public class ThreeProvider extends ItemViewProvider<ThreeModel, ThreeProvider.ThreeViewHolder> {


    public ThreeProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ThreeViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ThreeViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull final ThreeViewHolder holder, @NonNull ThreeModel card, final int position) {
        holder.itemView.setBackgroundColor(0xffff0000);
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

    public static class ThreeViewHolder extends RecyclerView.ViewHolder {

        public ThreeViewHolder(View itemView) {
            super(itemView);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 150);
            itemView.setLayoutParams(lp);
        }
    }
}
