package com.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * des:
 * author: Yang Weisi
 * date 2018/12/2 10:30
 */
public class YahooLoadingView extends View{
    public YahooLoadingView(Context context) {
        this(context,null);
    }

    public YahooLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YahooLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //怎么让圆旋转起来
        
    }
}
