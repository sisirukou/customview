package com.customview.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;

import com.customview.R;
import com.customview.view.QQStepView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * des:
 * author: Yang Weisi
 * date 2018/6/19 下午3:25
 */
public class QQStepViewActivity extends AppCompatActivity {

    @BindView(R.id.stepView)
    QQStepView stepView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqstepview);

        ButterKnife.bind(this);


        init();

    }



    private void init() {
        stepView.setStepMax(4000);
        //属性动画
        ValueAnimator valueAnimator=ObjectAnimator.ofFloat(0,3000);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currentStep=(float)valueAnimator.getAnimatedValue();
                stepView.setStepCurrent((int)currentStep);
            }
        });
    }

    public static void launch(Context context){
        Intent intent=new Intent(context,QQStepViewActivity.class);
        context.startActivity(intent);
    }
}
