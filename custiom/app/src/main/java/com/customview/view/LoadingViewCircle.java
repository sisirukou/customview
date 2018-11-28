package com.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/27 20:57
 */
public class LoadingViewCircle extends View{

    private Paint mPaint;
    private int mColor;

    public LoadingViewCircle(Context context) {
        this(context,null);
    }

    public LoadingViewCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingViewCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆形
        int cx=getWidth()/2;
        int cy=getHeight()/2;
        canvas.drawCircle(cx,cy,cx,mPaint);
    }

    public void exchangeColor(int color){
        mColor=color;
        mPaint.setColor(color);
        invalidate();
    }

    public int getColor() {
        return mColor;
    }
}
