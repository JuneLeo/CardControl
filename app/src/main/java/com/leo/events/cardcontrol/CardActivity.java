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
import com.poseidon.control.control.card.CardManager;
import com.poseidon.control.event.obsever.Subscriber;
import com.poseidon.control.wrapper.CardWrapper;
import java.util.Map;

/**
 * Created by spf on 2018/11/15.
 */
public class CardActivity extends AppCompatActivity {
    private CardControl mControl;
    private LinearLayout container;
    private LinearLayout titleContainer;
    private LinearLayout toolbarContainer;
    private CardWrapper mCardWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.poseidon.control.R.layout.activity_card);
        //控制器
        mControl = CardControl.create(CardGroup.MODULE_CARD);
        mCardWrapper = new CardWrapper(mControl);

        initLinearLayout();
        initEvent();
        bindCard();
        mControl.notifyAllView();





    }

    private void initLinearLayout() {
        container = findViewById(com.poseidon.control.R.id.ll_container);
        titleContainer = findViewById(com.poseidon.control.R.id.title_container);
        toolbarContainer = findViewById(com.poseidon.control.R.id.tool_bar);
        mCardWrapper.add(container);
        mCardWrapper.add(titleContainer);
        mCardWrapper.add(toolbarContainer);
    }

    private void bindCard() {
        for (Map.Entry<CardManager, LinearLayout> entry : mCardWrapper.getCardmanager().entrySet()) {
            LinearLayout linearLayout = entry.getValue();
            CardManager cardManager = entry.getKey();
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
        mControl.getObservable("box_click", String.class).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Toast.makeText(CardActivity.this, s, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mControl.destroy();
    }
}
