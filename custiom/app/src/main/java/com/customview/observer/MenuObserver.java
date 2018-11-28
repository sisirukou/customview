package com.customview.observer;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/27 18:20
 */
public abstract class MenuObserver {
    //可以参考一下ListView的源码，setAdapter()和BaseAdapter
    public abstract void closeContainerMenu();
}
