package com.customview.view;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * des:花束直播-三阶贝塞尔曲线-自定义路径动画
 * author: Yang Weisi
 * date 2018/12/1 11:16
 */
public class LoveTypeEvaluator implements TypeEvaluator<PointF> {

    private PointF point1,point2;

    public LoveTypeEvaluator(PointF point1, PointF point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    /**
     *
     * @param t:[0,1]
     * @param point0
     * @param point3
     * @return
     */
    @Override
    public PointF evaluate(float t, PointF point0, PointF point3) {
        //开始套公式，公式有四个点，但是这里只有两个点，所以需要构造函数
        PointF pointF=new PointF();
        pointF.x = point0.x*(1-t)*(1-t)*(1-t)
                + 3*point1.x*t*(1-t)*(1-t)
                + 3*point2.x*t*t*(1-t)
                + point3.x*t*t*t;

        pointF.y = point0.y*(1-t)*(1-t)*(1-t)
                + 3*point1.y*t*(1-t)*(1-t)
                + 3*point2.y*t*t*(1-t)
                + point3.y*t*t*t;

        return pointF;
    }
}
