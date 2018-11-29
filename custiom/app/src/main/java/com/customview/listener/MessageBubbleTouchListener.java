package com.customview.listener;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.customview.view.MessageBubbleView;

/**
 * des:监听当前View的触摸事件
 * author: Yang Weisi
 * date 2018/11/29 22:18
 */
public class MessageBubbleTouchListener implements View.OnTouchListener {

    private View mStaticView;//原来需要拖动爆炸的View
    private WindowManager mWindowManager;
    private MessageBubbleView mMessageBubbleView;
    private WindowManager.LayoutParams mParams;

    public MessageBubbleTouchListener(View mStaticView,Context context) {
        this.mStaticView = mStaticView;
        mWindowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mMessageBubbleView=new MessageBubbleView(context);
        mParams=new WindowManager.LayoutParams();
        mParams.format= PixelFormat.TRANSLUCENT;
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
                mMessageBubbleView.initPoint(event.getRawX(),event.getRawY());

                break;
            case MotionEvent.ACTION_MOVE:
                mMessageBubbleView.updateDragPoint(event.getRawX(),event.getRawY());
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }
}
