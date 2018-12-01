package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.customview.R;
import com.customview.view.LoveLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * des:
 * author: Yang Weisi
 * date 2018/10/31 13:12
 */
public class LoveLayoutActivity extends AppCompatActivity{

    @BindView(R.id.tvText)
    TextView tvText;

    @BindView(R.id.loveView)
    LoveLayout loveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lovelayout);
        ButterKnife.bind(this);


    }

    public static void launch(Context context){
        Intent intent=new Intent(context, LoveLayoutActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tvText})
    public void doOnClick(View view){
        switch (view.getId()){
            case R.id.tvText:
                for(int i=0;i<15;i++){
                    loveView.addLove();
                }
                break;
        }
    }
}
