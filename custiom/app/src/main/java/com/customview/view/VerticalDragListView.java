package com.customview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ListViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * des:
 * author: Yang Weisi
 * date 2018/10/23 21:34
 *
 * 1.ViewDragHelper介绍
 *  1.1 创建
 *   mDragHelper=ViewDragHelper.create(this,mDragHelperCallback);
 *  1.2 实现
 *
 *
 *
 * 2.效果分析实现：
 *  2.1 后面布局不能拖动
 *  2.2 列表只能垂直拖动
 *  2.3 垂直拖动的范围只能是后面菜单View的高度
 *      在哪里获取控件的高度？获取控件宽高一定要在测量完毕之后才能拿到，也就是在onMeasure()之后，可以在onLayout()中获取，因为onMeasure()会调用多次
 *  2.4 手指松开的时候两者选其一，要么打开要么关闭
 *
 * 3.事件的分发和拦截
 *
 *
 */
public class VerticalDragListView extends FrameLayout{

    //可以认为这是系统给我们写好的一个工具类
    private ViewDragHelper mDragHelper;
    private View mDragListView;//前面布局
    private int mMenuHeight;//后面菜单的高度
    private float mDownY;//手指按下的距离
    private boolean mMenuIsOpen=false;//菜单是否打开


    public VerticalDragListView(@NonNull Context context) {
        this(context,null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper=ViewDragHelper.create(this,mDragHelperCallback);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed){
            View menuView=getChildAt(0);
            mMenuHeight=menuView.getMeasuredHeight();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount=getChildCount();
        if(2!=childCount){
            throw new RuntimeException("VerticalDragListView 只能包含两个子布局");
        }

        mDragListView=getChildAt(1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mDragHelper.processTouchEvent(event);

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父控件不拦截子控件的触摸事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    /*
        because ACTION_DOWN was not received for this pointer before ACTION_MOVE.
        VerticalDragListView.onInterceptTouchEvent().DOWN -> ListView.onTouch()
        -> VerticalDragListView.onInterceptTouchEvent().MOVE
        ->VerticalDragListView.onTouchEvent().MOVE
        ->


     */

    /**
     * 现象就是ListView可以滑动，但是菜单滑动没有效果了
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(mMenuIsOpen){//菜单打开要全部拦截
            return true;
        }

        //向下滑动拦截，不要给ListView做处理
        //谁拦截谁？父View拦截子View，但是子View可以调用requestDisallowInterceptTouchEvent()方法请求父View不要拦截，改变的其实是mGroupFlags的值
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY=ev.getY();
                //让DragHelper拿一个完整的事件
                mDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY=ev.getY();
                if((moveY-mDownY)>0 && !canChildScrollUp()){
                    //向下滑动 && 滚动到顶部，拦截不让ListView做处理
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private ViewDragHelper.Callback mDragHelperCallback=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            //指定该子View是否可以拖动，就是child()
            //只能是列表可以拖动
            return mDragListView==child;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            //垂直拖动移动的位置
            //垂直拖动的范围只能是后面菜单View的高度

            if(top<=0){
                top=0;
            }

            if(top>=mMenuHeight){
                top=mMenuHeight;
            }

            return top;
        }

//        @Override
//        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
//            //水平拖动移动的位置
//            return left;
//        }


        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            //2.4 手指松开的时候两者选其一，要么打开要么关闭
            if(releasedChild==mDragListView){
                if(mDragListView.getTop()>mMenuHeight/2){//大于高度的一半，滚动到菜单的高度(打开)
                    mDragHelper.settleCapturedViewAt(0,mMenuHeight);
                    mMenuIsOpen=true;
                }else {//滚动到0的位置(关闭)
                    mDragHelper.settleCapturedViewAt(0,0);
                    mMenuIsOpen=false;
                }
                invalidate();
            }

        }
    };

    /**
     * 响应滚动
     */
    @Override
    public void computeScroll() {
        if(mDragHelper.continueSettling(true)){
            invalidate();
        }
    }
    /**
     * 判断View是否滚动到了最顶部
     */
    public boolean canChildScrollUp() {
        if (mDragListView instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mDragListView, -1);
        }
        return mDragListView.canScrollVertically(-1);
    }
}
