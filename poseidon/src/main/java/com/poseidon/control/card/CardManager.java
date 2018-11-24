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

    public CardManager() {
        this.mCardList = new ArrayList<>();
    }


    public void addCard(ICard card) {
        mCardList.add(card);
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
