package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.customview.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * des:
 * author: Yang Weisi
 * date 2018/6/19 下午3:25
 */
public class DragHelperActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;

    private List<String> mItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draghelper);

        ButterKnife.bind(this);


        init();

    }



    private void init() {
        mItems=new ArrayList<>();
        for(int i=0;i<200;i++){
            mItems.add("i->"+i);
        }

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                convertView= LayoutInflater.from(DragHelperActivity.this).inflate(R.layout.adapter_draghelper_item,parent,false);
                TextView tvItem=(TextView) convertView.findViewById(R.id.tvItem);
                tvItem.setText(mItems.get(position));

                return convertView;
            }
        });
    }

    public static void launch(Context context){
        Intent intent=new Intent(context,DragHelperActivity.class);
        context.startActivity(intent);
    }
}
