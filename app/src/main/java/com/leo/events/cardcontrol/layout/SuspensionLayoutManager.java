package com.leo.events.cardcontrol.layout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuspensionLayoutManager extends RecyclerView.LayoutManager {
//    private int KEY = 2 << 21;

    private View view;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    Map<Integer, SuspensionPosition> mSuspensionPositions;

    {
        mSuspensionPositions = new HashMap<>();
    }

    public SuspensionLayoutManager() {

    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
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
        //0.为了不影响，应该先将最后一个添加的移除
        if (childCount > 0) {
            for (int i = childCount - 1; i >= 0; i--) {
                for (Map.Entry<Integer, SuspensionPosition> entry : mSuspensionPositions.entrySet()) {
                    SuspensionPosition value = entry.getValue();
                    if (i == value.realPosition) {
                        removeAndRecycleViewAt(i, recycler);
                    }
                }
            }
        }
        //1.计算
        childCount = getChildCount();
        if (childCount > 0) {
            firstPosition = getPosition(getChildAt(0));
            lastPosition = getPosition(getChildAt(childCount - 1));
            if (dy >= 0) {
                if (lastPosition == state.getItemCount() - 1 && getDecoratedBottom(getChildAt(childCount - 1)) - dy < getHeight() - getPaddingBottom()) {
                    dy = getDecoratedBottom(getChildAt(childCount - 1)) - (getHeight() - getPaddingBottom());
                }
            } else {
                if (firstPosition == 0 && getDecoratedTop(getChildAt(0)) - dy > getPaddingTop()) {
                    dy = getDecoratedTop(getChildAt(0)) - getPaddingTop();
                }
            }
            if (dy >= 0) {
                offset = getDecoratedTop(getChildAt(0));
            } else {
                offset = getDecoratedBottom(getChildAt(childCount - 1));
            }

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (dy >= 0) {
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


        //2.回收
        detachAndScrapAttachedViews(recycler);

        //3.布局  正反方向布局
        //child一定要铺满屏幕
        if (dy >= 0) { //正方向从头开始 offset 为第一个view的顶部
            for (int i = firstPosition; i <= getItemCount() - 1; i++) {
                View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, 0, 0);
                int height = getDecoratedMeasurementVertical(child);
                if (isOverlayViewHolder(child)) {
                    SuspensionPosition suspensionPosition = getSuspensionPosition(i);
                    suspensionPosition.position = i;
                    suspensionPosition.origin.right = getWidth() - getPaddingRight();
                    suspensionPosition.origin.top = (getHeight() - getDecoratedMeasuredHeight(child)) / 2;
                    suspensionPosition.origin.left = suspensionPosition.origin.right - getDecoratedMeasuredWidth(child);
                    suspensionPosition.origin.bottom = suspensionPosition.origin.top + getDecoratedMeasuredHeight(child);
                    suspensionPosition.status = 2;
                    removeAndRecycleView(child, recycler);
                    continue;
                }
                layoutDecoratedWithMargins(child, getPaddingLeft(), offset - dy, getDecoratedMeasuredWidth(child), offset + height - dy);

                if (isSuspensionViewHolder(child) && getDecoratedTop(child) - dy <= getPaddingTop()) { //添加
                    SuspensionPosition suspensionPosition = getSuspensionPosition(i);
                    suspensionPosition.position = i;
                    suspensionPosition.origin.left = getPaddingLeft();
                    suspensionPosition.origin.top = getPaddingTop();
                    suspensionPosition.origin.right = getDecoratedMeasuredWidth(child);
                    suspensionPosition.origin.bottom = height;
                    suspensionPosition.height = height;
                    suspensionPosition.status = 1;
                } else if (isSuspensionViewHolder(child) && hasSupensionViewHolder()) {
                    SuspensionPosition maxSuspensionPosition = getMaxSupensionViewHolder();
                    if (maxSuspensionPosition != null) {
                        if (getDecoratedTop(child) <= maxSuspensionPosition.origin.bottom) {
                            int susHeight = maxSuspensionPosition.origin.bottom - maxSuspensionPosition.origin.top;
                            maxSuspensionPosition.origin.bottom = offset - dy;
                            maxSuspensionPosition.origin.top = offset - dy - susHeight;
                        }
                    }

                }
                //fix 首个悬浮不能隐藏
                if (isSuspensionViewHolder(child) && getDecoratedTop(child) - dy >= getPaddingTop() && dy == 0) {
                    SuspensionPosition suspensionPosition = getSuspensionPosition(i);
                    if (suspensionPosition != null) {
                        mSuspensionPositions.remove(i);
                    }
                }

                offset += height;
                if (getDecoratedBottom(child) > getHeight()) {
                    break;
                }
            }

        } else { //反方向从尾开始  offset为屏幕最后一个view的底部

            for (int i = lastPosition; i >= 0; i--) {
                View child = recycler.getViewForPosition(i);
                addView(child, 0);
                measureChildWithMargins(child, 0, 0);
                if (isOverlayViewHolder(child)) {
                    removeAndRecycleView(child, recycler);
                    continue;
                }
                int height = getDecoratedMeasurementVertical(child);
                layoutDecoratedWithMargins(child, getDecoratedLeft(child), offset - height - dy, getDecoratedMeasuredWidth(child), offset - dy);
                if (isSuspensionViewHolder(child) && getDecoratedTop(child) - dy >= getPaddingTop()) { //移除悬浮
                    SuspensionPosition suspensionPosition = getSuspensionPosition(i);
                    if (suspensionPosition != null) {
                        mSuspensionPositions.remove(i);
                    }
                    SuspensionPosition maxSuspensionPosition = getMaxSupensionViewHolder();
                    if (maxSuspensionPosition != null && maxSuspensionPosition.origin.top <= getPaddingTop()) {
                        maxSuspensionPosition.origin.bottom = offset - height - dy;
                        maxSuspensionPosition.origin.top = maxSuspensionPosition.origin.bottom - height;
                        if (maxSuspensionPosition.origin.top > getPaddingTop()) {
                            maxSuspensionPosition.origin.top = getPaddingTop();
                            maxSuspensionPosition.origin.bottom = getPaddingTop() + height;
                        }

                    }
                }
                offset -= height;
                if (getDecoratedTop(child) < 0) {
                    break;
                }
            }
        }


        //4.添加悬浮的位置
        if (mSuspensionPositions != null && !mSuspensionPositions.isEmpty()) {
            SuspensionPosition belowSuspensionPosition = null;
            for (Map.Entry<Integer, SuspensionPosition> entry : mSuspensionPositions.entrySet()) {
                SuspensionPosition suspensionPosition = entry.getValue();
                suspensionPosition.realPosition = -1;
                if (suspensionPosition.status == 1) {
                    if (belowSuspensionPosition == null || suspensionPosition.position > belowSuspensionPosition.position) {
                        belowSuspensionPosition = suspensionPosition;
                    }
                } else if (suspensionPosition.status == 2) {
                    View view = recycler.getViewForPosition(suspensionPosition.position);
                    addView(view);
                    measureChildWithMargins(view, 0, 0);
                    suspensionPosition.realPosition = getChildCount() - 1;
                    layoutDecoratedWithMargins(view, suspensionPosition.origin.left, suspensionPosition.origin.top, suspensionPosition.origin.right, suspensionPosition.origin.bottom);
                }
            }

            if (belowSuspensionPosition != null) {
                View view = recycler.getViewForPosition(belowSuspensionPosition.position);
//                updateSuspensionViewHolder(view);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                belowSuspensionPosition.realPosition = getChildCount() - 1;
                layoutDecoratedWithMargins(view, belowSuspensionPosition.origin.left, belowSuspensionPosition.origin.top, belowSuspensionPosition.origin.right, belowSuspensionPosition.origin.bottom);
                Log.d("", "");
            }
        }


    }

    private SuspensionPosition getMaxSupensionViewHolder() {
        if (mSuspensionPositions.size() == 1){
            return null;
        }
        SuspensionPosition suspensionPosition = null;
        for (Map.Entry<Integer, SuspensionPosition> entry : mSuspensionPositions.entrySet()) {
            SuspensionPosition value = entry.getValue();
            if ((suspensionPosition == null || (value.position > suspensionPosition.position)) && value.status == 1) {
                suspensionPosition = value;
            }

        }
        return suspensionPosition;
    }

    private boolean hasSupensionViewHolder() {
        if (mSuspensionPositions.isEmpty()) {
            return false;
        }
        for (Map.Entry<Integer, SuspensionPosition> entry : mSuspensionPositions.entrySet()) {
            SuspensionPosition value = entry.getValue();
            if (value.status == 1) {
                return true;
            }
        }
        return false;
    }

    private SuspensionPosition getSuspensionPosition(int i) {
        SuspensionPosition suspensionPosition = mSuspensionPositions.get(i);
        if (suspensionPosition == null) {
            suspensionPosition = new SuspensionPosition();
            mSuspensionPositions.put(i, suspensionPosition);
        }
        return suspensionPosition;
    }

    private boolean isSuspensionViewHolder(View child) {
        Class clz = RecyclerView.LayoutParams.class;
        try {
            Field mViewHolder = clz.getDeclaredField("mViewHolder");
            mViewHolder.setAccessible(true);
            Object o = mViewHolder.get(child.getLayoutParams());
            if (o instanceof SuspensionViewHolder) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean isOverlayViewHolder(View child) {
        Class clz = RecyclerView.LayoutParams.class;
        try {
            Field mViewHolder = clz.getDeclaredField("mViewHolder");
            mViewHolder.setAccessible(true);
            Object o = mViewHolder.get(child.getLayoutParams());
            if (o instanceof OverlayViewHolder) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updateSuspensionViewHolder(View child) {

        try {
            Class clz = RecyclerView.LayoutParams.class;
            Field mViewHolder = clz.getDeclaredField("mViewHolder");
            mViewHolder.setAccessible(true);
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) mViewHolder.get(child.getLayoutParams());
            Class vhClz = RecyclerView.ViewHolder.class;
            Method addFlags = vhClz.getDeclaredMethod("addFlags", int.class);
            addFlags.setAccessible(true);
            Field updateFlagField = vhClz.getDeclaredField("FLAG_UPDATE");
            int updateFlog = (int) updateFlagField.get(null);
            addFlags.invoke(viewHolder, updateFlog);

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

    public static class SuspensionPosition {
        public int status;
        public int position;
        public Rect origin = new Rect();
        public int realPosition;
        public int height;
    }
}
