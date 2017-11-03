package com.dak.weakview.adapter.viewholder;

import android.view.View;

/**
 * weakView 的ViewHolder
 * Created by bazengliang on 2016/12/1.
 */
public abstract class WeakViewHolder {


    public abstract <T extends View> T getView(int viewId);

    public abstract View getConvertView();

}
