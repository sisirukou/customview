package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.customview.R;
import com.customview.utils.ScreenUtils;

/**
 * des:
 * author: Yang Weisi
 * date 2018/10/11 18:55
 *
 *
 * 回顾View的绘制流程
 * onFinishInflate() xml解析完毕，setContentView(R.layout.activity_main)
 * onLayout 摆放子View,View绘制流程中的最后一个方法  onMeasure()  onLayout()  onDraw(),而View的绘制流程在onResume()之后才调用的
 *
 */
public class KugouSlidMenu extends HorizontalScrollView {

    private int mMenuWidth;//菜单的宽度
    private View mContentView;//内容View
    private View mMenuView;//菜单View

    private boolean mMenuIsOpen=false;//判断菜单是否打开

    private boolean mIsIntercept=false;//是否拦截点击事件

    private GestureDetector mGestureDetector;

    public KugouSlidMenu(Context context) {
        this(context,null);
    }

    public KugouSlidMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public KugouSlidMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.KugouSlidMenu);
        float rightMargin =array.getDimension(R.styleable.KugouSlidMenu_menuRightMargin, ScreenUtils.dip2px(context,50));
        //计算菜单的宽度
        mMenuWidth=(int) (ScreenUtils.getScreenWidth(context)-rightMargin);

        array.recycle();

        mGestureDetector=new GestureDetector(mGestureListener);

    }


    private GestureDetector.OnGestureListener mGestureListener=new GestureDetector.SimpleOnGestureListener(){
        //快速滑动，只要快速滑动就会调用
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //判断菜单
            /**
             * 需要判断往哪边快速滑动，打开，往右；关闭，往左
             * 快速往左边滑是负数，快速往右边滑是正数
             */
            if(mMenuIsOpen){//菜单是打开，需要关闭菜单，往左边快速滑
                if(velocityX<0){
                    closeMenu();
                    return true;
                }
            }else {//菜单是关闭，需要打开菜单，往右快速滑动
                if(velocityX>0){
                    openMenu();
                    return true;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };


    //1.宽度不对(乱套了)，需要指定高度
    @Override
    protected void onFinishInflate() {
        //这个方法是布局(xml布局文件)解析完毕调用
        super.onFinishInflate();
        /* 指定宽高
           1.内容页宽度是屏幕的宽度
           2.菜单页的宽度是 屏幕宽度-右边一小部分距离(自定义属性)
         */

        //获取KugouSlidMenu的第一个子孩子 LinearLayout
        ViewGroup container=(ViewGroup)getChildAt(0);

        //只允许两个布局
        int childCount=container.getChildCount();
        if(2!=childCount){
            throw new RuntimeException("只能放置两个子View");
        }

        //LinearLayout的第一个子孩子-菜单
        mMenuView=container.getChildAt(0);
        //设置宽高，只能通过LayoutParams
        ViewGroup.LayoutParams menuParams=mMenuView.getLayoutParams();
        menuParams.width=mMenuWidth;
        mMenuView.setLayoutParams(menuParams);

        //LinearLayout的第二个子孩子-内容
        mContentView=container.getChildAt(1);
        ViewGroup.LayoutParams contentParams=mContentView.getLayoutParams();
        contentParams.width=ScreenUtils.getScreenWidth(getContext());
        mContentView.setLayoutParams(contentParams);

    }

    /**
     * onFinishInflate 是在onlayout()方法之前执行的，所以需要在onLayout()里面写scrollTo()
     * 默认情况下侧边栏是关闭的，所以一进来需要移动一段距离
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //2.默认情况下侧边栏是关闭的
        scrollTo(mMenuWidth,0);
    }

    //4.处理右边的缩放，左边的缩放和透明度，需要不断获取当前位置

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //算一个梯度值
        float scale=1f*l/mMenuWidth;//scale变化是从1-0
//        Log.e("TAG","---scale:"+scale);
        //右边缩放  最小0.7f，最大1f
        float rightScale=0.7f+0.3f*scale;
        //设置右边缩放
        ViewCompat.setPivotX(mContentView,0);
        ViewCompat.setPivotY(mContentView,mContentView.getMeasuredHeight()/2);
        ViewCompat.setScaleX(mContentView,rightScale);
        ViewCompat.setScaleY(mContentView,rightScale);

        //菜单透明度缩放  透明度是由半透明到完全透明  缩放值是0.7f-1f
        float alpla=0.7f+(1-scale)*0.3f;
        ViewCompat.setAlpha(mMenuView,alpla);//设置透明度
        //设置缩放
        ViewCompat.setScaleX(mMenuView,alpla);
        ViewCompat.setScaleY(mMenuView,alpla);

        //最后一个效果，退出这个按钮刚开始是在右边，按照我们目前的方法，永远在左边
        //设置平移
        ViewCompat.setTranslationX(mMenuView,0.25f*l);


    }

    /**
     * 事件拦截
     * 2.处理事件拦截 + ViewGroup 事件分发源码实践
     *           当菜单打开的时候，手指触摸右边内容需要关闭菜单，还需要拦截事件(打开情况下点击内容页不会响应点击事件)
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercept=false;
        if(mMenuIsOpen){
            float currentX=ev.getX();
            if(currentX>mMenuWidth){//标示触摸右边内容页，关闭菜单，子View不需要响应任何事件(点击和触摸)，拦截子View的事件
                closeMenu();
                mIsIntercept=true;
                //如果返回true,代表会拦截子VIew的事件，但是也会响应自己的onTouch事件
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    //3.手指抬起是二选一，要么关闭要么打开
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mIsIntercept){//如果有拦截，就不要执行自己的onTouch
            return true;
        }
        /**
         * 遗留作业：1.获取手指滑动速率，当前大于一定值就认为是快速滑动 或者用GestureDetector(是系统提供好的类，可以直接用)
         *
         */
        if(mGestureDetector.onTouchEvent(ev)){//想要调用快速滑动方法，必须在onTouch里写mGestureDetector
            //快速滑动触发了，就不能执行下面的方法了
            return true;
        }

        if(ev.getAction()==MotionEvent.ACTION_UP){//只需要管手指抬起，看当前滚动的距离
            int currentScrollX=getScrollX();
            if(currentScrollX>mMenuWidth/2){//关闭
                closeMenu();
            }else {//打开
                openMenu();
            }
            //确保super.onTouchEvent(ev)不会执行
            return true;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 关闭菜单，滚动到mMenuWidth的位置
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth,0);
        mMenuIsOpen=false;
    }

    /**
     * 打开菜单，就是滚动到0 的位置
     */
    private void openMenu() {
        smoothScrollTo(0,0);//是有动画的，scrollTo()是没有动画的
        mMenuIsOpen=true;
    }
}
