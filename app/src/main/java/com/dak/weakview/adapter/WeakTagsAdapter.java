package com.dak.weakview.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.dak.weakview.adapter.viewholder.WeakTagsViewHolder;
import com.dak.weakview.layout.WeakTagsLayout;
import com.dak.weakview.view.WeakTagsTagView;

import java.util.ArrayList;
import java.util.List;

/**
 * tag布局的adapter
 * Created by dak on 2017/11/1.
 */
public abstract class WeakTagsAdapter<T> extends WeakViewAdapter<WeakTagsViewHolder> {
    private Context context;
    private List<T> mList = new ArrayList<>();
    private WeakTagsLayout weakTagsLayout;

    public WeakTagsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public WeakTagsViewHolder onCreateViewHolder(ViewGroup parent) {
        weakTagsLayout = (WeakTagsLayout) parent;
        return new WeakTagsViewHolder(new WeakTagsTagView(context));
    }

    public T getItem(int position) {
        return position >= 0 && position < mList.size() ? mList.get(position) : null;
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

    /**
     * 获取单选选中的数据
     *
     * @return
     */
    public T getSelectSingleData() {
        if (weakTagsLayout != null && weakTagsLayout.getSelectSinglePosition() >= 0) {
            return mList.get(weakTagsLayout.getSelectSinglePosition());
        }
        return null;
    }

    /**
     * 获取多选选中的数据
     *
     * @return
     */
    public List<T> getSelectMoreData() {
        List<T> list = null;
        if (weakTagsLayout != null && weakTagsLayout.getSelectSelectPosition().size() > 0) {
            list = new ArrayList<>();
            List<Integer> selectList = weakTagsLayout.getSelectSelectPosition();
            int count = selectList.size();
            for (int i = 0; i < count; i++) {
                list.add(mList.get(selectList.get(i)));
            }
        }
        return list;
    }
}
