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
import com.leo.events.cardcontrol.layout.SuspensionViewHolder;
import com.leo.events.cardcontrol.model.GroupCard;

@CardMap(GroupCard.class)
public class GroupProvider extends ItemViewProvider<GroupCard, GroupProvider.GroupViewHolder> {

    public GroupProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new GroupViewHolder(inflater.inflate(R.layout.item_group, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHolder holder, @NonNull GroupCard card, final int position) {
        holder.tvGroup.setText(card.group);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "点击了第" + position + "位置", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class GroupViewHolder extends SuspensionViewHolder {
        public TextView tvGroup;

        public GroupViewHolder(View itemView) {
            super(itemView);
            tvGroup = itemView.findViewById(R.id.tv_group);
        }
    }
}
