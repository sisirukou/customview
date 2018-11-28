package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.customview.R;
import com.customview.view.City58Loading;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * des:
 * author: Yang Weisi
 * date 2018/10/31 13:12
 */
public class City58Activity extends AppCompatActivity{

    @BindView(R.id.loadingView)
    City58Loading loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_58city);
        ButterKnife.bind(this);
    }


    public static void launch(Context context){
        Intent intent=new Intent(context, City58Activity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tvData})
    public void doClick(View view){
        switch (view.getId()){
            case R.id.tvData:
                loadingView.setVisibility(View.GONE);
                break;
        }
    }
}
