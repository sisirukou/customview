package com.customview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.customview.R;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/26 22:04
 */
public class ListScreenMenuAdapter extends BaseMenuAdapter {

    private String[] mItems={"类型","品牌","价格","更多"};
    private Context mContext;

    public ListScreenMenuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
       TextView tvTabView= (TextView) LayoutInflater.from(mContext ).inflate(R.layout.adapter_list_screen_tab,parent,false);
       tvTabView.setText(mItems[position]);
       return tvTabView;
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        //真正的开发过程中，不同的位置显示的布局不一样，需要switch
        TextView tvMenuView= (TextView) LayoutInflater.from(mContext ).inflate(R.layout.adapter_list_screen_menu,parent,false);
        tvMenuView.setText(mItems[position]);
        tvMenuView.setTextColor(Color.BLACK);
        tvMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeContainerMenu();
                Toast.makeText(mContext,"点击关闭",Toast.LENGTH_LONG).show();

            }
        });
        return tvMenuView;
    }

    @Override
    public void menuOpen(View tabView) {
        TextView tabTv=(TextView)tabView;
        tabTv.setTextColor(Color.RED);
    }

    @Override
    public void menuclose(View tabView) {
        TextView tabTv=(TextView)tabView;
        tabTv.setTextColor(Color.BLACK);
    }
}
