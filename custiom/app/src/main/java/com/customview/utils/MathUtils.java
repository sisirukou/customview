package com.customview.utils;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/1 18:03
 */
public class MathUtils {

    public static double distance(double x1,double y1,double x2,double y2){
        return Math.sqrt(Math.abs(x1-x2) * Math.abs(x1-x2) + Math.abs(y1-y2) * Math.abs(y1-y2));
    }

    public static boolean checkInRound(float sx,float sy,float r,float x,float y){

        return Math.sqrt((sx-x)*(sx-x)+(sy-y)*(sy-y))<r;
    }
}
