package com.dak.weakview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dak.weakview.adapter.viewholder.WeakCurrencyViewHold;

import java.util.ArrayList;
import java.util.List;

/**
 * weakView 的通用Adapter
 * Created by dak on 2017/9/7.
 */
public abstract class WeakCurrencyAdapter<T> extends WeakViewAdapter<WeakCurrencyViewHold> {
    private List<T> mList = new ArrayList<>();
    private Context context;
    private int layoutId;

    public WeakCurrencyAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public WeakCurrencyViewHold onCreateViewHolder(ViewGroup parent) {
        return new WeakCurrencyViewHold(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public void notifyItemView(WeakCurrencyViewHold holder, int position) {
        notifyItemView(holder, mList.get(position), position);
    }

    public abstract void notifyItemView(WeakCurrencyViewHold holder, T item, int position);

    public void refreshData(List<T> list) {
        if (list == null)
            return;
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
