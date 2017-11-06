package com.dak.weakview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * 具体标签
 * Created by runTop on 2017/11/1.
 */
public class WeakTagsTagView extends View {
    private Paint textPaint;
    private String textStr = "default";

    public WeakTagsTagView(Context context) {
        super(context);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.setBackgroundColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getLeft();
        int right = getRight();
        int top = getTop();
        int bottom = getBottom();
        canvas.drawText(textStr, 0, 0, textPaint);
    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
    }
}
