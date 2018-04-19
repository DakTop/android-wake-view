package com.dak.weakview.adapter.viewholder;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dak on 2017/11/1.
 */
public class WeakCurrencyViewHold extends WeakViewHolder {

    private final SparseArray<View> mViews;
    private View convertView;

    public WeakCurrencyViewHold(View itemView) {
        convertView = itemView;
        this.mViews = new SparseArray<View>();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @Override
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    @Override
    public View getConvertView() {
        return convertView;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public void setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
    }

    /**
     * 为TextView设置文字
     *
     * @param viewId
     * @return
     */
    public void setText(int viewId, String str) {
        TextView text = getView(viewId);
        text.setText(str);
    }

}
