package com.dak.weakview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.GridLayout;


/**
 * 自定义可动态更新的网格布局
 *
 * @author bazengliang
 */
public class WeakGridView extends GridLayout implements WeakViewAdapter.OnNotifyDataLisetener {
    private int itemWidth = 0;
    private int rowCount = 0;
    private int oldRowCount = 0;
    private int columnCount = 0;
    private boolean isEquallyWidth = false;
    private WeakViewAdapter adapter;

    public WeakGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setColumnCount(int mColumnCount) {
        super.setColumnCount(mColumnCount);
        this.columnCount = mColumnCount;
        if (columnCount > 0)
            itemWidth = getScreenWidth(this.getContext()) / columnCount;
    }


    /**
     * 确定布局行数
     */
    private void updateRowCount() {
        int viewHolderCount = adapter.getViewHolderCount();
        oldRowCount = rowCount;
        rowCount = viewHolderCount / columnCount;
        if (viewHolderCount % columnCount > 0) {
            rowCount++;
        }
        this.setRowCount(rowCount);
    }

    /**
     * 插入新的视图
     */
    private void insertNotifyChanged() {
        //获取viewHolderList已经添加完新的子View后的大小
        int dataSize = adapter.getViewHolderCount();
        //获取当前容器中子View的大小（即未添加新子View时的大小）
        int childSize = getChildCount();
        //开始插入的行，如果是第一次插入则为0。
        int startRowNum = 0;
        //从哪一列开始插入，如果是第一次插入则为0。
        int startCoumnNum = childSize % columnCount;
        //如果不是第一次插入并且最后一行没有插入满，行数需要减一，因为它是从第0行开始插入的。
        if (oldRowCount != 0 && startCoumnNum != 0) {
            startRowNum = oldRowCount - 1;
        } else {
            /**
             * 1、如果是第一次插入（此时oldRowCount为0）
             * 2、已经插入但是最后一行已经满了，则需要从下一行开始插入，此时oldRowCount的值正好是从下一行开始的值。
             */
            startRowNum = oldRowCount;
        }
        for (int r = startRowNum; r <= rowCount; r++)
            for (int c = startCoumnNum; c < columnCount; c++) {
                //获取将要插入的子View在viewHolderList的索引值
                int location = (r * columnCount) + c;
                //判断location是否还可以获取到数据
                if (location < dataSize) {
                    Spec rowSpec = GridLayout.spec(r); // 设置它的行，从索引为0列开始
                    Spec columnSpec = GridLayout.spec(c);//设置它的列,从索引为0列开始
                    LayoutParams params = new LayoutParams(rowSpec, columnSpec);
                    if (isEquallyWidth) {
                        params.width = itemWidth;
                    }
                    addView(adapter.getHolderView(location), params);
                }
            }
    }

    /**
     * 删除的视图
     */
    private void deleteNotifyChanged() {
        int viewHolderSize = adapter.getViewHolderCount();
        int removeCount = getChildCount() - viewHolderSize;
        while (removeCount > 0) {
            this.removeViewAt(getChildCount() - 1);
            removeCount--;
        }
        //如果全部删除了，则重置容器的值。
        if (viewHolderSize == 0) {
            this.removeAllViews();
            rowCount = 0;
            oldRowCount = 0;
        }
    }

    public void setItemEquallyWidth(boolean mIsEquallyWidth) {
        isEquallyWidth = mIsEquallyWidth;
    }

    public void setAdapter(WeakViewAdapter mAdapter) {
        adapter = mAdapter;
        adapter.setOnNotifyDataLisetener(this);
    }

    @Override
    public void onInsertNotifyDataLisetener() {
        updateRowCount();
        insertNotifyChanged();
    }

    @Override
    public void onDeleteNotifyDataLisetener() {
        deleteNotifyChanged();
    }


    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
