package com.poseidon.control.control.layout;

import android.view.ViewGroup;

import com.poseidon.control.control.card.CardManager;


public interface ILayoutManager {

    void onCreateView();

    void onUpdateView();

    void setContainer(ViewGroup container);

    ViewGroup getContainer();

    void setManager(CardManager manager);


}
