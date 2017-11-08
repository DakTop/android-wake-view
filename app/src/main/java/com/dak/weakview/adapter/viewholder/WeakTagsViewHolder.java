package com.dak.weakview.adapter.viewholder;

import android.view.View;

import com.dak.weakview.view.WeakTagsTagView;

/**
 * Created by bazengliang on 2016/12/1.
 */
public class WeakTagsViewHolder extends WeakCurrencyViewHold {

    public WeakTagsViewHolder(View itemView) {
        super(itemView);
    }

    public void setTagVal(String str) {
        View view = getConvertView();
        if (view instanceof WeakTagsTagView) {
            ((WeakTagsTagView) view).setTextStr(str);
        }
    }
}