package com.poseidon.control.card;

import android.os.Bundle;

import com.poseidon.control.debug.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by spf on 2018/11/15.
 */
public class CardManager {
    private List<ICard> mCardList;

    private Map<String, Set<ICard>> mChangeMap;

    private Set<String> mConcernKeys;

    public CardManager() {
        this.mCardList = new ArrayList<>();
        this.mChangeMap = new HashMap<>();
        this.mConcernKeys = new HashSet<>();
    }


    public void addCardConcern(ICard card, String concern) {
        if (!mCardList.contains(card)) {
            return;
        }
        if (mChangeMap.get(concern) == null) {
            mChangeMap.put(concern, new HashSet<ICard>());
        }
        mChangeMap.get(concern).add(card);
    }

    public void addConcernKeys(String key) {
        mConcernKeys.add(key);
    }


    public List<ICard> getConcernCard() {
        List<ICard> result = new LinkedList<>();
        for (String key : mConcernKeys) {
            Set<ICard> set = mChangeMap.get(key);
            if (set != null) {
                result.addAll(set);
            }
        }
        if (result.size() == 0) {
            return mCardList;
        }
        mChangeMap.clear();
        return result;
    }


    public void addCard(ICard card) {
        card.onAttachCardManager(this);
        mCardList.add(card);
    }

    public void setCardList(List<ICard> cardList) {
        this.mCardList.clear();
        for (ICard card : cardList) {
            card.onAttachCardManager(this);
        }
        this.mCardList.addAll(cardList);
    }

    public List<ICard> getCards() {
        return mCardList;
    }

    public void performCreateCard(Bundle bundle) {
        for (ICard card : mCardList) {
            card.onCreate(bundle);
        }
    }

    public void performStartCard() {
        for (ICard card : mCardList) {
            card.onStart();
        }
    }

    public void performResumeCard() {
        for (ICard card : mCardList) {
            card.onResume();
        }
    }

    public void performPauseCard() {
        for (ICard card : mCardList) {
            card.onPause();
        }
    }

    public void performStopCard() {
        for (ICard card : mCardList) {
            card.onStop();
        }
    }

    public void performDestroyCard() {
        for (ICard card : mCardList) {
            card.onDestroy();
        }
    }


}
