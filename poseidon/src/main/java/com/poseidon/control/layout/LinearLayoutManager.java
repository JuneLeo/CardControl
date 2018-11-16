package com.poseidon.control.layout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.poseidon.control.card.CardManager;
import com.poseidon.control.card.ICard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LinearLayoutManager implements ILayoutManager {

    private LinearLayout container;

    private Map<ICard, View> cachedViewMap;


    private CardManager manager;

    public LinearLayoutManager() {
        cachedViewMap = new HashMap<>();
    }

    @Override
    public void setManager(CardManager manager) {
        this.manager = manager;
    }

    @Override
    public void onCreateView() {
        List<ICard> cardList = manager.getCards();
        for (int i = 0, size = cardList.size(); i < size; i++) {
            createViewInternal(cardList, cardList.get(i));
        }
    }
    @Override
    public void onUpdateView() {
        List<ICard> cardList = manager.getCards();
        for (int i = 0, size = cardList.size(); i < size; i++) {
            ICard currentCard = cardList.get(i);
            updateViewInternal(cardList, currentCard);
        }
    }

    @Override
    public void onCreateCardView(ICard card) {
        List<ICard> cardList = manager.getConcernCard();
        if (!cardList.contains(card)) {
            return;
        }
        createViewInternal(cardList, card);
    }



    @Override
    public void onUpdateCardView(ICard card) {
        List<ICard> cardList = manager.getConcernCard();
        if (!cardList.contains(card)) {
            return;
        }
        updateViewInternal(cardList, card);
    }

    private void createViewInternal(List<ICard> cardList, ICard currentCard) {
        if (!currentCard.getCardView().isAvailable()) {
            return; //view认为自己不需要被初始化，不添加
        }
        View v = currentCard.getCardView().onCreateView(container);
        int positionIndex = 0;
        for (ICard card : cardList) {
            if (card == currentCard) {
                break;
            }
            if (card.getCardView().isAvailable()) {
                positionIndex++;
            }
        }
        if (positionIndex < container.getChildCount()) {
            container.addView(v, positionIndex);
        } else {
            container.addView(v);
        }
        currentCard.getCardView().onUpdateView(v,container);
        cachedViewMap.put(currentCard, v);
    }

    private void updateViewInternal(List<ICard> cardList, ICard currentCard) {
        if (cachedViewMap.get(currentCard) == null) {
            createViewInternal(cardList, currentCard);
            currentCard.getCardView().onUpdateView(cachedViewMap.get(currentCard),  container);
        } else {
            View cachedView = cachedViewMap.get(currentCard);
            if (currentCard.getCardView().useRecycledView()) {
                currentCard.getCardView().onUpdateView(cachedView, container);
            } else {
                int viewIndex = container.indexOfChild(cachedView);
                View v = currentCard.getCardView().onCreateView( container);
                container.removeView(cachedView);
                container.addView(v, viewIndex);
                currentCard.getCardView().onUpdateView(v, container);
                cachedViewMap.put(currentCard, v);
            }
        }
    }

    @Override
    public void setContainer(ViewGroup container) {
        if (!(container instanceof LinearLayout)) {
            throw new IllegalArgumentException("container need LinearLayout");
        }
        this.container = (LinearLayout) container;
    }

    @Override
    public ViewGroup getContainer() {
        return container;
    }
}
