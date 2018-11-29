package com.poseidon.control.control.card;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

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
