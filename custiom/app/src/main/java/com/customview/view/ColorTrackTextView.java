package com.customview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.customview.R;

/**
 * des:
 * author: Yang Weisi
 * date 2018/7/12 下午5:15
 * 分析：extends ViewViewGroup :需要些onMeasure() onDraw()
 *      extends TextView:onMeasure()不需要实现，textColor、textSize不用写
 *      1.一个文字两种颜色，用两个画笔去画
 *      2.能够从左到右，从右到左
 *      3.能够整合ViewPage
 *
 *      实现不通朝向：左边是不变色右边是变色
 */
@SuppressLint("AppCompatCustomView")
public class ColorTrackTextView extends TextView {

    private Paint mOriginPaint;//实现一个文字两种颜色，绘制不变色字体的画笔
    private Paint mChangePaint;//实现一个文字两种颜色，绘制变色字体的画笔

    private float mCurrentProgress=0.0f;//实现一个文字两种颜色，当前进度

    //实现不同朝向
    private Direction mDirection=Direction.LEFT_TO_RIGHT;
    public enum Direction{
        LEFT_TO_RIGHT,//从左到右
        RIGHT_TO_LEFT//从右到左
    }

    public ColorTrackTextView(Context context) {
        this(context,null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        initPaint(context,attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int originColor=array.getColor(R.styleable.ColorTrackTextView_originColor,getTextColors().getDefaultColor());
        int changeColor=array.getColor(R.styleable.ColorTrackTextView_changeColor,getTextColors().getDefaultColor());
        mOriginPaint=getPaintByColor(originColor);
        mChangePaint=getPaintByColor(changeColor);
        array.recycle();
    }

    private Paint getPaintByColor(int color) {
        Paint paint =new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        return paint;
    }

    /**
     * 一个文字两种颜色
     * 利用clipRect的api可以裁剪，左边用一个画笔去画，右边用另一个画笔去画，不断改变中间值
     */

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        canvas.save();

        //根据进度把中间值算出来
        int middle=(int)(mCurrentProgress*getWidth());

        if(mDirection==Direction.LEFT_TO_RIGHT){//从左到右(左边是红色，右边是黑色)，整体先是黑色，然后从左向右移动变为红色
            //绘制不变色的
            drawText(canvas,mChangePaint,0,middle);
            //绘制变色
            drawText(canvas,mOriginPaint,middle,getWidth());

        }else if(mDirection==Direction.RIGHT_TO_LEFT){//从右到左(右边是红色，左边的黑色)，整体先是红色，然后从右移动变为黑色
            //绘制不变色的
            drawText(canvas,mChangePaint,getWidth()-middle,getWidth());
            //绘制变色
            drawText(canvas,mOriginPaint,0,getWidth()-middle);
        }




    }

    /**
     * 绘制text
     * @param canvas
     * @param paint
     * @param start
     * @param end
     */
    private void drawText(Canvas canvas,Paint paint,int start,int end){
        canvas.save();
        Rect rect=new Rect(start,0,end,getHeight());
        canvas.clipRect(rect);

        String text=getText().toString();
        Rect bounds=new Rect();
        paint.getTextBounds(text,0,text.length(),bounds);
        int x=getWidth()/2-bounds.width()/2;//获取字体宽度

        //基线baseLine
        Paint.FontMetricsInt fontMetricsInt=paint.getFontMetricsInt();
        int dy=(fontMetricsInt.bottom-fontMetricsInt.top)/2-fontMetricsInt.bottom;
        int baseLine=getHeight()/2+dy;

        canvas.drawText(text,x,baseLine,paint);

        canvas.restore();
    }

    public void setDirection(Direction direction){
        this.mDirection=direction;
    }

    public void setCurrentProgress(float currentProgress){
        this.mCurrentProgress=currentProgress;
        invalidate();
    }

    public void setChangeColor(int changeColor){
        this.mChangePaint.setColor(changeColor);
    }

    public void setOriginColor(int originColor){
        this.mOriginPaint.setColor(originColor);
    }

}
