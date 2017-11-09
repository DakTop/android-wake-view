package com.dak.weakview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.dak.weakview.adapter.WeakViewAdapter;


/**
 * 自定义可动态更新的纵向/横向列表布局
 * Created by bazengliang on 2017/1/16.
 */
public class WeakLinearLayout extends LinearLayout implements WeakViewAdapter.OnNotifyDataLisetener {
    private WeakViewAdapter adapter;
    private OnItemClickListener itemClickListener;

    public WeakLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(WeakViewAdapter adapter) {
        this.adapter = adapter;
        adapter.setViewGroupParent(this);
        adapter.setOnNotifyDataLisetener(this);
    }

    @Override
    public void onInsertNotifyDataLisetener() {
        int insertCount = adapter.getViewHolderCount() - getChildCount();
        for (int i = 0; i < insertCount; i++) {
            final int position = getChildCount();
            final View view = adapter.getHolderView(position);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onWeakItemClickListener(position, view);
                    }
                }
            });
            this.addView(view);
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

    public interface OnItemClickListener {
        void onWeakItemClickListener(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

