package com.dak.weakview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * 具体标签
 * Created by runTop on 2017/11/1.
 */
public class WeakTagsTagView extends View {
    private Paint paint;
    private RectF rectF;
    private String textStr = "default";
    private float textW;
    private float textH;
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

    public WeakTagsTagView(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        textW = 0;
        textH = 0;
        paint.setTextSize(tagTextSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textH = fontMetrics.descent - fontMetrics.ascent + tagPaddingTOB * 2;
        char[] textChar = textStr.toCharArray();
        for (char str : textChar) {
            textW += paint.measureText(String.valueOf(str));
        }
        setMeasuredDimension((int) (textW + tagPaddingLOR * 2), (int) textH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        rectF.set(tagStrokeWidth, tagStrokeWidth, w - tagStrokeWidth, h - tagStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制边框
        paint.setColor(tagStrokeColor);
        paint.setStrokeWidth(tagStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, tagStrokeCorners, tagStrokeCorners, paint);
        //绘制背景
        paint.setColor(tagBackgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, tagStrokeCorners, tagStrokeCorners, paint);
        //绘制文字
        float x = (getWidth() - textW) / 2;
        float y = (getHeight() - textH) / 2 + textH - tagPaddingTOB - 5;
        paint.setTextSize(tagTextSize);
        paint.setColor(tagTextColor);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(textStr, x, y, paint);
    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
        requestLayout();
    }

    public void setTagPaddingLOR(float tagPaddingLOR) {
        this.tagPaddingLOR = tagPaddingLOR;
    }

    public void setTagPaddingTOB(float tagPaddingTOB) {
        this.tagPaddingTOB = tagPaddingTOB;
    }

    public void setTagTextSize(float tagTextSize) {
        this.tagTextSize = tagTextSize;
    }

    public void setTagTextColor(int tagTextColor) {
        this.tagTextColor = tagTextColor;
    }

    public void setTagBackgroundColor(int tagBackgroundColor) {
        this.tagBackgroundColor = tagBackgroundColor;
    }

    public void setTagStrokeColor(int tagStrokeColor) {
        this.tagStrokeColor = tagStrokeColor;
    }

    public void setTagStrokeWidth(float tagStrokeWidth) {
        this.tagStrokeWidth = tagStrokeWidth;
    }

    public void setTagStrokeCorners(float tagStrokeCorners) {
        this.tagStrokeCorners = tagStrokeCorners;
    }
}
