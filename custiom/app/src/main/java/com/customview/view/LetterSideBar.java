package com.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * des:
 * author: Yang Weisi
 * date 2018/8/27 16:24
 */
public class LetterSideBar extends View{

    private static String[] mLetters={"A","B","C","D","E","F","G","H","I","J"
            ,"K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};

    private int itemHeight;

    private String mCurrentToucherLetter;//当前触摸字母位置

    private Paint mPaintFoucus;
    private Paint mPaintNormal;

    private String mCurrentLetter="";



    public LetterSideBar(Context context) {
        this(context,null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaintNormal=new Paint();
        mPaintNormal.setAntiAlias(true);
        mPaintNormal.setTextSize(sp2px(12));//设置的是像素，所以需要转化为dip
        mPaintNormal.setColor(Color.BLUE);

        mPaintFoucus=new Paint();
        mPaintFoucus.setAntiAlias(true);
        mPaintFoucus.setTextSize(sp2px(12));//设置的是像素，所以需要转化为dip
        mPaintFoucus.setColor(Color.RED);
    }

    private float sp2px(int sp) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度可以直接获取
        int height=MeasureSpec.getSize(heightMeasureSpec);
        //宽度 paddingLeft+字母宽度(取决于画笔)+paddingRight
        int textWidth=(int)mPaintNormal.measureText("A");
        int width=getPaddingLeft()+getPaddingRight()+textWidth;

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //字母高度
        itemHeight=(getHeight()-getPaddingTop()-getPaddingBottom())/mLetters.length;

        for(int i=0;i<mLetters.length;i++){
            //绘制在最中间=宽度/2-文字/2
            int textWidth=(int)mPaintNormal.measureText(mLetters[i]);
            int x=getWidth()/2-textWidth/2;

            //知道每个字母的中心位置，第一个字母高度一半，第二个字母高度一半+前面字符的高度
            int letterCenterY=i*itemHeight+itemHeight/2+getPaddingTop();
            //基线，基于中心位置
           Paint.FontMetrics fontMetrics= mPaintNormal.getFontMetrics();
            int dy=(int)((fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
            int baseLine=letterCenterY+dy;
            //当前字母高亮
            if(mLetters[i].equals(mCurrentToucherLetter)){
                canvas.drawText(mLetters[i],x,baseLine,mPaintFoucus);
            }else {
                canvas.drawText(mLetters[i],x,baseLine,mPaintNormal);
            }

        }
    }

    /**
     * 处理交互手指在上面的效果
     * 效果是：手指触摸高亮当前位置，中间显示当前选择的字母
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算出当前触摸字母，获取当前位置
                float currentMoveY=event.getY();

                int currentPosition=(int)(currentMoveY/itemHeight);
                if(currentPosition<0){
                    currentPosition=0;
                }
                if(currentPosition>mLetters.length-1){
                    currentPosition=mLetters.length-1;
                }
                mCurrentToucherLetter=mLetters[currentPosition];

                if(null!=mListener){

                    mListener.touch(mCurrentToucherLetter,true);
                }

                if(mCurrentLetter.equals(mCurrentToucherLetter)){
                    return true;
                }

                mCurrentLetter=mCurrentToucherLetter;

                invalidate();

                break;

            case MotionEvent.ACTION_UP:
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(null!=mListener){
                            mListener.touch(mCurrentToucherLetter,false);
                        }
                    }
                },1000);

                break;
        }

        return true;
    }

    private LetterTouchListener mListener;

    public void setOnLetterTouchListener(LetterTouchListener listener){
        this.mListener=listener;
    }

    public interface LetterTouchListener{
        void touch(CharSequence letter,boolean isTouch);
    }


}
