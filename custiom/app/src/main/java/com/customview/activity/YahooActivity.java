package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.customview.R;

/**
 * des:
 * author: Yang Weisi
 * date 2018/10/31 13:12
 */
public class YahooActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_yahoo);
    }


    public static void launch(Context context){
        Intent intent=new Intent(context, YahooActivity.class);
        context.startActivity(intent);
    }
}
