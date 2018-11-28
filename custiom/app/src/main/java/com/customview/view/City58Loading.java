package com.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.customview.R;
import com.customview.utils.ScreenUtils;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/24 10:48
 * 效果分析：
 * 加载显示组合控件(布局)布局分三个部分=ShapeView+View(阴影)+TextView
 * 动画：
 * 1.位移动画，形状的View上抛和下落
 * 2.缩放动画，中间阴影，放大和缩小
 * 3.下落位移的时候配合中间阴影缩小，上抛的时候配合中间阴影放大
 * 4.差值器，动画速率的问题
 * 5.下落的时候改变形状
 * 6.旋转动画，正方形180°，三角形120°
 *
 * 性能优化
 * 性能优化，源码
 * 当后台数据返回时，要把当前页面设置为GONE(隐藏),只是用代码设置了隐藏，但是动画还在跑
 */
public class City58Loading extends LinearLayout{

    private ShapeView mShapeView;//形状
    private View mShadowView;//阴影

    private int mTranslationDistance=0;
    //动画执行的时间
    private final long ANIMATOR_DURATION=350;

    //是否停止动画
    private boolean mIsStopAnimator=false;

    public City58Loading(Context context) {
        this(context,null);
    }

    public City58Loading(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public City58Loading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance=(int)ScreenUtils.dip2px(context,80);
        initLayout();
    }

    /**
     * 初始化加载布局
     */
    private void initLayout(){
        //加载写好的布局 view_58city_laoding
        inflate(getContext(), R.layout.view_58city_loading,this);
        mShapeView=(ShapeView) findViewById(R.id.shapeView);
        mShadowView=findViewById(R.id.shadowView);
        post(new Runnable() {
            @Override
            public void run() {
                //在onResume()方法之后View的绘制流程之后执行（View的绘制流程源码分析那一章）
                startFallAnimator();
            }
        });
    }

    /**
     * 下落动画
     */
    private void startFallAnimator() {

        if(mIsStopAnimator){
            return;
        }

        //属性动画
        //下落位移动画
        ObjectAnimator translationAnimator=ObjectAnimator.ofFloat(mShapeView,"translationY",0,mTranslationDistance);
//        translationAnimator.setDuration(ANIMATOR_DURATION);
        //中间阴影缩小
        ObjectAnimator scaleAnimator=ObjectAnimator.ofFloat(mShadowView,"scaleX",1f,0.3f);
//        scaleAnimator.setDuration(ANIMATOR_DURATION);

        //组合起来
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_DURATION);
        //设置一个加速差值器，下落是速度应该是越来越快
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(translationAnimator,scaleAnimator);
        //下落完之后就上抛了，监听动画执行完毕
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //改变形状
                mShapeView.exchange();
                startUpAnimator();

            }
        });
        animatorSet.start();

    }

    /**
     *开始执行上抛动画
     */
    private void startUpAnimator() {

        if(mIsStopAnimator){
            return;
        }

        //属性动画
        //上抛位移动画
        ObjectAnimator translationAnimator=ObjectAnimator.ofFloat(mShapeView,"translationY",mTranslationDistance,0);
//        translationAnimator.setDuration(ANIMATOR_DURATION);
        //中间阴影放大
        ObjectAnimator scaleAnimator=ObjectAnimator.ofFloat(mShadowView,"scaleX",0.3f,1f);
//        scaleAnimator.setDuration(ANIMATOR_DURATION);

        //组合起来
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_DURATION);
        //设置一个减速差值器，上抛是越来越慢的
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(translationAnimator,scaleAnimator);
        //下落完之后就上抛了，监听动画执行完毕
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startFallAnimator();

            }

            @Override
            public void onAnimationStart(Animator animation) {
                //开始旋转
                startRotationAnimator();
            }
        });
        animatorSet.start();
    }

    /**
     * 上抛的时候需要旋转
     */
    private void startRotationAnimator() {
        ObjectAnimator rotationAnimator=null;
        switch (mShapeView.getCurrentShape()){
            case Circle:
            case Square://正方形，180°
                rotationAnimator=ObjectAnimator.ofFloat(mShapeView,"rotation",0,180);
                break;
            case Triangle://三角形 120°
                rotationAnimator=ObjectAnimator.ofFloat(mShapeView,"rotation",0,-120);
                break;
        }
        rotationAnimator.setDuration(ANIMATOR_DURATION);
        rotationAnimator.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(View.INVISIBLE);//不要再去摆放和计算，少走一些系统源码
        //清理动画
        mShapeView.clearAnimation();
        mShadowView.clearAnimation();
        //把City58Loading从父布局移除
        ViewGroup parent= (ViewGroup) getParent();
        if(parent!=null){
            parent.removeView(this);//从父布局中移除
            removeAllViews();//移除自己所有的布局
        }

        mIsStopAnimator=true;

    }
}
