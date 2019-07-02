package com.leo.events.cardcontrol.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.card.annotation.CardMap;
import com.card.control.CardAdapter;
import com.card.control.ItemViewProvider;
import com.leo.events.cardcontrol.R;
import com.leo.events.cardcontrol.layout.SuspensionViewHolder;
import com.leo.events.cardcontrol.model.GroupCard;
import com.leo.events.cardcontrol.model.NameCard;

import org.w3c.dom.Text;

@CardMap(NameCard.class)
public class NameProvider extends ItemViewProvider<NameCard, NameProvider.NameViewHolder> {

    public NameProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new NameViewHolder(inflater.inflate(R.layout.item_name, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NameViewHolder holder, @NonNull NameCard card, final int position) {
        holder.tvName.setText(card.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "点击了第" + position + "位置", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class NameViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;

        public NameViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
