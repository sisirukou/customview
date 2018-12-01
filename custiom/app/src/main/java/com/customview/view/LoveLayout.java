package com.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.customview.R;

import java.util.Random;

/**
 * des:花束直播点赞效果
 * author: Yang Weisi
 * date 2018/12/1 10:17
 *
 * 三阶贝塞尔曲线公式
 *  P = P0(1-t)^3 + 3P1t(1-t)^2 + 3P2(1-t)t^2 + P3*t^3
 *  确定点
 *  p0: 固定的,底部的中心,x:控件宽/2；y:控件高-图片高/2
 *  p1: x:不固定,随机,只要在屏幕内就可以；y:
 *  p2: x:不固定,随机,只要在屏幕内就可以；y:p2的y要比p1大就行
 *  p3: 最上面，x:随机；y:0
 */
public class LoveLayout extends RelativeLayout{

    private Random mRandom;//随机数
    private int[] mImageRes;//图片资源
    private int mWidth,mHeight;//控件的宽高
    private int mDrawableWidth,mDrawableHeight;//图片的宽高

    private Interpolator[] mInterpolator;

    public LoveLayout(Context context) {
        this(context,null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRandom=new Random();
        mImageRes=new int[]{R.mipmap.pl_blue,R.mipmap.pl_red,R.mipmap.pl_yellow};


        Drawable drawable= ContextCompat.getDrawable(context,R.mipmap.pl_blue);
        mDrawableWidth=drawable.getIntrinsicWidth();
        mDrawableHeight=drawable.getIntrinsicHeight();

        mInterpolator=new Interpolator[]{new AccelerateInterpolator(),new DecelerateInterpolator(),
                new AccelerateDecelerateInterpolator(),new LinearInterpolator()};

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件的宽高
        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 添加一个点赞的View
     */
    public void addLove(){
        //添加一个ImageView在底部
        ImageView loveIv=new ImageView(getContext());
        //给一个图片资源
        loveIv.setImageResource(mImageRes[mRandom.nextInt(mImageRes.length-1)]);
        //添加到底部 LayoutParams
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        loveIv.setLayoutParams(params);
        addView(loveIv);

        //添加的效果
        AnimatorSet animatorSet=getAnimator(loveIv);


    }

    /**
     * 动画效果
     * @return
     */
    private AnimatorSet getAnimator(final ImageView iv) {
        AnimatorSet allAnimatorSet=new AnimatorSet();
        //添加效果：放大和透明度变化(属性动画)
        AnimatorSet innerAnimator=new AnimatorSet();
        ObjectAnimator alphaAnimator=ObjectAnimator.ofFloat(iv,"alpha",0.3f,1f);
        ObjectAnimator scaleXAnimator=ObjectAnimator.ofFloat(iv,"scaleX",0.3f,1f);
        ObjectAnimator scaleYAnimator=ObjectAnimator.ofFloat(iv,"scaleY",0.3f,1f);
        innerAnimator.playTogether(alphaAnimator,scaleXAnimator,scaleYAnimator);
        innerAnimator.setDuration(350);

        //运动路径动画
        allAnimatorSet.playSequentially(innerAnimator,getBezierAnimator(iv));
        allAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //将image移除
                removeView(iv);
            }
        });
        allAnimatorSet.start();
        return allAnimatorSet;
    }

    /**
     * 运动路径动画
     * @return
     */
    private Animator getBezierAnimator(final ImageView iv) {

        //点
        PointF point0=new PointF(mWidth/2-mDrawableWidth/2,mHeight-mDrawableHeight);
        //确保 p2点的y值 一定要大于 p1点的y值
        PointF point1=getPoint(1);
        PointF point2=getPoint(2);
        PointF point3=new PointF(mRandom.nextInt(mWidth)-mDrawableWidth,0);


        //TypeEvaluator
        LoveTypeEvaluator typeEvaluator=new LoveTypeEvaluator(point1,point2);
        //ofFloat参数:第一个参数typeEvaluator，第二个参数p0,第三参数p3
        ValueAnimator bezierAnimator=ObjectAnimator.ofObject(typeEvaluator,point0,point3);
        bezierAnimator.setDuration(3000);
        //添加一些随机的差值器(效果更好)
        bezierAnimator.setInterpolator(mInterpolator[mRandom.nextInt(mInterpolator.length-1)]);
        bezierAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                iv.setX(pointF.x);
                iv.setY(pointF.y);
                //透明度
                float t=animation.getAnimatedFraction();
                iv.setAlpha(1-t+0.2f);

            }
        });
        return bezierAnimator;
    }

    private PointF getPoint(int index) {
        return new PointF(mRandom.nextInt(mWidth)-mDrawableWidth,mRandom.nextInt(mHeight/2)+(index-1)*(mHeight/2));
    }
}
