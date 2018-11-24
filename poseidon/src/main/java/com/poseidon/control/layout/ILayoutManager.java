package com.poseidon.control.layout;

import android.view.ViewGroup;

import com.poseidon.control.card.CardManager;
import com.poseidon.control.card.ICard;


public interface ILayoutManager {

    void onCreateView();

    void onUpdateView();

    void setContainer(ViewGroup container);

    ViewGroup getContainer();

    void setManager(CardManager manager);


}
