package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.customview.R;
import com.customview.view.LetterSideBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * des:
 * author: Yang Weisi
 * date 2018/8/13 上午11:16
 */
public class LetterBarActivity extends AppCompatActivity implements LetterSideBar.LetterTouchListener {

    @BindView(R.id.tvLetter)
    TextView tvLetter;
    @BindView(R.id.letterSideBar)
    LetterSideBar letterSideBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lettersidebar);
        initView();
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, LetterBarActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        ButterKnife.bind(this);

        letterSideBar.setOnLetterTouchListener(this);

//        letterSideBar.setOnLetterTouchListener(new LetterSideBar.LetterTouchListener() {
//            @Override
//            public void touch(CharSequence letter, boolean isTouch) {
//                if(isTouch){
//                    tvLetter.setVisibility(ViewViewGroup.VISIBLE);
//                    tvLetter.setText(letter);
//                }else {
//                    tvLetter.setVisibility(ViewViewGroup.GONE);
//                }
//            }
//        });


    }

    @Override
    public void touch(CharSequence letter, boolean isTouch) {
        if (isTouch) {
            tvLetter.setVisibility(View.VISIBLE);
            tvLetter.setText(letter);
        } else {
            tvLetter.setVisibility(View.GONE);
        }
    }
}