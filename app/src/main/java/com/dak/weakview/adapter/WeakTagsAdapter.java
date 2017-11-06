package com.dak.weakview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dak.weakview.adapter.viewholder.WeakTagsViewHolder;
import com.dak.weakview.view.WeakTagsTagView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by runTop on 2017/11/1.
 */
public abstract class WeakTagsAdapter<T> extends WeakViewAdapter<WeakTagsViewHolder> {
    private Context context;
    private List<T> mList = new ArrayList<>();
    private int layoutId;

    public WeakTagsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public WeakTagsViewHolder onCreateViewHolder(ViewGroup parent) {
        View tagView = null;
        if (layoutId != 0) {
            tagView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        } else {
            tagView = new WeakTagsTagView(context);
        }
        return new WeakTagsViewHolder(tagView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void notifyItemView(WeakTagsViewHolder holder, int position) {
        notifyItemView(holder, mList.get(position), position);
    }

    public abstract void notifyItemView(WeakTagsViewHolder holder, T item, int position);

    public void refreshData(List<T> list) {
        if (list == null)
            return;
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }


    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }
}
