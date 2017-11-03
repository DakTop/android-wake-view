package com.dak.weakview.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.dak.weakview.view.WeakTagsTagView;

/**
 * Created by bazengliang on 2016/12/1.
 */
public class WeakTagsViewHolder extends WeakViewHolder {
    private WeakTagsTagView weakTagsTagView;

    public WeakTagsViewHolder(Context context) {
        weakTagsTagView = new WeakTagsTagView(context);
    }

    @Override
    public <T extends View> T getView(int viewId) {
        return (T) weakTagsTagView;
    }

    @Override
    public View getConvertView() {
        return weakTagsTagView;
    }

    public WeakTagsTagView getView() {
        return getView(0);
    }


}
