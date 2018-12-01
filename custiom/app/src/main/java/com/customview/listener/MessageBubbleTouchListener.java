package com.customview.listener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.customview.R;
import com.customview.utils.ScreenUtils;
import com.customview.view.MessageBubbleView;

/**
 * des:监听当前View的触摸事件
 * author: Yang Weisi
 * date 2018/11/29 22:18
 */
public class MessageBubbleTouchListener implements View.OnTouchListener, MessageBubbleView.MessageBubbleListener {

    private View mStaticView;//原来需要拖动爆炸的View
    private WindowManager mWindowManager;
    private MessageBubbleView mMessageBubbleView;
    private WindowManager.LayoutParams mParams;
    private FrameLayout mBombFrame;//爆炸容器
    private ImageView mBombImage;//爆炸的ImageView
    private Context mContext;
    private BubbleDisapperListener mDisapperListener;

    public MessageBubbleTouchListener(View mStaticView,Context context,BubbleDisapperListener disapperListener) {
        this.mStaticView = mStaticView;
        mWindowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mMessageBubbleView=new MessageBubbleView(context);
        mMessageBubbleView.setMessageBubbleListener(this);
        mParams=new WindowManager.LayoutParams();
        mParams.format= PixelFormat.TRANSLUCENT;
        this.mContext=context;
        mBombFrame=new FrameLayout(mContext);
        mBombImage=new ImageView(mContext);
        mBombImage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBombFrame.addView(mBombImage);
        this.mDisapperListener=disapperListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //首先把自己隐藏
                mStaticView.setVisibility(View.GONE);
                //在WindowManager上面搞一个View,是写好的这个贝塞尔View
                mWindowManager.addView(mMessageBubbleView,mParams);
                //初始化贝塞尔View的点
                //保证固定圆的中心在View的中心
                int[] location=new int[2];
                mStaticView.getLocationOnScreen(location);
                Bitmap bitmap=getBitmapByView(mStaticView);
                mMessageBubbleView.initPoint(location[0]+mStaticView.getWidth()/2,location[1]+mStaticView.getHeight()/2- ScreenUtils.getStatusBarHeight(mContext));
                //给消息拖拽设置一个Bipmap
                mMessageBubbleView.setDragBitmap(bitmap);

                break;
            case MotionEvent.ACTION_MOVE:
                mMessageBubbleView.updateDragPoint(event.getRawX(),event.getRawY()- ScreenUtils.getStatusBarHeight(mContext));
                break;
            case MotionEvent.ACTION_UP:
                mMessageBubbleView.handleActionUp();
                break;
        }

        return true;
    }

    /**
     * 从一个View中获取Bitmap
     */
    private Bitmap getBitmapByView(View view) {
        view.buildDrawingCache();
        Bitmap bitmap=view.getDrawingCache();
        return bitmap;
    }

    @Override
    public void restore() {
        //把消息移除
        mWindowManager.removeView(mMessageBubbleView);
        //把原来隐藏的View显示
        mStaticView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss(PointF pointF) {
        //要去执行爆炸
        //原来的View要移除
        mWindowManager.removeView(mMessageBubbleView);
        //要是WindowManager添加一个爆炸动画
        mWindowManager.addView(mBombFrame,mParams);
        mBombImage.setBackgroundResource(R.drawable.anim_bubble_pop);
        AnimationDrawable drawable= (AnimationDrawable) mBombImage.getBackground();
        mBombImage.setX(pointF.x-drawable.getIntrinsicWidth()/2);
        mBombImage.setY(pointF.y-drawable.getIntrinsicHeight()/2);

        drawable.start();
        mBombImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWindowManager.removeView(mBombFrame);
                //通知一下外面该View消失
                if(null!=mDisapperListener){
                    mDisapperListener.dismiss(mStaticView);
                }
            }
        },getAnimationDrawableTime(drawable));

    }

    private long getAnimationDrawableTime(AnimationDrawable drawable) {
        int numberOfFrames=drawable.getNumberOfFrames();
        long time=0;
        for(int i=0;i<numberOfFrames;i++){
            time+=drawable.getDuration(1);
        }
        return time;
    }

    public interface BubbleDisapperListener{
        void dismiss(View view);
    }
}
