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
 * date 2018/10/11 17:37
 */
public class QQSildMenuActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qqslidmenu);
    }


    public static void launch(Context context){
        Intent intent=new Intent(context, QQSildMenuActivity.class);
        context.startActivity(intent);
    }



}
