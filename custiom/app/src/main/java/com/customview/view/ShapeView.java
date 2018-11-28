package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.customview.R;

/**
 * des:
 * author: Yang Weisi
 * date 2018/7/18 上午11:39
 */
public class ShapeView extends View{

    private int mOneColor= Color.RED;
    private int mTwoColor=Color.BLACK;
    private int mThreeColor=Color.BLUE;
    private Paint mPaint;

    private Path mPath;

    private Shape mCurrentShape= Shape.Circle;

    public Shape getCurrentShape() {
        return mCurrentShape;
    }


    public ShapeView(Context context) {
        this(context,null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.HomeworkLoading);
        mOneColor=array.getColor(R.styleable.HomeworkLoading_oneColor,mOneColor);
        mTwoColor=array.getColor(R.styleable.HomeworkLoading_twoColor,mTwoColor);
        mThreeColor=array.getColor(R.styleable.HomeworkLoading_threeColor,mThreeColor);
        array.recycle();

        mPaint=new Paint();
        mPaint.setAntiAlias(true);

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



//        canvas.drawCircle(centerX,centerY,radius,mPaint);

        switch (mCurrentShape){
            case Circle://画圆形
                mPaint.setColor(getResources().getColor(R.color.caa738ffe));
                int center=getWidth()/2;
                canvas.drawCircle(center,center,center,mPaint);
                break;
            case Square://画正方形
                mPaint.setColor(getResources().getColor(R.color.caae84e40));
                canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
                break;
            case Triangle://画三角形 用Path
                mPaint.setColor(getResources().getColor(R.color.caa72d572));
                if(null==mPath){
                    //要画等边三角形
                    mPath=new Path();
                    mPath.moveTo(getWidth()/2,0);
                    mPath.lineTo(0,(float)((getWidth()/2)*Math.sqrt(2)));
                    mPath.lineTo(getWidth(),(float)((getWidth()/2)*Math.sqrt(2)));
                    mPath.close();

                }
                canvas.drawPath(mPath,mPaint);
                break;
        }
    }

    /**
     * 改变形状
     */
    public void exchange(){
        switch (mCurrentShape){
            case Circle:
                mCurrentShape= Shape.Square;
                break;
            case Square:
                mCurrentShape= Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape= Shape.Circle;
                break;
        }
        //刷新显示，不断绘制形状
        invalidate();
    }

    public enum Shape{
        Circle,Square,Triangle
    }

}
