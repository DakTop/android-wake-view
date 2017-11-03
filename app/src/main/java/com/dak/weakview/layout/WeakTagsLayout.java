package com.dak.weakview.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.dak.weakview.R;
import com.dak.weakview.adapter.WeakTagsAdapter;
import com.dak.weakview.adapter.WeakViewAdapter;

/**
 * Tag标签布局
 * Created by runTop on 2017/10/31.
 */
public class WeakTagsLayout extends ViewGroup implements WeakViewAdapter.OnNotifyDataLisetener {
    private WeakTagsAdapter adapter;
    /**
     * 行高度
     */
    private int lineHeight = 0;

    /**
     * 元素之间左右的间隔
     */
    private float horizontalColumnSpace = 0;
    /**
     * 行与行之间的间隔
     */
    private float verticalLineSpace = 0;

    public WeakTagsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeakTagsLayout);
        horizontalColumnSpace = typedArray.getDimension(R.styleable.WeakTagsLayout_layout_horizontalColumnSpace, 0);
        verticalLineSpace = typedArray.getDimension(R.styleable.WeakTagsLayout_layout_verticalLinesSpace, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = this.getChildCount();
        int lines = 1;
        float lineFixedWidth = MeasureSpec.getSize(widthMeasureSpec);//固定行宽
        float currLineWidth = 0;
        float paddingLeft = getPaddingLeft();
        float paddingRight = getPaddingRight();
        if ((lineFixedWidth -= paddingLeft + paddingRight) <= 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View viewChild = getChildAt(i);
            lineHeight = Math.max(lineHeight, viewChild.getMeasuredHeight());
            measureChild(viewChild, widthMeasureSpec, heightMeasureSpec);
            //当子View的宽度和horizontalLinesSpace的累加大于lineFixedWidth则另起一行
            if ((currLineWidth += viewChild.getMeasuredWidth()) > lineFixedWidth) {
                //另起一行事需要添加horizontalLinesSpace的值
                currLineWidth = viewChild.getMeasuredWidth();
                lines++;
            }
            currLineWidth += horizontalColumnSpace;
        }
        //如果只有一行而且layout_width设置的是wrap_content则设置当前控件宽度为行宽
        if (lines == 1 && MeasureSpec.getSize(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            lineFixedWidth = currLineWidth - horizontalColumnSpace + paddingLeft + paddingRight;
        } else {
            lineFixedWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            setMeasuredDimension((int) lineFixedWidth, MeasureSpec.getSize(heightMeasureSpec));
        } else {
            setMeasuredDimension((int) lineFixedWidth, (int) (lineHeight * lines + verticalLineSpace * (lines - 1) + getPaddingTop() + getPaddingBottom()));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }
        float paddingLeft = getPaddingLeft();
        float layoutWidth = getWidth() - getPaddingRight();
        float currentLeft = paddingLeft;
        float currentTop = getPaddingTop();
        int currentBottom = (int) currentTop + lineHeight;
        for (int i = 0; i < childCount; i++) {
            View chidView = getChildAt(i);
            if ((currentLeft + chidView.getMeasuredWidth()) > layoutWidth) {
                currentLeft = paddingLeft;
                currentTop += (int) (lineHeight + verticalLineSpace);
                currentBottom = (int) currentTop + lineHeight;
            }
            chidView.layout((int) currentLeft, (int) currentTop, (int) (currentLeft += chidView.getMeasuredWidth()), currentBottom);
            //
            currentLeft += horizontalColumnSpace;
        }
    }

    @Override
    public void onInsertNotifyDataLisetener() {
        int insertCount = adapter.getViewHolderCount() - getChildCount();
        for (int i = 0; i < insertCount; i++) {
            this.addView(adapter.getHolderView(getChildCount()));
        }
    }

    @Override
    public void onDeleteNotifyDataLisetener() {

    }

    public void setAdapter(WeakTagsAdapter adapter) {
        this.adapter = adapter;
        adapter.setOnNotifyDataLisetener(this);
    }
}