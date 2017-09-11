package com.dak.weakview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
 * 自定义可动态更新的纵向/横向列表布局
 * Created by bazengliang on 2017/1/16.
 */
public class WeakLinearLayout extends LinearLayout implements WeakViewAdapter.OnNotifyDataLisetener{
    private WeakViewAdapter adapter;

    public WeakLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(WeakViewAdapter adapter) {
        this.adapter = adapter;
        adapter.setOnNotifyDataLisetener(this);
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
        int removeCount = getChildCount() - adapter.getViewHolderCount();
        while (removeCount > 0) {
            this.removeViewAt(getChildCount() - 1);
            removeCount--;
        }
    }
}

