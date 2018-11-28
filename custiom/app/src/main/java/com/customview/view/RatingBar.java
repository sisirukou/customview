package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.customview.R;

/**
 * des:
 * author: Yang Weisi
 * date 2018/8/13 上午11:15
 */
public class RatingBar extends View{

    private Bitmap mStarNormalBitmap,mStarFocusBitmap;
    private int mGradeNumber=5;
    private int mCurrentGrade=0;

    public RatingBar(Context context) {
        this(context,null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.RatingBar);

        int starNormalId=array.getResourceId(R.styleable.RatingBar_starNormal,0);
        if(0==starNormalId){
            throw new RuntimeException("请设置未选中图片");
        }
        mStarNormalBitmap= BitmapFactory.decodeResource(getResources(),starNormalId);

        int starFocusId=array.getResourceId(R.styleable.RatingBar_starFocus,0);
        if(0==starFocusId){
            throw new RuntimeException("请设置选中图片");
        }
        mStarFocusBitmap=BitmapFactory.decodeResource(getResources(),starFocusId);

        mGradeNumber=array.getInt(R.styleable.RatingBar_gradeNumber,mGradeNumber);

        array.recycle();

    }

    /**
     * 指定控件宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度  一张图片的高度,自己实现 padding
        int height=mStarFocusBitmap.getHeight();
        //宽度  星星数量*星星的宽度+间隔
        int width=mStarFocusBitmap.getWidth()*mGradeNumber;

        setMeasuredDimension(width,height);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        for(int i=0;i<mGradeNumber;i++){
            int x=i*mStarFocusBitmap.getWidth();
            //结合触摸反馈，触摸的时候mCurrentGrade值是不断变化，判断当前分数之前的都要设置为高亮
            if(mCurrentGrade>i){//当前分数之前
                canvas.drawBitmap(mStarFocusBitmap,x,0,null);
            }else {
                canvas.drawBitmap(mStarNormalBitmap,x,0,null);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //移动 按下 抬起 处理逻辑都是一样，判断手指位置，根据当前位置计算出分数，再去刷新显示
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN://按下
            case MotionEvent.ACTION_MOVE://移动
//            case MotionEvent.ACTION_UP://抬起
                //event.getX()相对于当前控件的位置    event.getRawX()获取屏幕上x的位置
                float moveX=event.getX();
                int currentGrade=(int)moveX/mStarFocusBitmap.getWidth()+1;
                if(currentGrade<0){
                    currentGrade=0;
                }
                if(currentGrade>mGradeNumber){
                    currentGrade=mGradeNumber;
                }

                //当前分数相同，不刷新界面
                if(mCurrentGrade==currentGrade){
                    return true;
                }
                //刷新显示
                mCurrentGrade=currentGrade;
                invalidate();

                break;

        }


        return true;//需要返回true,第一次(Down)如果返回false，以后就不会进来

    }
}
