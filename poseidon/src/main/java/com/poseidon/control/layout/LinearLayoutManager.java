package com.poseidon.control.layout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.poseidon.control.card.CardManager;
import com.poseidon.control.card.ICard;
import com.poseidon.control.view.ICardView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LinearLayoutManager implements ILayoutManager {

    private LinearLayout container;

    private CardManager manager;

    public LinearLayoutManager() {
    }

    @Override
    public void setManager(CardManager manager) {
        this.manager = manager;
    }

    @Override
    public void setContainer(ViewGroup container) {
        if (!(container instanceof LinearLayout)) {
            throw new IllegalArgumentException("container need LinearLayout");
        }
        this.container = (LinearLayout) container;
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

    private View createViewInternal(List<ICard> cardList, ICard currentCard) {
        ICardView iCardView = currentCard.getCardView();
        if (!iCardView.isAvailable()) {
            return null;
        }
        View v = iCardView.onCreateView(container);
        iCardView.onBindView(v, container); //绑定view和container
        int positionIndex = 0;
        for (ICard card : cardList) {
            if (card == currentCard) {
                break;
            }
            if (iCardView.isAvailable()) {
                positionIndex++;
            }
        }
        if (positionIndex < container.getChildCount()) {
            container.addView(v, positionIndex);
        } else {
            container.addView(v);
        }
        return v;
    }

    private void updateViewInternal(List<ICard> cardList, ICard currentCard) {
        ICardView cardView = currentCard.getCardView();
        if (!cardView.isAvailable()) {
            return;
        }
        if (cardView.getView() == null || cardView.getRootView() == null) {
            View viewInternal = createViewInternal(cardList, currentCard);
            cardView.onUpdateView(viewInternal, container);
        } else {
            View cachedView = cardView.getView();
            if (cardView.useRecycledView()) {
                cardView.onUpdateView(cachedView, container);
            } else {
                int viewIndex = container.indexOfChild(cachedView);
                View v = cardView.onCreateView(container);
                container.removeView(cachedView);
                container.addView(v, viewIndex);
                cardView.onUpdateView(v, container);
            }
        }
    }

    @Override
    public ViewGroup getContainer() {
        return container;
    }
}
