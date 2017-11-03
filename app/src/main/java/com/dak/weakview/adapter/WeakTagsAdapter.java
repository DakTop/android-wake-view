package com.dak.weakview.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.dak.weakview.adapter.viewholder.WeakTagsViewHolder;
import com.dak.weakview.view.WeakTagsTagView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by runTop on 2017/11/1.
 */
public class WeakTagsAdapter<T> extends WeakViewAdapter<WeakTagsViewHolder> {
    private Context context;
    private List<T> mList = new ArrayList<>();

    public WeakTagsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public WeakTagsViewHolder onCreateViewHolder(ViewGroup parent) {
        return new WeakTagsViewHolder(context);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void notifyItemView(WeakTagsViewHolder holder, int position) {
        WeakTagsTagView weakTagsTagView = holder.getView();
        weakTagsTagView.setTextStr("default2");
    }

    public void refreshData(List<T> list) {
        if (list == null)
            return;
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }
}
