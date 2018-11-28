package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.customview.R;
import com.customview.adapter.TagLayoutBaseAdapter;
import com.customview.view.TagLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * des:
 * author: Yang Weisi
 * date 2018/9/10 17:46
 */
public class TagLayoutActivity extends AppCompatActivity{

    @BindView(R.id.tagLayout)
    TagLayout tagLayout;



    private List<String> mItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taglayout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //后台获取
        mItems=new ArrayList<>();
        mItems.add("sljlsg");
        mItems.add("--就是理解的感觉");
        mItems.add("--就是龙卷风");
        mItems.add("--个明年");
        mItems.add("--感觉还好你");


        tagLayout.setAdapter(new TagLayoutBaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                TextView tagTv= (TextView) LayoutInflater.from(TagLayoutActivity.this).inflate(R.layout.adapter_tablayout_item,parent,false);
                tagTv.setText(mItems.get(position));
                return tagTv;
            }
        });

    }

    public static void launch(Context context){
        Intent intent=new Intent(context, TagLayoutActivity.class);
        context.startActivity(intent);
    }



}
