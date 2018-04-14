package com.dak.weakview.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dak.weakview.R;
import com.dak.weakview.adapter.WeakTagsAdapter;
import com.dak.weakview.adapter.WeakViewAdapter;
import com.dak.weakview.view.WeakTagsTagView;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag标签布局
 * Created by runTop on 2017/10/31.
 */
public class WeakTagsLayout extends ViewGroup implements WeakViewAdapter.OnNotifyDataLisetener {
    private WeakTagsAdapter adapter;
    /**
     * 行高度
     */
    private int lineHeight = 0;

    /**
     * 元素之间左右的间隔
     */
    private float horizontalColumnSpace = 0;
    /**
     * 行与行之间的间隔
     */
    private float verticalLineSpace = 0;
    /**
     * 父布局边框圆角角度，默认90度。
     */
    private float layoutCorners = 0;
    /**
     * 父布局边框颜色
     */
    private int layoutStrokeColor = Color.TRANSPARENT;
    /**
     * 父布局背景颜色
     */
    private int layoutBackgroundColor = Color.TRANSPARENT;
    /**
     * 父布局边框宽度
     */
    private float layoutStrokeWidth = 0;
    /**
     * tag左右的padding的值
     */
    private float tagPaddingLOR;
    /**
     * tag上下的padding的值
     */
    private float tagPaddingTOB;
    /**
     * tag文字大小
     */
    private float tagTextSize;
    /**
     * tag文字颜色
     */
    private int tagTextColor;
    /**
     * tag背景颜色
     */
    private int tagBackgroundColor;
    /**
     * tag边框颜色
     */
    private int tagStrokeColor;
    /**
     * tag边框大小
     */
    private float tagStrokeWidth;
    /**
     * tag边框角度
     */
    private float tagStrokeCorners;
    /**
     * 当选择模式为限制个数时，selectLimitCount代表最多能选几个
     */
    private int selectLimitCount = 0;
    /**
     * 选中tag背景颜色
     */
    private int selectTagBgColor;
    /**
     * 选中tag文字颜色
     */
    private int selectTagTextColor;
    /**
     * 选中tag边框颜色
     */
    private int selectTagStrokeColor;
    /**
     * 父布局绘制区域
     */
    private RectF backgroundRect;
    private Paint paint;
    private OnTagItemClickListener onItemClickListener;
    //被点击的子View的位置
    private int clickPosition = -1;
    //单击选择模式
    private SelectMode clickSelectMode;
    //子View列表
    private List<OnTagSelectListener> listTagsSelectListener = new ArrayList<>();
    //  多选模式下选中的Position列表
    private List<Integer> selectPositionList = new ArrayList<>();
    //  单选模式下选中的Position
    private int selectSinglePosition = -1;

    public WeakTagsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeakTagsLayout);
        horizontalColumnSpace = typedArray.getDimension(R.styleable.WeakTagsLayout_layout_horizontalColumnSpace, 20);
        verticalLineSpace = typedArray.getDimension(R.styleable.WeakTagsLayout_layout_verticalLinesSpace, 20);
        layoutCorners = typedArray.getDimension(R.styleable.WeakTagsLayout_layout_corners, 0);
        layoutStrokeColor = typedArray.getColor(R.styleable.WeakTagsLayout_layout_strokeColor, Color.TRANSPARENT);
        layoutStrokeWidth = typedArray.getDimension(R.styleable.WeakTagsLayout_layout_strokeWidth, 0);
        layoutBackgroundColor = typedArray.getColor(R.styleable.WeakTagsLayout_layout_backgroundColor, Color.TRANSPARENT);
        tagPaddingLOR = typedArray.getDimension(R.styleable.WeakTagsLayout_tag_paddingLOR, 20);
        tagPaddingTOB = typedArray.getDimension(R.styleable.WeakTagsLayout_tag_paddingTOB, 10);
        tagTextSize = typedArray.getDimension(R.styleable.WeakTagsLayout_tag_textSize, 25);
        tagTextColor = typedArray.getColor(R.styleable.WeakTagsLayout_tag_textColor, Color.BLACK);
        tagBackgroundColor = typedArray.getColor(R.styleable.WeakTagsLayout_tag_backgroundColor, Color.TRANSPARENT);
        tagStrokeColor = typedArray.getColor(R.styleable.WeakTagsLayout_tag_strokeColor, Color.GRAY);
        tagStrokeWidth = typedArray.getDimension(R.styleable.WeakTagsLayout_tag_strokeWidth, 2);
        tagStrokeCorners = typedArray.getDimension(R.styleable.WeakTagsLayout_tag_strokeCorners, 0);
        selectLimitCount = typedArray.getInteger(R.styleable.WeakTagsLayout_select_limit_count, -1);
        selectTagBgColor = typedArray.getInteger(R.styleable.WeakTagsLayout_select_tagBgColor, Color.GRAY);
        selectTagTextColor = typedArray.getInteger(R.styleable.WeakTagsLayout_select_tagTextColor, Color.GREEN);
        selectTagStrokeColor = typedArray.getInteger(R.styleable.WeakTagsLayout_select_tagStrokeColor, Color.GREEN);
        //
        int selectMode = typedArray.getColor(R.styleable.WeakTagsLayout_select_click, 0);
        switch (selectMode) {
            case 1://单选模式
                clickSelectMode = SelectMode.single;
                break;
            case 2://多选模式
                clickSelectMode = SelectMode.more;
                break;
            default://默认模式
                clickSelectMode = SelectMode.normal;
                break;
        }
        typedArray.recycle();
        //默认自定义ViewGroup是不会调用onDraw方法，需要手动调用setWillNotDraw方法或者设置ViewGroup的背景才会执行onDraw方法
        setWillNotDraw(false);
        backgroundRect = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = this.getChildCount();
        int lines = 1;
        float lineFixedWidth = MeasureSpec.getSize(widthMeasureSpec);//固定行宽
        float currLineWidth = 0;
        float paddingLeft = getPaddingLeft();
        float paddingRight = getPaddingRight();
        if ((lineFixedWidth -= paddingLeft + paddingRight) <= 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View viewChild = getChildAt(i);
            lineHeight = Math.max(lineHeight, viewChild.getMeasuredHeight());
            measureChild(viewChild, widthMeasureSpec, heightMeasureSpec);
            //当子View的宽度和horizontalLinesSpace的累加大于lineFixedWidth则另起一行
            if ((currLineWidth += viewChild.getMeasuredWidth()) > lineFixedWidth) {
                //另起一行事需要添加horizontalLinesSpace的值
                currLineWidth = viewChild.getMeasuredWidth();
                lines++;
            }
            currLineWidth += horizontalColumnSpace;
        }
        //如果只有一行而且layout_width设置的是wrap_content则设置当前控件宽度为行宽
        if (lines == 1 && MeasureSpec.getSize(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            lineFixedWidth = currLineWidth - horizontalColumnSpace + paddingLeft + paddingRight;
        } else {
            lineFixedWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            setMeasuredDimension((int) lineFixedWidth, MeasureSpec.getSize(heightMeasureSpec));
        } else {
            setMeasuredDimension((int) lineFixedWidth, (int) (lineHeight * lines + verticalLineSpace * (lines - 1) + getPaddingTop() + getPaddingBottom()));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }
        float paddingLeft = getPaddingLeft();
        float layoutWidth = getWidth() - getPaddingRight();
        float currentLeft = paddingLeft;
        float currentTop = getPaddingTop();
        int currentBottom = (int) currentTop + lineHeight;
        for (int i = 0; i < childCount; i++) {
            View chidView = getChildAt(i);
            if ((currentLeft + chidView.getMeasuredWidth()) > layoutWidth) {
                currentLeft = paddingLeft;
                currentTop += (int) (lineHeight + verticalLineSpace);
                currentBottom = (int) currentTop + lineHeight;
            }
            chidView.layout((int) currentLeft, (int) currentTop, (int) (currentLeft += chidView.getMeasuredWidth()), currentBottom);
            //
            currentLeft += horizontalColumnSpace;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        backgroundRect.set(layoutStrokeWidth, layoutStrokeWidth, w - layoutStrokeWidth, h - layoutStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(layoutBackgroundColor);
        canvas.drawRoundRect(backgroundRect, layoutCorners, layoutCorners, paint);
        //绘制边框
        paint.setStrokeWidth(layoutStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(layoutStrokeColor);
        canvas.drawRoundRect(backgroundRect, layoutCorners, layoutCorners, paint);
    }

    @Override
    public void onInsertNotifyDataLisetener() {
        int insertCount = adapter.getViewHolderCount() - getChildCount();
        for (int i = 0; i < insertCount; i++) {
            View view = adapter.getHolderView(getChildCount());
            WeakTagsTagView weakTagsTagView = (WeakTagsTagView) view;
            weakTagsTagView.setTagBackgroundColor(tagBackgroundColor);
            weakTagsTagView.setTagPaddingLOR(tagPaddingLOR);
            weakTagsTagView.setTagPaddingTOB(tagPaddingTOB);
            weakTagsTagView.setTagStrokeColor(tagStrokeColor);
            weakTagsTagView.setTagStrokeCorners(tagStrokeCorners);
            weakTagsTagView.setTagStrokeWidth(tagStrokeWidth);
            weakTagsTagView.setTagTextColor(tagTextColor);
            weakTagsTagView.setTagTextSize(tagTextSize);
            weakTagsTagView.setSelectTagBgColor(selectTagBgColor);
            weakTagsTagView.setSelectTagTextColor(selectTagTextColor);
            weakTagsTagView.setSelectTagStrokeColor(selectTagStrokeColor);
            listTagsSelectListener.add(weakTagsTagView);
            this.addView(view);
        }
    }


    @Override
    public void onDeleteNotifyDataLisetener() {
        int removeCount = getChildCount() - adapter.getViewHolderCount();
        while (removeCount > 0) {
            int position = getChildCount() - 1;
            this.removeViewAt(position);
            listTagsSelectListener.remove(position);
            removeCount--;
        }
    }

    public void setAdapter(WeakTagsAdapter adapter) {
        this.removeAllViews();
        listTagsSelectListener.clear();
        this.adapter = adapter;
        adapter.setViewGroupParent(this);
        adapter.setOnNotifyDataLisetener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (childContainsEventXY(getChildAt(i), event)) {
                        clickPosition = i;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (clickPosition >= 0 && onItemClickListener != null && childContainsEventXY(getChildAt(clickPosition), event)) {
                    //判断选择模式
                    switch (clickSelectMode) {
                        case normal:
                            break;
                        case single:
                            selectSinglePosition = clickPosition;
                            clickSelectSingle(clickPosition);
                            break;
                        case more:
                            clickSelectMore(clickPosition);
                            break;
                    }
                    onItemClickListener.onTagItemClickListener(clickPosition, getChildAt(clickPosition));
                    clickPosition = -1;
                }
                break;
        }
        return true;
    }

    /**
     * 单选
     *
     * @param position
     */
    private void clickSelectSingle(int position) {
        int count = listTagsSelectListener.size();
        for (int i = 0; i < count; i++) {
            if (position == i) {
                listTagsSelectListener.get(i).itemSelect(true);
            } else {
                listTagsSelectListener.get(i).itemSelect(false);
            }
        }
    }

    /**
     * 多选
     *
     * @param position
     */
    public void clickSelectMore(int position) {
        if (selectPositionList.contains(position)) {
            listTagsSelectListener.get(position).itemSelect(false);
            selectPositionList.remove(Integer.valueOf(position));
        } else if (selectLimitCount <= 0 || selectPositionList.size() < selectLimitCount) {
            listTagsSelectListener.get(position).itemSelect(true);
            selectPositionList.add(position);
        }
    }

    /**
     * 判断屏幕接受的事件是否落在传递进来的View中
     *
     * @param view
     * @param event
     * @return
     */
    private boolean childContainsEventXY(View view, MotionEvent event) {
        Rect rect = new Rect();
        view.getHitRect(rect);
        return rect.contains((int) event.getX(), (int) event.getY());
    }

    public interface OnTagItemClickListener {
        void onTagItemClickListener(int position, View view);
    }

    public void setOnItemClickListener(OnTagItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public enum SelectMode {
        normal, single, more;
    }

    public void setSelectLimitCount(int selectLimitCount) {
        this.selectLimitCount = selectLimitCount;
    }

    public void setClickSelectMode(SelectMode clickSelectMode) {
        selectPositionList.clear();
        selectSinglePosition = -1;
        this.clickSelectMode = clickSelectMode;
    }

    public SelectMode getClickSelectMode() {
        return clickSelectMode;
    }

    public interface OnTagSelectListener {
        void itemSelect(boolean select);
    }

    public int getSelectSinglePosition() {
        return selectSinglePosition;
    }

    public List<Integer> getSelectSelectPosition() {
        return selectPositionList;
    }

}