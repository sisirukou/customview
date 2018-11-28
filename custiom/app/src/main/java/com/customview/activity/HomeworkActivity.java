package com.customview.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.customview.R;
import com.customview.view.HomeWorkProgressBar;
import com.customview.view.HomeworkLoading;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * des:
 * author: Yang Weisi
 * date 2018/6/19 下午3:25
 */
public class HomeworkActivity extends AppCompatActivity {

    @BindView(R.id.homeworkProgerssBar)
    HomeWorkProgressBar homeworkProgerssBar;
    @BindView(R.id.homeworkLoading)
    HomeworkLoading homeworkLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        ButterKnife.bind(this);


        init();

    }



    private void init() {


    }

    public static void launch(Context context){
        Intent intent=new Intent(context,HomeworkActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tvProgreBar,R.id.tvLoading})
    public void doClick(View view){
        switch (view.getId()){
            case R.id.tvProgreBar:
                homeworkProgerssBar.setStepMax(4000);
                //属性动画
                ValueAnimator valueAnimator= ObjectAnimator.ofFloat(0,4000);
                valueAnimator.setDuration(2000);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.start();
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float currentStep=(float)valueAnimator.getAnimatedValue();
                        homeworkProgerssBar.setStepCurrent((int)currentStep);
                    }
                });
                break;
            case R.id.tvLoading:
//                ValueAnimator animator=ObjectAnimator.ofFloat(0,4000);
//                animator.setDuration(4000*1000);
//                animator.start();
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                        homeworkLoading.exchange();
//                    }
//                });

                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                while (true){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            homeworkLoading.exchange();
                                        }
                                    });
                                    try {
                                        Thread.sleep(1000);

                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                }


                            }
                        }
                ).start();

                break;
        }
    }
}
