package com.customview.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.customview.R;
import com.customview.fragment.ItemFragment;
import com.customview.view.ColorTrackTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * des:
 * author: Yang Weisi
 * date 2018/6/19 下午3:25
 */
public class ColorTrackActivity extends AppCompatActivity {

    @BindView(R.id.tvColorTrack)
    ColorTrackTextView tvColorTrack;
    @BindView(R.id.indicatorView)
    LinearLayout mIndicatorContainer;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    private List<ColorTrackTextView> mIndicators;
    private String[] items={"直播","推荐","视频","图片","段子","精华"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colortrack);

        ButterKnife.bind(this);

        mIndicators=new ArrayList<>();



        initViewPager();
        initIndicator();


    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position<items.length-1){
                    //1.左边 位置position
                    ColorTrackTextView left=mIndicators.get(position);
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                    left.setCurrentProgress(1-positionOffset);

                    //2.右边 位置position+1
                    ColorTrackTextView right=mIndicators.get(position+1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrentProgress(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIndicator() {
        for(int i=0;i<items.length;i++){
            //动态添加颜色跟着TextView
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight=1;
            ColorTrackTextView colorTrackTextView=new ColorTrackTextView(this);
            //设置颜色
            colorTrackTextView.setTextSize(20);
            colorTrackTextView.setChangeColor(Color.RED);
            colorTrackTextView.setText(items[i]);
            colorTrackTextView.setLayoutParams(params);
            //把新的加入LinearLayout容器
            mIndicatorContainer.addView(colorTrackTextView);
            mIndicators.add(colorTrackTextView);



        }
    }



    public static void launch(Context context){
        Intent intent=new Intent(context,ColorTrackActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tvLeftToRight,R.id.tvRightToLeft})
    public void doClick(View view){
        switch (view.getId()){
            case R.id.tvLeftToRight:
                tvColorTrack.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                ValueAnimator valueAnimator= ObjectAnimator.ofFloat(0,1);
                valueAnimator.setDuration(2000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        float currentProgress=(float) animator.getAnimatedValue();
                        tvColorTrack.setCurrentProgress(currentProgress);

                    }
                });
                valueAnimator.start();
            break;
            case R.id.tvRightToLeft:
                tvColorTrack.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                ValueAnimator valueAnimator1= ObjectAnimator.ofFloat(0,1);
                valueAnimator1.setDuration(2000);
                valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        float currentProgress=(float) animator.getAnimatedValue();
                        tvColorTrack.setCurrentProgress(currentProgress);

                    }
                });
                valueAnimator1.start();
                break;
        }
    }
}
