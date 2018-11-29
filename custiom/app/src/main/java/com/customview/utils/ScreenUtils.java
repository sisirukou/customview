package com.customview.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * des:
 * author: Yang Weisi
 * date 2018/10/15 15:43
 */
public class ScreenUtils {

    public static float dip2px(Context context,int dpValut){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValut*scale+0.5f);
    }

    public static int getScreenWidth(Context context){
        WindowManager wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;//获取屏幕宽度(像素)
        return width;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context Context){
        int resourceId=Context.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            //根据资源ID获取响应的尺寸
            return Context.getResources().getDimensionPixelSize(resourceId);
        }
        return (int)dip2px(Context,25);
    }


}
