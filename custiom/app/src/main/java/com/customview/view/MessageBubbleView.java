package com.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.customview.listener.MessageBubbleTouchListener;
import com.customview.utils.ScreenUtils;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/28 22:37
 *
 * 1.普及数学基础知识(三角函数)
 *   消息拖拽(QQ)，贝塞尔曲线(轮播指示器，下拉刷新控件，花束直播点赞效果等等)
 *
 * 2.思路：
 *   实现的效果完全分离，任何控件都可以实现拖拽消失(一行代码解决)
 *
 * 3.分析
 *   3.1 有两个圆，一个是固定圆，位置固定不动，但是半径会变(两个圆之间的距离越远半径越小)
 *       还有一个是拖拽半径，是不变的，位置是跟随手指移动
 *   3.2 在两圆之间，中间会有一个粘性的不规则图像(贝塞尔曲线)
 *
 *       3.2.1 两个点的距离：(x0,y0),(x1,y1)
 *             用勾股定理：直角三角形两直角边a、b的平方和等于斜边c的平方a2+b2=c2
 *       3.2.2 贝塞尔曲线理解
 *             https://www.cnblogs.com/wjtaigwh/p/6647114.html
 *       3.2.3 sinA=对边比斜边
 *             cosA=邻边比斜边
 *             tanA=对边比邻边
 *             cotA=邻边比对边
 *
 * 4.完全与控件分离，一行代码解决控件爆炸
 *   架构的分离思想(不要耦合，高扩展BadgeView),架构思想核心(利他)
 *   4.1 怎么才能把一个View拖动到状态栏上面
 *       只要超过父布局就会别截取，所以可以把View放在WindowManager上面拖动，原来的View还在原来的位置，拖动的时候其实是新建了一个View,复制一张图片，在WindowManager上面拖动
 *
 *   4.2 怎么实现贝塞尔
 *
 *
 */
public class MessageBubbleView extends View{

    private PointF mFixationPoint,mDragPoint;//两个圆的圆心

    private int mDragRadius=10;//拖拽圆半径

    private int mFixactionRadiusMax=7;//初始圆半径最大值
    private int mFixactionRadiusMin=3;//初始圆半径最小值

    private int mFixactionRadius;//初始圆半径

    private Paint mPaint;//画笔


    public MessageBubbleView(Context context) {
        this(context,null);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragRadius= (int)ScreenUtils.dip2px(context,mDragRadius);
        mFixactionRadiusMax= (int)ScreenUtils.dip2px(context,mFixactionRadiusMax);
        mFixactionRadiusMin= (int)ScreenUtils.dip2px(context,mFixactionRadiusMin);
        mPaint=new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(mFixationPoint==null && mDragPoint==null){
            return;
        }
        //画两个圆
        //拖拽圆
        canvas.drawCircle(mDragPoint.x,mDragPoint.y,mDragRadius,mPaint);
        //固定圆 有一个初始化大小，而且它的半径是随着距离的增大而减小，小到一定程度就不见了(不画了)


        Path bezeierPath=getBezeierPath();

        if(null !=bezeierPath){//太小了就不画了
            //画圆
            canvas.drawCircle(mFixationPoint.x,mFixationPoint.y,mFixactionRadius,mPaint);
            //画贝塞尔曲线
            canvas.drawPath(bezeierPath,mPaint);
        }

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN://手指按下要去指定当前位置
//                float downX=event.getX();
//                float downY=event.getY();
//                initPoint(downX,downY);
//                break;
//            case MotionEvent.ACTION_MOVE://拖拽的时候更新位置
//                float moveX=event.getX();
//                float moveY=event.getY();
//                updateDragPoint(moveX,moveY);
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        invalidate();
//
//        return true;
//    }

    /**
     * 更新拖拽点的位置,实时更新
     */
    public void updateDragPoint(float moveX, float moveY) {
        mDragPoint.x=moveX;
        mDragPoint.y=moveY;
        //不断绘制
        invalidate();
    }

    /**
     * 初始化点的位置
     */
    public void initPoint(float downX,float downY){
        mFixationPoint=new PointF(downX,downY);
        mDragPoint=new PointF(downX,downY);
    }

    /**
     * 求两点之间的距离
     */
    private double getDistance(PointF point1, PointF point2) {
        return Math.sqrt((point1.x-point2.x)*(point1.x-point2.x)+(point1.y-point2.y)*(point1.y-point2.y));
    }

    /**
     * 获取贝塞尔曲线
     */
    public Path getBezeierPath(){
        //两个点的距离
        double distance=getDistance(mDragPoint,mFixationPoint);
        mFixactionRadius=(int)(mFixactionRadiusMax-distance/14);
        if(mFixactionRadius<mFixactionRadiusMin) {//超过一定距离 就不画了
            return null;
        }

        Path bezeierPath=new Path();

        //求∠A,先求斜率tanA,再反斜率atanA
        //求斜率
        float dy=mDragPoint.y-mFixationPoint.y;
        float dx=mDragPoint.x-mFixationPoint.x;
        float tanA =dy/dx;
        //求∠A
        double arcTan=Math.atan(tanA);

        // p0
        float p0x= (float) (mFixationPoint.x+mFixactionRadius*Math.sin(arcTan));
        float p0y= (float) (mFixationPoint.y-mFixactionRadius*Math.cos(arcTan));

        // p1
        float p1x= (float) (mDragPoint.x+mDragRadius*Math.sin(arcTan));
        float p1y= (float) (mDragPoint.y-mDragRadius*Math.cos(arcTan));

        // p2
        float p2x= (float) (mDragPoint.x-mDragRadius*Math.sin(arcTan));
        float p2y= (float) (mDragPoint.y+mDragRadius*Math.cos(arcTan));

        // p3
        float p3x= (float) (mFixationPoint.x-mFixactionRadius*Math.sin(arcTan));
        float p3y= (float) (mFixationPoint.y+mFixactionRadius*Math.cos(arcTan));

        //拼装 贝塞尔曲线路径
        bezeierPath.moveTo(p0x,p0y);
        //两个点 第一个点(控制点,用两点的中心)
        PointF controlPoint=getControlPoint();
        //画第一条线(用了三个点，p0,p1,控制点)
        bezeierPath.quadTo(controlPoint.x,controlPoint.y,p1x,p1y);
        //画第二条
        bezeierPath.lineTo(p2x,p2y);//连接到
        bezeierPath.quadTo(controlPoint.x,controlPoint.y,p3x,p3y);
        bezeierPath.close();


        return bezeierPath;
    }

    private PointF getControlPoint() {
        return new PointF((mDragPoint.x+mFixationPoint.x)/2,(mDragPoint.y+mFixationPoint.y)/2);
    }

    public interface BubbleDisapperListener{
        void dismiss(View view);
    }

    /**
     * 绑定可以拖拽的控件
     * @param view
     * @param disapperListener
     */
    public static void attach(View view, BubbleDisapperListener disapperListener) {
        if(null!=view){
            view.setOnTouchListener(new MessageBubbleTouchListener(view,view.getContext()));
        }
    }
}
