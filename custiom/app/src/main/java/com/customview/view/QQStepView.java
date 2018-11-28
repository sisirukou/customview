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
import android.view.View;

import com.customview.R;

/**
 * des:
 * author: Yang Weisi
 * date 2018/6/19 下午3:16
 */
public class QQStepView extends View {

    private int mOuterColor= Color.RED;
    private int mInnerColor= Color.BLUE;
    private int mBorderWidth;
    private int mStepTextSize;
    private int mStepTextColor;

    private Paint mOutPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    //步数应该是百分百，不能写死
    private int mStepMax=0;
    private int mStepCurrent=0;

    public QQStepView(Context context) {
        this(context,null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);

    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //1.分析效果
        //2.确定自定义属性，编写attr.xml
        //3.在布局中使用
        //4.在自定义view中获取自定义属性

        //5.onMessure()
        //6.画外圆弧，内圆弧，文字
        //7.其他

        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOuterColor=array.getColor(R.styleable.QQStepView_outerColor,mOuterColor);
        mInnerColor=array.getColor(R.styleable.QQStepView_innerColor,mInnerColor);
        mBorderWidth=(int)array.getDimension(R.styleable.QQStepView_borderWidth,mBorderWidth);
        mStepTextSize=array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize,mStepTextSize);
        mStepTextColor=array.getColor(R.styleable.QQStepView_stepTextColor,mStepTextColor);

        array.recycle();
        mOutPaint=new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);

        mInnerPaint=new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //调用者在布局文件中可能，wrap_content
        //获取模式 AT_MOST 40dp
        //宽度高度不一致，去最小值，确保是个正方形
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width>height?height:width,width>height?height:width);

    }

    /**
     * 画外圆弧，内圆弧，文字
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1.画外圆弧 分析：圆弧闭合了  思考：边缘没显示完整，描边有宽度 mBorderWidth  圆弧

        int center=getWidth()/2;//中心点
        int radius=getWidth()/2-mBorderWidth/2;//半径

        RectF rectF=new RectF(mBorderWidth/2,mBorderWidth/2,getWidth()-mBorderWidth/2,getHeight()-mBorderWidth/2);
        canvas.drawArc(rectF,135,270,false,mOutPaint);

        //画内圆弧,根据百分百显示
        if(0==mStepMax){
            return;
        }
        float sweepAngle=(float)mStepCurrent/mStepMax;
        RectF rectFInner=new RectF(mBorderWidth/2,mBorderWidth/2,getWidth()-mBorderWidth/2,getHeight()-mBorderWidth/2);
        canvas.drawArc(rectFInner,135,sweepAngle*270,false,mInnerPaint);


        //画文字
        String stepText=mStepCurrent+"";
        Rect textBounds=new Rect();
        mTextPaint.getTextBounds(stepText,0,stepText.length(),textBounds);
        int dx=getWidth()/2-textBounds.width()/2;
        //基线
        Paint.FontMetricsInt fontMetricsInt=mTextPaint.getFontMetricsInt();
        int dy=(fontMetricsInt.bottom-fontMetricsInt.top)/2- fontMetricsInt.bottom;
        int baseLine=getHeight()/2+dy;
        canvas.drawText(stepText,dx,baseLine,mTextPaint);


    }

    //7.其他  写几个方法动起来


    public synchronized void setStepMax(int mStepMax) {
        this.mStepMax = mStepMax;
    }

    public synchronized void setStepCurrent(int mStepCurrent) {
        this.mStepCurrent = mStepCurrent;
        //不断绘制 invalidate()会调用onDraw()方法
        /**
         * 源码分析：
         * invlidate()流程：一路往上跑，跑到最外层，调用draw()->dispatchDraw() 一路往上画，最终画到当前调用invaldate的view的onDraw()方法
         * invlidate()牵连着整个layout布局中的view
         */
        //为什么不能在子线程更新UI
        //开了线程，更新UI，一般会调用setText(),setImageView()  ，都会调到 ViewRootImp里的checkThred()
        //heckThred()用来检测线程，报错来自这里面
        invalidate();
    }
}
