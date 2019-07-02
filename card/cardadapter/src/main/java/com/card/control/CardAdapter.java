package com.card.control;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.card.generator.IGenerator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.card.generator.*;

import java.util.List;

/**
 * Created by kuangwen on 16/7/19.
 */
public class CardAdapter extends RecyclerView.Adapter {
    protected List<BaseCard> mList;
    protected Context mContext;
    protected SparseArray<ItemViewProvider> mProviders;
    protected static final List<String> mCardPool = new ArrayList<>();
    protected static final List<String> mProviderPool = new ArrayList<>();

    public CardAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        this.mProviders = new SparseArray<>();
    }

    static {
        try {
            Class<?> aClass = Class.forName("com.card.generator.Generator");
            IGenerator iGenerator = (IGenerator) aClass.newInstance();
            iGenerator.initCardTable(mCardPool, mProviderPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(BaseCard card) {
        if (card != null) {
            mList.add(card);
            notifyDataSetChanged();
        }
    }

    public void add(int location, BaseCard card) {
        if (card != null) {
            mList.add(location, card);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemOnclick(int position);
    }


    protected OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        BaseCard baseCard = mList.get(position);
        return mCardPool.indexOf(baseCard.getClass().getName());
    }

    public BaseCard getItem(int position) {
        if (position >= 0 && position <= mList.size() - 1) {
            return mList.get(position);
        }
        return null;
    }

    public void clear() {
        if (!mList.isEmpty()) {
            mList.clear();
            notifyDataSetChanged();
        }

    }

    public void addAll(List<BaseCard> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public List<BaseCard> getData() {
        return mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemViewProvider provider = getProviderByViewType(viewType);

        if (provider == null) {
            return null;
        }
        return provider.onCreateViewHolder(inflater, parent);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewProvider provider = getProviderByViewType(getItemViewType(position));

        if (provider == null) {
            return;
        }
        provider.onBindViewHolder(holder, getItem(position), position);
    }

    public ItemViewProvider getProviderByViewType(int viewType) {
        ItemViewProvider provider = mProviders.get(viewType);
        if (provider == null) {
            try {
                Constructor c = Class.forName(mProviderPool.get(viewType)).getConstructor(OnItemClickListener.class);
                provider = (ItemViewProvider) c.newInstance(mOnItemClickListener);
                if (provider != null) {
                    mProviders.put(viewType, provider);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return provider;
    }

    public List<BaseCard> getList() {
        return mList;
    }

    synchronized public BaseCard findBaseCardIndexByClazz(Class<?> clazz) {

        for (BaseCard baseCard : mList) {
            if (clazz.isAssignableFrom(baseCard.getClass())) {
                return baseCard;
            }
        }
        return null;
    }

    synchronized public List<BaseCard> findBaseCardsIndexByClazz(Class<?> clz) {
        List<BaseCard> result = new ArrayList<>();

        for (BaseCard baseCard : mList) {
            if (clz.isAssignableFrom(baseCard.getClass())) {
                result.add(baseCard);
            }
        }
        return result;
    }


}
