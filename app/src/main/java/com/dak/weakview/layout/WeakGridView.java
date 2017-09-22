package com.dak.weakview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
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
        int startRowNum = childSize / columnCount;
        //从哪一列开始插入，如果是第一次插入则为0。
        int startCoumnNum = childSize % columnCount;
        for (int r = startRowNum; r <= rowCount; r++) {
            for (int c = startCoumnNum; c < columnCount; c++) {
                //获取将要插入的子View在viewHolderList的索引值
                int location = (r * columnCount) + c;
                //判断location是否还可以获取到数据
                if (location < dataSize) {
                    View itemView = adapter.getHolderView(location);
                    LayoutParams params = (LayoutParams) itemView.getLayoutParams();
                    if (isEquallyWidth) {
                        params.width = itemWidth;
                    }
                    params.rowSpec = GridLayout.spec(r); // 设置它的行，从索引为0列开始
                    params.columnSpec = GridLayout.spec(c);//设置它的列,从索引为0列开始
                    addView(itemView);
                }
            }
            startCoumnNum = 0;
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
        }
    }

    public void setItemEquallyWidth(boolean mIsEquallyWidth) {
        isEquallyWidth = mIsEquallyWidth;
    }

    public void setAdapter(WeakViewAdapter mAdapter) {
        adapter = mAdapter;
        adapter.setViewGroupParent(this);
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
