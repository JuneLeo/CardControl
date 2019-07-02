package com.leo.events.cardcontrol.layout;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.reflect.Field;

public class SuspensionLayoutManager2 extends RecyclerView.LayoutManager {

    private int mOffsetY;



    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        super.onLayoutChildren(recycler, state);
        if (state.getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (state.isPreLayout()) {//state.isPreLayout()是支持动画的
            return;
        }
        reLayoutChildren(recycler, state, 0);

    }

    private int getDecoratedMeasurementVertical(View child) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        return getDecoratedMeasuredHeight(child) + lp.topMargin + lp.bottomMargin;
    }

    private void reLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        int childCount = getChildCount();
        int firstPosition = 0;
        int lastPosition = getItemCount() - 1;
        int offset = 0;
        if (childCount > 0) {
            firstPosition = getPosition(getChildAt(0));
            lastPosition = getPosition(getChildAt(childCount - 1));
            if (dy > 0) {
                offset = getDecoratedTop(getChildAt(0));
            } else {
                offset = getDecoratedBottom(getChildAt(childCount - 1));
            }

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (dy > 0) {
                    if (getDecoratedBottom(child) - dy < getPaddingTop()) {
                        firstPosition++;
                        offset = getDecoratedBottom(child);
                    }
                } else {
                    if (getDecoratedTop(child) - dy > getHeight() - getPaddingBottom()) {
                        lastPosition--;
                        offset = getDecoratedTop(child);
                    }
                }
            }
        }
        //回收
        detachAndScrapAttachedViews(recycler);
        //布局  正反方向布局
        //child一定要铺满屏幕
        if (dy >= 0) { //正方向从头开始 offset 为第一个view的顶部
            for (int i = firstPosition; i <= getItemCount() - 1; i++) {
                View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, 0, 0);
                int height = getDecoratedMeasurementVertical(child);

                layoutDecoratedWithMargins(child, getPaddingLeft(), offset - dy, getDecoratedMeasuredWidth(child), offset + height - dy);

                offset += height;
                if (getDecoratedBottom(child) > getHeight()) {
                    break;
                }
            }

        } else { //反方向从尾开始  offset为屏幕最后一个view的底部

            for (int i = lastPosition; i >= 0; i--) {
                View child = recycler.getViewForPosition(i);
                addView(child,0);
                measureChildWithMargins(child, 0, 0);
                int height = getDecoratedMeasurementVertical(child);
                layoutDecoratedWithMargins(child, getDecoratedLeft(child), offset - height - dy, getDecoratedMeasuredWidth(child), offset - dy);
                offset -= height;
                if (getDecoratedTop(child) < 0) {
                    break;
                }
            }
        }
    }

    private boolean isSuspensionViewHolder(View child) {
        Class clz = RecyclerView.LayoutParams.class;
        try {
            Field mViewHolder = clz.getDeclaredField("mViewHolder");
            mViewHolder.setAccessible(true);
            Object o = mViewHolder.get(child.getLayoutParams());
            if (o instanceof SuspensionViewHolder){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        reLayoutChildren(recycler, state, dy);
//        offsetChildrenVertical(-dy);
        return dy;
    }


    @Override
    public boolean canScrollHorizontally() {
        return false;
    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
