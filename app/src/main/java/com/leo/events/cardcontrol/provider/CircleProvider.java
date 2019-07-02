package com.leo.events.cardcontrol.provider;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.card.annotation.CardMap;
import com.card.control.CardAdapter;
import com.card.control.ItemViewProvider;
import com.leo.events.cardcontrol.R;
import com.leo.events.cardcontrol.layout.OverlayViewHolder;
import com.leo.events.cardcontrol.model.CircleCard;
@CardMap(CircleCard.class)
public class CircleProvider extends ItemViewProvider<CircleCard, CircleProvider.CircleViewHolder> {


    public CircleProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public CircleViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new CircleViewHolder(inflater.inflate(R.layout.item_circle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CircleViewHolder holder, @NonNull CircleCard card, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textView.setText(position + "");
                Toast.makeText(holder.itemView.getContext(), "点击了第" + position + "位置", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class CircleViewHolder extends OverlayViewHolder {
        TextView textView;

        public CircleViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }
}
