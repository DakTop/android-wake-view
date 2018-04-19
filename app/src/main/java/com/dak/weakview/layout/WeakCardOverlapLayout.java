package com.dak.weakview.layout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dak.weakview.adapter.WeakViewAdapter;
import com.dak.weakview.utils.Tool;

/**
 * 层叠卡片列表
 * Created by dak on 2018/4/8.
 */
public class WeakCardOverlapLayout extends RelativeLayout implements WeakViewAdapter
        .OnNotifyDataLisetener {
    private WeakViewAdapter adapter;
    private ViewDragHelper viewDragHelper;
    //层叠卡片的个数
    private int cardCount = 3;
    private boolean isRemoveChil = false;
    private int moveX;
    private int moveY;
    //层叠卡片的缩放比例，无缩放为0
    private final float scaleVal = 0.15f;
    //层叠的卡片层次的高度
    private int viewStackUpHeight = 15;
    private WeakCardOverlapLayout.OnItemClickListener itemClickListener;
    //子View状态初始化标识
    private boolean initChilState = false;
    //子View在数据集中的位置
    private int position = 0;

    public WeakCardOverlapLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setClipChildren(false);
        viewStackUpHeight = Tool.dip2px(context, viewStackUpHeight / 2);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new DragCallbackImpl());
    }

    /**
     * 设置子View是否能超过父View边界。
     *
     * @param c
     */
    public void setParentClipChild(boolean c) {
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null) {
            viewGroup.setClipChildren(false);
        }
    }


    public void setAdapter(WeakViewAdapter adapter) {
        this.adapter = adapter;
        adapter.setViewGroupParent(this);
        adapter.setOnNotifyDataLisetener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1) {
            //当显示的View大于1的时候，就会出现层叠效果，此时父ViewGroup需要重新计算高度，即需要增加高度，而增加的在这个高度就是设置viewStackUpHeight乘以卡片个数减一。
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + (viewStackUpHeight * (getChildCount() - 1)));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (initChilState) {
            int size = getChildCount();
            for (int i = 0; i < size; i++) {
                initChilState(size - 1 - i, getChildAt(i));
            }
            initChilState = false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                requestDisallowInterceptTouchEventAll();
                break;
            case MotionEvent.ACTION_DOWN:
                requestDisallowInterceptTouchEventAll();
                break;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                requestDisallowInterceptTouchEventAll();
                break;
            case MotionEvent.ACTION_CANCEL:
                requestDisallowInterceptTouchEventAll();
                break;
        }
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    class DragCallbackImpl extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //这里判断只能滑动第一个View,并且只有一个View的时候不能滑动。
            return getChildCount() <= 1 ? false : getChildAt(getChildCount() - 1) == child;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //这里控制滑动View的横向边界
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //这里控制滑动View的纵向边界
            return top;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return Integer.MAX_VALUE;
        }

        /**
         * 拖动View松手时调用此方法。
         *
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            float curX = releasedChild.getX();
            float curY = releasedChild.getY();
            int vW = releasedChild.getWidth();
            int vH = releasedChild.getHeight();
            moveX = (int) (curX * 2.3);
            moveY = (int) (curY * 2.3);
            //如果在X或者Y抽方向上移动的距离大于卡片宽度或者高度一半的话，则移除屏幕之外
            if (Math.abs(curX) > vW / 2 || Math.abs(curY) > vH / 2) {
                //这里简单的将在x、y轴滑动的距离扩大2倍当做卡片移动的方向
                viewDragHelper.smoothSlideViewTo(releasedChild, moveX, moveY);
                isRemoveChil = true;
            } else {
                viewDragHelper.smoothSlideViewTo(releasedChild, 0, 0);
            }
            invalidate();
        }

        /**
         * 正在被拖动的View或者自动滚动的View的位置改变时会调用此方法。
         *
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            //这里首先判断正在改变位置的View是否是刚刚拖动松手后滑出屏幕外的View，如果是则继续判断，它是否已经完全滑动出屏幕外。
            if (isRemoveChil && moveX != 0 && moveY != 0 && Math.abs(left) >= Math.abs(moveX) && Math.abs(top) >= Math.abs(moveY)) {
                isRemoveChil = false;
                moveX = 0;
                moveY = 0;
                notifyView();
            }
        }
    }

    private void requestDisallowInterceptTouchEventAll() {
        getParent().requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void onInsertNotifyDataLisetener() {
        //这里控制显示的view的个数不能超过设置的cardSize。
        int insertCount = Math.min(cardCount, adapter.getItemCount());
        int childCount = getChildCount();
        if (childCount > 0) {
            if (childCount >= cardCount) {
                return;
            } else {
                insertCount = insertCount - childCount;
            }
        }
        for (int i = 0; i < insertCount; i++) {
            addBottomView(i, adapter.getHolderView(i + childCount));
        }
        bottomViewHolderPosition = insertCount - 1;
        position = 0;
        initChilState = true;
    }

    /**
     * 当把第一个卡片滑出屏幕后需要更新界面以及ViewHolder列表。
     */
    private void notifyView() {
        //第一个ViewHolder滑出屏幕后，将这个ViewHolder添加到ViewHolder列表的尾部，并移除列表的第一个ViewHolder以起到View复用以及循环在展示列表数据集的效果
        adapter.getViewHolderList().remove(0);
        adapter.getViewHolderList().add(adapter.onCreateViewHolder(this));
        int dataSize = adapter.getItemCount();
        adapter.notifyItemViewWithHolder(dataSize - 1, position);
        position++;
        if (position >= dataSize) {
            position = 0;
        }
        int chilCount = getChildCount();
        //记录下被划出屏幕的View在Y轴上移动的距离。
        float lastTransY = getChildAt(chilCount - 1).getTranslationY();
        //删除被滑出屏幕的View
        removeViewAt(chilCount - 1);
        chilCount = getChildCount();
        //缩放和移动剩下的View。
        for (int i = chilCount - 1; i >= 0; i--) {
            View view = getChildAt(i);
            //获取当前View的在Y轴上的移动距离
            float thisTransY = view.getTranslationY();
            //当前View只要移动上一个View在Y轴上移动的距离即可。
            ValueAnimator translation = ObjectAnimator.ofFloat(view, "translationY", lastTransY);
            //当前View放大到滑出屏幕的View的大小，他们的差值为scaleVal。
            ValueAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", view.getScaleX() + scaleVal);
            ValueAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", view.getScaleY() + scaleVal);
            translation.setDuration(150);
            scaleX.setDuration(150);
            scaleY.setDuration(150);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(translation).with(scaleX).with(scaleY);
            animatorSet.start();
            //用lastTransY变量记录当前View的在Y轴上的移动距离
            lastTransY = thisTransY;
        }
        int cardBottomPosition = Math.min(cardCount, dataSize) - 1;
        View view = adapter.getHolderView(cardBottomPosition);
        bottomViewHolderPosition++;
        if (bottomViewHolderPosition >= dataSize) {
            bottomViewHolderPosition = 0;
        }
        addBottomView(bottomViewHolderPosition, view);
        initChilState(cardBottomPosition, view);
    }

    int bottomViewHolderPosition = cardCount - 1;

    private void addBottomView(final int position, final View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onWeakItemClickListener(position, view);
                }
            }
        });
        addView(view, 0);
    }

    /**
     * 在底部补添一个View
     *
     * @param position
     */
    private void initChilState(final int position, View view) {
        //等于第一个无缩放View的高度。
        int firstViewH = getChildAt(getChildCount() - 1).getMeasuredHeight();
        //计算当前View的缩放值，按照设置的scaleVal值依次递减。
        float scale = 1 - (position * scaleVal);
        view.setScaleX(scale);
        view.setScaleY(scale);
        //被添加的所有的卡片缩放后都是在父布局的正中心的，所以要计算每个卡片需要向下移动的高度来显示出层叠的效果。这个高度可以根据下面的公式自己理解。
        float ty = ((firstViewH - (firstViewH * scale)) / 2) + viewStackUpHeight * position;
        view.setTranslationY(ty);
    }

    @Override
    public void onDeleteNotifyDataLisetener() {
        int removeCount = getChildCount() - adapter.getViewHolderCount();
        while (removeCount > 0) {
            this.removeViewAt(0);
            removeCount--;
        }
    }

    public WeakViewAdapter getAdapter() {
        return adapter;
    }

    /**
     * 显示卡片的个数
     *
     * @param cardCount
     */
    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    /**
     * 卡片底部层叠层次的高度。
     *
     * @param viewStackUpHeight
     */
    public void setViewStackUpHeight(int viewStackUpHeight) {
        this.viewStackUpHeight = viewStackUpHeight;
    }

    public void setOnItemClickListener(WeakCardOverlapLayout.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onWeakItemClickListener(int position, View view);
    }
}
