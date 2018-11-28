package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.customview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * des:
 * author: Yang Weisi
 * date 2018/8/13 上午11:16
 *
 * CoordinatorLayout:用来协调各个直接子控件
 *
 * 头部ToolBar用AppBarLayout包裹，中间用RecyclerView，按钮FloatingActionButton，底部用可以用BottomNavigationView
 *
 *
 *
 */
public class BehaviorActivity extends AppCompatActivity{

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_behavior);
        initView();
    }

    public static void launch(Context context){
        Intent intent=new Intent(context,BehaviorActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(BehaviorActivity.this).inflate(R.layout.adapter_behavior_item,parent,false);
                ViewHolder viewHolder=new ViewHolder(itemView);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
