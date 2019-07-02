package com.leo.events.cardcontrol.layout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class FlowLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    private int mVerticalOffset;// 竖直偏移量，每次换行时，要根据这个offset判断
    private int mFirstVisiPos;// 屏幕可见的第一个view的Position
    private int mLastVisiPos; // 屏幕可见最后一个view的Position

    private SparseArray<Rect> mItemRects;//key 是View的position，保存View的bounds 和 显示标志，

    {
        mItemRects = new SparseArray<>();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() == 0) {  //没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {//state.isPreLayout()是支持动画的
            return;
        }
        //onLayoutChildren方法在RecyclerView 初始化时 会执行两遍
        detachAndScrapAttachedViews(recycler);

        //初始化区域
        mVerticalOffset = 0;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount();

        //初始化时调用 填充childView
        fill(recycler, state,0);

    }

    /**
     * dy 大于0 页面向上滚动，我们向下浏览，小于0，页面向下滚动，我们向上浏览
     *
     * @param recycler
     * @param state
     * @param dy
     */
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        int topOffset = getPaddingTop(); // recyclerView的 top padding

        if (getChildCount() > 0) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (dy > 0) {   //页面向上滚动，如果第一view的 bottom - dy 滚出了屏幕回收
                    if (getDecoratedBottom(child) - dy < getPaddingTop()) {
                        removeAndRecycleView(child, recycler);
                        mFirstVisiPos++;
                    }
                } else if (dy < 0) { //页面向下滚动，如果最后一个view 的top - dy（最后一个view的top+滚动的距离） 滚出了屏幕回收
                    if (getDecoratedTop(child) - dy > getHeight() - getPaddingBottom()) {
                        removeAndRecycleView(child, recycler);
                        mLastVisiPos--;
                    }
                }
            }
        }


        int leftOffset = getPaddingLeft(); // recyclerview的 left padding
        int lineMaxHeight = 0; // 每一行最大的高度
        if (dy >= 0) {
            int minPos = mFirstVisiPos;
            mLastVisiPos = getItemCount() - 1;
            if (getChildCount() > 0) {  //非首次，已经有view存在
                View lastView = getChildAt(getChildCount() - 1);
                minPos = getPosition(lastView) + 1;
                topOffset = getDecoratedTop(lastView); // 最后一个view的底部距离容器size
                leftOffset = getDecoratedRight(lastView);
                lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(lastView));

            }

            for (int i = minPos; i <= mLastVisiPos; i++) {
                View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, 0, 0);

                int childWidthOffset = getDecoratedMeasurementHorizontal(child);
                if (leftOffset + childWidthOffset <= getHorizontalSpace()) {  //一行可以放下
                    int childHeightOffset = getDecoratedMeasurementVertical(child);
                    layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + childWidthOffset, topOffset + childHeightOffset);

                    Rect rect = new Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + childWidthOffset, topOffset + childHeightOffset + mVerticalOffset);

                    mItemRects.put(i, rect);

                    leftOffset += childWidthOffset;

                    lineMaxHeight = Math.max(lineMaxHeight, childHeightOffset);

                }else { // 一行放不下，换行
                    leftOffset = getPaddingLeft();
                    topOffset += lineMaxHeight;
                    lineMaxHeight = 0;

                    if (topOffset - dy > getHeight() - getPaddingBottom()) { // 如果排到了超出屏幕
                        removeAndRecycleView(child, recycler);
                        mLastVisiPos = i - 1;
                    } else {
                        int childHeightOffset = getDecoratedMeasurementVertical(child);
                        layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + childWidthOffset, topOffset + childHeightOffset);

                        Rect rect = new Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + childWidthOffset, topOffset + childHeightOffset + mVerticalOffset);

                        mItemRects.put(i, rect);

                        leftOffset += childWidthOffset;

                        lineMaxHeight = Math.max(lineMaxHeight, childHeightOffset);
                    }
                }
            }
            //添加完后，判断是否已经没有更多的ItemView，并且此时屏幕仍有空白，则需要修正dy
            View lastChild = getChildAt(getChildCount() - 1);//获取最后一个view
            if (getPosition(lastChild) == getItemCount() - 1){
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild); //
                if (gap > 0){ //表示没有排列到容器底部，容器太大
                    dy -= gap;
                }
            }

        }else{
            /**
             * ##  利用Rect保存子View边界
             正序排列时，保存每个子View的Rect，逆序时，直接拿出来layout。
             */
            int maxPos = getItemCount() - 1;
            mFirstVisiPos = 0;
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                maxPos = getPosition(firstView) - 1;
            }
            for (int i = maxPos; i >= mFirstVisiPos; i--) {
                Rect rect = mItemRects.get(i);

                if (rect.bottom - mVerticalOffset - dy < getPaddingTop()) {
                    mFirstVisiPos = i + 1;
                    break;
                } else {
                    View child = recycler.getViewForPosition(i);
                    addView(child, 0);//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                    measureChildWithMargins(child, 0, 0);

                    layoutDecoratedWithMargins(child, rect.left, rect.top - mVerticalOffset, rect.right, rect.bottom - mVerticalOffset);
                }
            }
        }

        return dy;
    }

    private int getDecoratedMeasurementVertical(View child) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        return getDecoratedMeasuredHeight(child) + lp.topMargin + lp.bottomMargin;
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getDecoratedMeasurementHorizontal(View child) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        return getDecoratedMeasuredWidth(child) + lp.leftMargin + lp.rightMargin;
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //位移0、没有子View 当然不移动
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }

        int realOffset = dy;//实际滑动的距离， 可能会在边界处被修复
        //边界修复代码
        if (mVerticalOffset + realOffset < 0) {//上边界(滑出去，再滑回来，刚好0，修复)
            realOffset = -mVerticalOffset;
        } else if (realOffset > 0) {//下边界
            //利用最后一个子View比较修正
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }

        realOffset = fill(recycler, state, realOffset);//先填充，再位移。

        mVerticalOffset += realOffset;//累加实际滑动距离

        offsetChildrenVertical(-realOffset);//滑动

        return realOffset;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }


    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally();
    }


}
