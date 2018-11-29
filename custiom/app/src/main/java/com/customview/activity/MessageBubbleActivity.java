package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.customview.R;
import com.customview.view.MessageBubbleView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * des:
 * author: Yang Weisi
 * date 2018/10/31 13:12
 */
public class MessageBubbleActivity extends AppCompatActivity{

    @BindView(R.id.tvText)
    TextView tvText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_bubble);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        MessageBubbleView.attach(tvText, new MessageBubbleView.BubbleDisapperListener() {
            @Override
            public void dismiss(View view) {

            }
        });
    }


    public static void launch(Context context){
        Intent intent=new Intent(context, MessageBubbleActivity.class);
        context.startActivity(intent);
    }
}
