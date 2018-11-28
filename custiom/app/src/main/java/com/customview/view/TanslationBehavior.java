package com.customview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.customview.R;

/**
 * des:关注垂直滚动，而且向上的时候是出来，向下是隐藏
 * author: Yang Weisi
 * date 2018/11/14 15:05
 */
public class TanslationBehavior extends FloatingActionButton.Behavior{

    private LinearLayout mBottomTabView;

    private boolean isOut=false;

    /**
     * 构造方法一定要有，要不绝对报错
     * @param context
     * @param attrs
     */
    public TanslationBehavior(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        mBottomTabView = parent.findViewById(R.id.bottom_tab_layout);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    /**
     *  当coordinatorLayout 的子View试图开始嵌套滑动的时候被调用。当返回值为true的时候表明
     *  coordinatorLayout 充当nested scroll parent 处理这次滑动，需要注意的是只有当返回值为true
     *  的时候，Behavior 才能收到后面的一些nested scroll 事件回调（如：onNestedPreScroll、onNestedScroll等）
     *  这个方法有个重要的参数axes，表明处理的滑动的方向。
     *
     * @param coordinatorLayout 和Behavior 绑定的View的父CoordinatorLayout
     * @param child  和Behavior 绑定的View
     * @param directTargetChild
     * @param target
     * @param axes 嵌套滑动 应用的滑动方向{ViewCompat.SCROLL_AXIS_HORIZONTAL,ViewCompat.CROLL_AXIS_VERTICAL}
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }


    /**
     * 进行嵌套滚动时被调用
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed target 已经消费的x方向的距离
     * @param dyConsumed target 已经消费的y方向的距离
     * @param dxUnconsumed x 方向剩下的滚动距离
     * @param dyUnconsumed y 方向剩下的滚动距离
     */
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        //向上的时候是出来，向下是隐藏
        if(dyConsumed>0){//往上滑动,是隐藏，需要加一个标志位
            if(!isOut){//不是往下走，需要往下走
                CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)child.getLayoutParams();
                child.animate().translationY(params.bottomMargin+child.getMeasuredHeight()).setDuration(300).start();
                //处理底部位移动画
                mBottomTabView.animate().translationY(mBottomTabView.getMeasuredHeight()).setDuration(300).start();
                isOut=true;
            }
        }else {//往下滑动
            if(isOut){
                child.animate().translationY(0).setDuration(300).start();
                //处理底部位移动画
                mBottomTabView.animate().translationY(0).setDuration(300);
                isOut=false;
            }
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }
}
