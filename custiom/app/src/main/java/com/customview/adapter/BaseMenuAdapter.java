package com.customview.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.customview.observer.MenuObserver;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/26 13:43
 */
public abstract class BaseMenuAdapter {


    private MenuObserver mObservers;

    public void registerDataSetObserver(MenuObserver observer) {
        mObservers=observer;
    }

    public void unregisterDataSetObserver(MenuObserver observer) {
        mObservers=null;
    }

    public void closeContainerMenu(){
        if(mObservers!=null){
            mObservers.closeContainerMenu();
        }
    }

    //获取总共有多少条
    public abstract int getCount();

    //获取当前的TabView
    public abstract View getTabView(int position, ViewGroup parent);

    //获取当前的菜单内容
    public abstract View getMenuView(int position,ViewGroup parent);

    //菜单打开
    public void menuOpen(View tabView) {
    }

    //菜单关闭
    public void menuclose(View tabView) {

    }
}
