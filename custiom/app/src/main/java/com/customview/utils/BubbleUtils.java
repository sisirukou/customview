package com.customview.utils;

import android.graphics.PointF;

/**
 * des:贝塞尔曲线-仿QQ消息拖动 工具类
 * author: Yang Weisi
 * date 2018/11/30 12:09
 */
public class BubbleUtils {

    /**
     *
     * @param p1
     * @param p2
     * @param percent
     * @return
     */
    public static PointF getPointByPercent(PointF p1,PointF p2,float percent){
        return new PointF(evaluateValue(percent,p1.x,p2.x),evaluateValue(percent,p1.y,p1.y));
    }

    /**
     * 根据分度值，计算从start到end中，fraction位置的值，fraction范围为0-1
     * @return
     */
    private static float evaluateValue(float fraction, Number start, Number end) {
        return start.floatValue()+(end.floatValue()-start.floatValue())*fraction;
    }

}
