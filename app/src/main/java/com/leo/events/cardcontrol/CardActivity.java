package com.leo.events.cardcontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.leo.events.cardcontrol.card.banner.BannerCard;
import com.leo.events.cardcontrol.card.box.BoxCard;
import com.leo.events.cardcontrol.card.func.FuncCard;
import com.leo.events.cardcontrol.card.tab.TabCard;
import com.leo.events.cardcontrol.card.title.TitleCard;
import com.leo.events.cardcontrol.card.toolbar.ToolbarCard;
import com.leo.events.cardcontrol.card.vip.VipCard;
import com.poseidon.control.CardControl;
import com.poseidon.control.card.CardManager;
import com.poseidon.control.card.ICard;
import com.poseidon.control.layout.LinearLayoutManager;
import com.poseidon.control.obsever.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spf on 2018/11/15.
 */
public class CardActivity extends AppCompatActivity {
    private CardControl mControl;
    private Map<LinearLayoutManager, CardManager> maps = new HashMap<>();
    private LinearLayout container;
    private LinearLayout titleContainer;
    private LinearLayout toolbarContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.poseidon.control.R.layout.activity_card);
        //控制器
        mControl = new CardControl();
        initLinearLayout();
        initEvent();
        initOtherEvent();
        bindCard();
        mControl.updateViewAll();

    }



    private void initLinearLayout() {
        //三个布局
        List<LinearLayout> linearLayouts = new ArrayList<>();
        container = findViewById(com.poseidon.control.R.id.ll_container);
        titleContainer = findViewById(com.poseidon.control.R.id.title_container);
        toolbarContainer = findViewById(com.poseidon.control.R.id.tool_bar);
        // 1.LinearLayout
        linearLayouts.add(container);
        linearLayouts.add(titleContainer);
        linearLayouts.add(toolbarContainer);

        for (LinearLayout linearLayout : linearLayouts) {
            //2.LinearLayoutManager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager();
            linearLayoutManager.setContainer(linearLayout);

            //3.CardManager
            CardManager cardManager = new CardManager();
            linearLayoutManager.setManager(cardManager);
            maps.put(linearLayoutManager, cardManager);
        }
    }

    private void bindCard() {
        for (Map.Entry<LinearLayoutManager, CardManager> entry : maps.entrySet()) {
            LinearLayoutManager key = entry.getKey();
            CardManager cardManager = entry.getValue();
            LinearLayout linearLayout = (LinearLayout) key.getContainer();
            if (linearLayout == titleContainer) { //中间图标容器
                cardManager.addCard(new TitleCard(mControl));
            } else if (linearLayout == container) {  // 中间容器
                cardManager.addCard(new BannerCard(mControl));
                cardManager.addCard(new TabCard(mControl));
                cardManager.addCard(new VipCard(mControl));
                cardManager.addCard(new BoxCard(mControl));
                cardManager.addCard(new FuncCard(mControl));

            } else if (linearLayout == toolbarContainer) { // toolbar容器
                cardManager.addCard(new ToolbarCard(mControl));
            }
        }
    }


    private void initEvent() {
        //创建view的观察者
        mControl.getObservable(CardControl.CREATE_VIEW_KEY, Object.class, null).subscribe(new Subscriber<Object>() {
            @Override
            public void onNext(Object o) {
                if (o == null) {
                    for (Map.Entry<LinearLayoutManager, CardManager> entry : maps.entrySet()) {
                        entry.getKey().onCreateView();
                    }
                } else if (o instanceof ICard) {
                    for (Map.Entry<LinearLayoutManager, CardManager> entry : maps.entrySet()) {
                        ICard targetBlock = (ICard) o;
                        entry.getKey().onCreateCardView(targetBlock);
                    }
                }
            }
        });
        mControl.getObservable(CardControl.UPDATE_VIEW_KEY, Object.class, null).subscribe(new Subscriber<Object>() {
            @Override
            public void onNext(Object o) {
                if (o == null) {
                    for (Map.Entry<LinearLayoutManager, CardManager> entry : maps.entrySet()) {
                        entry.getKey().onUpdateView();
                    }
                } else if (o instanceof ICard) {
                    for (Map.Entry<LinearLayoutManager, CardManager> entry : maps.entrySet()) {
                        ICard targetCard = (ICard) o;
                        entry.getKey().onUpdateCardView(targetCard);
                    }
                }
            }
        });
    }



    private void initOtherEvent() {
        mControl.getObservable("box_click",String.class).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Toast.makeText(CardActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
