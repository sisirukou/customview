package com.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.customview.adapter.BaseMenuAdapter;
import com.customview.observer.MenuObserver;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/26 10:52
 *
 * 布局：LinearLayout=LinearLayout(放Tab点击的View)+阴影(View透明度变化)+菜单的内容(FrameLayout)
 *
 * 步骤：
 * 1. 布局实例化好(组合控件)
 * 2. 引入Adapter设计模式去适配
 *     Tab的样式是不一样的，不同的页面不同的项目是不一样的，所以我们要用Adapter设计模式去适配View(参考ListView)
 * 3. 执行动画
 *      3.1 进来的时候阴影和内容是不显示的(把它移上去)
 *      3.2 动画在执行是情况下，不要响应点击事件
 *      3.3 打开和关闭，变tab的颜色和显示，肯定不能写到这个类里面
 * 4. 测试
 */
public class ListDataScreen extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private LinearLayout mMenuTabView;//创建头部，用来存放Tab
    private FrameLayout mMenuMiddleView;//创建FrameLayout用来存放阴影(View) + 菜单内容布局(FrameLayout)
    private View mShadowView;//阴影
    private FrameLayout mMenuContainerView;//创建菜单，存放菜单内容

    //阴影颜色
    private int mShadowColor= 0x88888888;
    //菜单内容布局
    private int mMenuContainerColor= Color.parseColor("#ffffff");

    //筛选菜单的Adapter
    private BaseMenuAdapter mAdapter;

    private int mMenuContainerHeight;//内容菜单的高度

    private int mCurrentPosition=-1;//当前打开的位置

    private long DURATION_TIME=350;

    private boolean mAnimatorExcute;//动画是否在执行

    private AdapterMenuObserver mMenuObserver;//观察者


    public ListDataScreen(Context context) {
        this(context,null);
    }

    public ListDataScreen(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ListDataScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initLayout();
    }

    /**
     * 实例化布局
     */
    private void initLayout() {
        setOrientation(VERTICAL);
        //可以用代码创建
        //1.1 创建头部，用来存放Tab
        mMenuTabView=new LinearLayout(mContext);
        mMenuTabView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mMenuTabView);
        //1.2 创建FrameLayout用来存放阴影(View) + 菜单内容布局(FrameLayout)
        mMenuMiddleView=new FrameLayout(mContext);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight=1;
        mMenuMiddleView.setLayoutParams(params);
        addView(mMenuMiddleView);
        //创建阴影，可以不用设置LayoutParams 默认就是match_parent
        mShadowView=new View(mContext);
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0f);
        mShadowView.setVisibility(GONE);
        mShadowView.setOnClickListener(this);
        mMenuMiddleView.addView(mShadowView);
        //创建菜单，存放菜单内容
        mMenuContainerView=new FrameLayout(mContext);
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        mMenuMiddleView.addView(mMenuContainerView);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        if(mMenuContainerHeight==0 && height>0){
            //内容的高度设置为75%

            mMenuContainerHeight=(int)(height*75/100);
            ViewGroup.LayoutParams params=mMenuContainerView.getLayoutParams();
            params.height=mMenuContainerHeight;
            mMenuContainerView.setLayoutParams(params);

            //进来的时候阴影和内容是不显示的(把它移上去)
            mMenuContainerView.setTranslationY(-mMenuContainerHeight);
        }


    }

    /**
     * 设置Adapter
     * @param adapter
     */
    public void setAdapter(BaseMenuAdapter adapter){
        if(adapter==null && mMenuObserver!=null){
            mAdapter.unregisterDataSetObserver(mMenuObserver);
            throw new RuntimeException("请设置adapter!");
        }
        this.mAdapter=adapter;
        //注册观察者 具体的观察者对象 订阅
        mMenuObserver=new AdapterMenuObserver();
        mAdapter.registerDataSetObserver(mMenuObserver);

        //获取有多少条
        int count=mAdapter.getCount();
        for(int i=0;i<count;i++){
            //获取菜单的Tab
            View tabView=mAdapter.getTabView(i,mMenuTabView);
            mMenuTabView.addView(tabView);
            LinearLayout.LayoutParams params= (LayoutParams) tabView.getLayoutParams();
            params.weight=1;
            tabView.setLayoutParams(params);

            //设置点击事件
            setTabClick(tabView,i);

            //获取菜单内容
            View menuView=mAdapter.getMenuView(i,mMenuContainerView);
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);

        }



    }

    /**
     * 设置tab的点击事件
     * @param tabView
     * @param position
     */
    private void setTabClick(final View tabView, final int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentPosition==-1){//没打开状态
                    openMenu(position,tabView);
                }else {//打开状态
                    if(mCurrentPosition==position){//打开了，要关闭
                        closeMenu();
                    }else {//切换显示
                        View currentMenu=mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(GONE);
                        mAdapter.menuclose(mMenuTabView.getChildAt(mCurrentPosition));
                        mCurrentPosition=position;
                        currentMenu=mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(VISIBLE);
                        mAdapter.menuOpen(mMenuTabView.getChildAt(mCurrentPosition));
                    }
                }
            }
        });
    }

    /**
     * 关闭内容菜单
     */
    private void closeMenu() {
        ObjectAnimator translationAnimator=ObjectAnimator.ofFloat(mMenuContainerView,"translationY",0,-mMenuContainerHeight);
        translationAnimator.setDuration(DURATION_TIME);
        translationAnimator.start();

        mShadowView.setVisibility(VISIBLE);
        ObjectAnimator alphaAnimator=ObjectAnimator.ofFloat(mShadowView,"alpha",1f,0f);
        alphaAnimator.setDuration(DURATION_TIME);

        //要等关闭动画执行完才能去隐藏当前菜单
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(-1!=mCurrentPosition){
                    View menuView=mMenuContainerView.getChildAt(mCurrentPosition);
                    menuView.setVisibility(GONE);
                    mShadowView.setVisibility(GONE);
                    mAdapter.menuclose(mMenuTabView.getChildAt(mCurrentPosition));
                    mCurrentPosition=-1;
                }
                mAnimatorExcute=false;

            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExcute=true;
            }
        });

        alphaAnimator.start();
    }

    /**
     * 打开内容菜单
     * @param position
     * @param tabView
     */
    private void openMenu(final int position, final View tabView) {

        if(mAnimatorExcute){
            return;
        }

        mShadowView.setVisibility(VISIBLE);

        //获取当前位置，打开显示当前菜单，菜单是加到菜单容器
        View menuView=mMenuContainerView.getChildAt(position);
        menuView.setVisibility(VISIBLE);

        ObjectAnimator translationAnimator=ObjectAnimator.ofFloat(mMenuContainerView,"translationY",-mMenuContainerHeight,0);
        translationAnimator.setDuration(DURATION_TIME);
        translationAnimator.start();


        ObjectAnimator alphaAnimator=ObjectAnimator.ofFloat(mShadowView,"alpha",0f,1f);
        alphaAnimator.setDuration(DURATION_TIME);

        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExcute=false;
                mCurrentPosition=position;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExcute=true;
                //把当前tab传到外面
                mAdapter.menuOpen(tabView);

            }
        });


        alphaAnimator.start();

    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }

    /**
     * 具体是观察者
     */
    private class AdapterMenuObserver extends MenuObserver{

        @Override
        public void closeContainerMenu() {
            closeMenu();
        }
    }
}
