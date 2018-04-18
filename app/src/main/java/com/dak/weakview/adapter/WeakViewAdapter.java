package com.dak.weakview.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.dak.weakview.adapter.viewholder.WeakViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * weakView 的Adapter
 * Created by bazengliang on 2016/12/1.
 */

public abstract class WeakViewAdapter<T extends WeakViewHolder> {
    private OnNotifyDataLisetener onNotifyDataLisetener;
    private List<T> viewHolderList = new ArrayList<>();
    private ViewGroup viewGroupParent;

    /**
     * 更新布局的数目，主要是添加或者删除布局
     */
    public void notifyDataSetChanged() {
        int size = getItemCount();
        int viewHolderSize = viewHolderList.size();
        if (size > viewHolderSize) {//说明需要添加新的数据
            addViewHolder(size - viewHolderSize);
            notityViewHolderChange();
            if (onNotifyDataLisetener != null) {
                onNotifyDataLisetener.onInsertNotifyDataLisetener();
            }
        } else if (size < viewHolderSize) {//说明需要删除数据
            removeViewHolder(viewHolderSize - size);
            notityViewHolderChange();
            if (onNotifyDataLisetener != null) {
                onNotifyDataLisetener.onDeleteNotifyDataLisetener();
            }
        } else {
            notityViewHolderChange();
        }
    }

    /**
     * 更新ViewHolder的数据
     */
    private void notityViewHolderChange() {
        int viewHolderSize = viewHolderList.size();
        for (int i = 0; i < viewHolderSize; i++) {
            notifyItemView(viewHolderList.get(i), i);
        }
    }

    /**
     * 根据索引更新ViewHolder的数据
     */
    public void notifyDataSetChanged(int position) {
        notifyItemView(viewHolderList.get(position), position);
    }

    public abstract T onCreateViewHolder(ViewGroup parent);

    public abstract int getItemCount();

    public abstract void notifyItemView(T holder, int position);

    public void notifyItemViewWithHolder(int holderPosition, int position) {
        notifyItemView(viewHolderList.get(holderPosition), position);
    }

    ;

    /**
     * 添加ViewHolder
     *
     * @param count
     */
    private void addViewHolder(int count) {
        for (int i = 0; i < count; i++) {
            viewHolderList.add(onCreateViewHolder(viewGroupParent));
        }
    }

    /**
     * 删除ViewHolder
     *
     * @param count
     */
    private void removeViewHolder(int count) {
        int size = viewHolderList.size();
        if (size <= 0)
            return;
        if (count > size)
            count = size;
        while (count > 0) {
            viewHolderList.remove(viewHolderList.size() - 1);
            count--;
        }
    }

    public void setOnNotifyDataLisetener(OnNotifyDataLisetener onNotifyDataLisetener) {
        this.onNotifyDataLisetener = onNotifyDataLisetener;
    }

    public void setViewGroupParent(ViewGroup viewGroupParent) {
        this.viewGroupParent = viewGroupParent;
    }

    public List<T> getViewHolderList() {
        return viewHolderList;
    }

    public int getViewHolderCount() {
        return viewHolderList.size();
    }

    public View getHolderView(int position) {
        return viewHolderList.get(position).getConvertView();
    }

    public interface OnNotifyDataLisetener {
        void onInsertNotifyDataLisetener();

        void onDeleteNotifyDataLisetener();
    }

}