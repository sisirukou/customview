package com.customview.adapter;

import android.database.DataSetObservable;
import android.view.View;
import android.view.ViewGroup;

/**
 * des:流式布局的adapter
 * author: Yang Weisi
 * date 2018/9/14 11:01
 */
public abstract class TagLayoutBaseAdapter {

    //有多少条目
    public abstract int getCount();

    //getView
    public abstract View getView(int position, ViewGroup parent);

    //观察者模式及时通知更新
    public void unregisterDataSetObserver(DataSetObservable observable){

    }

    //观察者模式
    public void registerDataSetObserver(DataSetObservable observable){

    }


}
