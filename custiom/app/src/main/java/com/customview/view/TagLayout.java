package com.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.customview.adapter.TagLayoutBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * des:流式标签
 * author: Yang Weisi
 * date 2018/9/7 11:54
 *
 * performLayout:ViewViewGroup -> layout() -> onLayout() -> 摆放子布局 for循环所有子View,前提不是GONE，调用chlid.layout()
 * performDraw():ViewViewGroup -> draw() -> drawBackground()//画背景
 * onDraw(Canvas);//画自己 ViewGroup 默认情况下不会掉
 * dispatchDraw();//画子View 不断循环调用子View的draw()
 *
 *
 *View的绘制流程
 *第一步：preformMeasure()：用于指定和测量layout中所有控件的宽高，
 * 对应ViewGroup,先去测量里面的子孩子，根据子孩子的宽高再来计算和指定自己的宽高，
 * 对于View，他的宽高是由自己和父布局决定的
 *第二步：preformLayout()：用于摆放子布局，for循环所有子View，用child.layout()摆放childView
 * 第三步：preformDraw:用于绘制自己和子View，对于ViewGroup首先绘制自己的背景，
 * for循环绘制子View，调用子View的draw()方法，对于View绘制自己的背景，绘制自己显示的内容(如TextView绘制文字，背景)
 *
 * 思考：如果要获取View的高度
 * 1.前提肯定需要调用测量方法，测量完毕之后才能获取宽高
 * 2.View的绘制流程是在onResume()之后才开始的(activity的启动流程源码)
 * 3.addView、setVisibility 等等 会调用requestLayout();重新走一遍View的绘制流程
 * 4.优化的时候，根据知道源码写代码的时候优化，onDraw(),不要布局嵌套，等等
 *
 * onMeasure()指定宽高
 * for循环测量子View
 * 根据子View计算和指定自己的布局
 *
 * onLayout()
 * for循环摆放所有的子View
 *
 *
 * 子View的margin值
 *
 * 小任务:
 * 高度不一样，高度累加是加上一行条目中最大的高度
 * 写的时候，需要从后台获取数据
 */
public class TagLayout extends ViewGroup{

    private TagLayoutBaseAdapter mAdapter;

    private List<List<View>> mChildViews=new ArrayList<>();

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //清空集合
        mChildViews.clear();

        int childCount=getChildCount();

        //获取到宽度
        int width=MeasureSpec.getSize(widthMeasureSpec);

        //高度需要计算
        int height=getPaddingTop()+getPaddingBottom();

        //一行的宽度
        int lineWidth=getPaddingLeft();

        //单独一行行放开
        ArrayList<View> childViews=new ArrayList<>();
        mChildViews.add(childViews);

        //子View高度不一致的情况下
        int maxHeight=0;

        for(int i=0;i<childCount;i++){
            //1.for循环测量子View
            View childView =getChildAt(i);

            if(childView.getVisibility()==GONE){
                continue;
            }

            measureChild(childView,widthMeasureSpec,heightMeasureSpec);

            //margin值需要通过LayoutParams获取,ViewGroup.LayoutParams 没有，就用系统的MarginLayoutParams
            // Linearlayout有
            //LinearLayout有自己的LayoutParams继承ViewGroup的MarginLayoutParams
            //会复写一个非常重要的方法
            ViewGroup.MarginLayoutParams params= (MarginLayoutParams) childView.getLayoutParams();

            //2.根据子View计算和指定自己的布局
            //什么时候需要换行，一行不够的情况下,还需要考虑margin
            if(lineWidth+(childView.getMeasuredWidth()+params.leftMargin+params.rightMargin)>width){//宽度大于行，需要换行，累加高度
                //高度加的不对，高度累应该是加上一行条目中最大的高度，摆放也是
                height+=maxHeight;
                lineWidth=childView.getMeasuredWidth()+params.leftMargin+params.rightMargin;
                //在需要换行的情况下
                childViews=new ArrayList<>();
                mChildViews.add(childViews);

            }else {
                lineWidth+=childView.getMeasuredWidth()+params.leftMargin+params.rightMargin;
//                childViews.add(childView);//不需要换行的时候添加到集合里
                //第一行没有换行，所以在里面加高度
                maxHeight=Math.max(childView.getMeasuredHeight()+params.topMargin+params.bottomMargin,maxHeight);
            }
            childViews.add(childView);
        }

        height +=maxHeight;

        //指定自己的宽高
        setMeasuredDimension(width,height);

    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    /**
     * 摆放子View
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount=getChildCount();
        int left,top=getPaddingTop(),right,bottom;

        for (List<View> chlidViews : mChildViews) {
            left=getPaddingLeft();
            int maxHeight=0;

            for (View childView : chlidViews) {

                if(childView.getVisibility()==GONE){
                    continue;
                }

                ViewGroup.MarginLayoutParams params= (MarginLayoutParams) childView.getLayoutParams();

                left+=params.leftMargin;
                int childTop=top+params.topMargin;
                right=left+childView.getMeasuredWidth();
                bottom=childTop+childView.getMeasuredHeight();
                //摆放
                childView.layout(left,childTop,right,bottom);
                //left叠加
                left +=childView.getMeasuredWidth()+params.rightMargin;

                //取最大值
                int childHeight=childView.getMeasuredHeight()+params.topMargin+params.bottomMargin;
                maxHeight=Math.max(maxHeight,childHeight);
            }

            ViewGroup.MarginLayoutParams params= (MarginLayoutParams) chlidViews.get(0).getLayoutParams();


            //不断叠加top值
            top += maxHeight;
        }

    }

    public void setAdapter(TagLayoutBaseAdapter adapter){
        if(adapter==null){
            //抛空指针异常
        }

        //清空所有的子View
        removeAllViews();

        mAdapter=null;
        mAdapter=adapter;

        //获取数量
        int childCount=mAdapter.getCount();
        for(int i=0;i<childCount;i++){
            //通过位置获取View
            View childView=mAdapter.getView(i,this);
            addView(childView);
        }

    }


}
