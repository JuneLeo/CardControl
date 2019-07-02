package com.card.control;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;


/***
 * @author drakeet
 */
public abstract class ItemViewProvider<C extends BaseCard, V extends ViewHolder> {
    protected CardAdapter.OnItemClickListener mOnItemClickListener;

    public ItemViewProvider(CardAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    @NonNull
    public abstract V onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    public abstract void onBindViewHolder(@NonNull V holder, @NonNull C card, int position);
}
