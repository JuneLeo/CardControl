package com.leo.events.cardcontrol.layout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 *
 */
public class LinearLayoutManagerEx extends LinearLayoutManager {

    public LinearLayoutManagerEx(Context context) {
        super(context);
    }

    public LinearLayoutManagerEx(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d("LinearLayoutManagerEx", "dy : " + dy);

        return super.scrollVerticallyBy(dy, recycler, state);
    }
}
