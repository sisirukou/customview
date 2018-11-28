package com.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.customview.utils.ScreenUtils;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/27 20:56
 */
public class LoadingView extends RelativeLayout{

    private LoadingViewCircle mLeftView,mMiddleView,mRightView;

    private int mTranslationDistance;
    private final long ANIMATION_TIME=350;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTranslationDistance=(int)ScreenUtils.dip2px(context,30);

        //添加三个View 但是要圆形
        mLeftView=getCircleView(context);
        mLeftView.exchangeColor(Color.BLUE);
        mMiddleView=getCircleView(context);
        mMiddleView.exchangeColor(Color.RED);
        mRightView=getCircleView(context);
        mRightView.exchangeColor(Color.GREEN);

        addView(mLeftView);
        addView(mRightView);
        addView(mMiddleView);

        post(new Runnable() {
            @Override
            public void run() {//让布局实例化之后再去开启动画
                //切换颜色 左边给中间，中间给右边，右边给左边
                int liftColor=mLeftView.getColor();
                int rightColor=mRightView.getColor();
                int middleColor=mMiddleView.getColor();
                mMiddleView.exchangeColor(liftColor);
                mRightView.exchangeColor(middleColor);
                mLeftView.exchangeColor(rightColor);

                //往外面跑
                expendAmimation();
            }
        });

    }

    /**
     * 开启动画,往外跑
     */
    private void expendAmimation() {
        //向左跑
        ObjectAnimator leftTranslationAnimator=ObjectAnimator.ofFloat(mLeftView,"translationX",0,-mTranslationDistance);
        //向右跑
        ObjectAnimator rightTranslationAnimator=ObjectAnimator.ofFloat(mRightView,"translationX",0,mTranslationDistance);

        AnimatorSet set=new AnimatorSet();
        set.setDuration(ANIMATION_TIME);
        set.playTogether(leftTranslationAnimator,rightTranslationAnimator);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                innerAmimation();
            }
        });
        set.start();

    }

    /**
     * 开启动画,往里跑
     */
    private void innerAmimation() {
        //向左跑
        ObjectAnimator leftTranslationAnimator=ObjectAnimator.ofFloat(mLeftView,"translationX",-mTranslationDistance,0);
        //向右跑
        ObjectAnimator rightTranslationAnimator=ObjectAnimator.ofFloat(mRightView,"translationX",mTranslationDistance,0);

        AnimatorSet set=new AnimatorSet();
        set.setDuration(ANIMATION_TIME);
        set.playTogether(leftTranslationAnimator,rightTranslationAnimator);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                expendAmimation();
            }
        });
        set.start();

    }

    private LoadingViewCircle getCircleView(Context context) {
        LoadingViewCircle circleView=new LoadingViewCircle(context);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int)ScreenUtils.dip2px(context,8),(int)ScreenUtils.dip2px(context,8));
        params.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(params);
        return circleView;
    }
}
