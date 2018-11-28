package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.customview.R;

/**
 * des:
 * author: Yang Weisi
 * date 2018/7/17 下午6:20
 */
public class HomeWorkProgressBar extends View{

    private int mOuterColor= Color.RED;
    private int mInnerColor= Color.BLUE;
    private int mBorderWidth=10;
    private int mStepTextSize=20;
    private int mStepTextColor=Color.RED;

    private Paint mOutPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    //百分百
    private int mStepMax=100;
    private int mStepCurrent=0;

    public HomeWorkProgressBar(Context context) {
        this(context,null);
    }

    public HomeWorkProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HomeWorkProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initView(context,attrs,defStyleAttr);

        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.HomeWorkProgressBar);
        mOuterColor=array.getColor(R.styleable.HomeWorkProgressBar_outerColorP,mOuterColor);
        mInnerColor=array.getColor(R.styleable.HomeWorkProgressBar_innerColorP,mInnerColor);
        mBorderWidth=(int)array.getDimension(R.styleable.HomeWorkProgressBar_borderWidthP,dip2px(10));
        mStepTextSize=array.getDimensionPixelSize(R.styleable.HomeWorkProgressBar_stepTextSizeP,sp2px(mStepTextSize));
        mStepTextColor=array.getColor(R.styleable.HomeWorkProgressBar_stepTextColorP,mStepTextColor);

        array.recycle();

        mOutPaint=new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setColor(mOuterColor);

        mInnerPaint=new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setColor(mInnerColor);

        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }

    private int sp2px(int sp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    private float dip2px(int dip){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }



    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width>height?height:width,width>height?height:width);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float center=getWidth()/2;
        float radius=getWidth()/2-mBorderWidth/2;

        //画内圆
        canvas.drawCircle(center,center,radius,mOutPaint);

        //画外圆弧,根据百分百显示
        if(0==mStepMax){
            return;
        }
        float sweepAngle=(float)mStepCurrent/mStepMax;
        RectF rectFInner=new RectF(mBorderWidth/2,mBorderWidth/2,getWidth()-mBorderWidth/2,getHeight()-mBorderWidth/2);
        canvas.drawArc(rectFInner,0,sweepAngle*360,false,mInnerPaint);

        //画文字
        String stepText=(int)(sweepAngle*100)+"%";
        Rect textBounds=new Rect();
        mTextPaint.getTextBounds(stepText,0,stepText.length(),textBounds);
        int dx=getWidth()/2-textBounds.width()/2;
        //基线
        Paint.FontMetricsInt fontMetricsInt=mTextPaint.getFontMetricsInt();
        int dy=(fontMetricsInt.bottom-fontMetricsInt.top)/2- fontMetricsInt.bottom;
        int baseLine=getHeight()/2+dy;
        canvas.drawText(stepText,dx,baseLine,mTextPaint);

    }

    public synchronized void setStepMax(int mStepMax){
        if(mStepMax<0){
            //抛异常
        }
        this.mStepMax=mStepMax;
    }

    public synchronized void setStepCurrent(int mStepCurrent){
        if(mStepMax<0){
            //抛异常
        }
        this.mStepCurrent=mStepCurrent;
        invalidate();
    }

}
