package com.dak.weakview.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

/**
 * 具体标签
 * Created by runTop on 2017/11/1.
 */
public class WeakTagsTagView extends TextView {

    private Paint textPaint;
    private String textStr = "default";

    public WeakTagsTagView(Context context) {
        super(context);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.setBackgroundColor(Color.RED);
    }

//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        canvas.drawText(textStr, 0, 0, textPaint);
//    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
        setText(textStr);
    }
}
