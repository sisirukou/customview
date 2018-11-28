package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.customview.R;

import butterknife.ButterKnife;

/**
 * des:
 * author: Yang Weisi
 * date 2018/8/13 上午11:16
 */
public class RatingBarActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ratingbar);
        initView();
    }

    public static void launch(Context context){
        Intent intent=new Intent(context,RatingBarActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        ButterKnife.bind(this);

    }

}
