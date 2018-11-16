package com.leo.events.cardcontrol.card.banner;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.events.cardcontrol.R;
import com.poseidon.control.view.AbstractView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spf on 2018/11/15.
 */
public class BannerCardView extends AbstractView<BannerPresenter, BannerCardVM> {
    ViewPager viewpager;

    @Override
    public View onCreateView(final ViewGroup parent) {
        Context context = parent.getContext();
        final View v = LayoutInflater.from(context).inflate(R.layout.card_banner, null);
        viewpager = v.findViewById(R.id.viewpager);
        final List<View> views = getListView(v);
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        return v;
    }

    private List<View> getListView(View v) {
        List<View> views = new ArrayList<>();
        View view = new View(v.getContext());
        view.setBackgroundColor(Color.BLUE);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        views.add(view);

        View view2 = new View(v.getContext());
        view2.setBackgroundColor(Color.RED);
        view2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        views.add(view2);

        return views;
    }

    @Override
    public void onUpdateView(View view, ViewGroup parent) {

    }
}
