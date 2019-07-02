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
import com.leo.events.cardcontrol.model.FourModel;
import com.leo.events.cardcontrol.layout.SuspensionViewHolder;

@CardMap(FourModel.class)
public class FourProvider extends ItemViewProvider<FourModel, FourProvider.FourViewHolder> {


    public FourProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public FourViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new FourViewHolder(inflater.inflate(R.layout.item_suspension, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FourViewHolder holder, @NonNull FourModel card, final int position) {
        holder.itemView.setBackgroundColor(0xffff00ff);
        holder.textView.setText("悬浮 " + position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class FourViewHolder extends SuspensionViewHolder {
        public TextView textView;
        public FourViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }
}
