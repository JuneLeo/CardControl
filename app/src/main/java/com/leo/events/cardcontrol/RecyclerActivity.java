package com.leo.events.cardcontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.card.control.BaseCard;
import com.card.control.CardAdapter;
import com.leo.events.cardcontrol.layout.SuspensionLayoutManager;
import com.leo.events.cardcontrol.model.CircleCard;
import com.leo.events.cardcontrol.model.Name;
import com.leo.events.cardcontrol.model.NameCard;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("自定义LayoutManager");
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new SuspensionLayoutManager());
        CardAdapter cardAdapter = new CardAdapter(this);
        recyclerView.setAdapter(cardAdapter);
        List<BaseCard> list = Name.transfrom(this);
        if (list != null) {
            cardAdapter.addAll(list);
            cardAdapter.add(2, new CircleCard());
            for (int i = 0; i<1; i++) {
                NameCard nameCard = new NameCard();
                nameCard.name = "宋鹏飞";
                cardAdapter.add(0, nameCard);
            }
        }
    }
}
