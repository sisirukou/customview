package com.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.customview.activity.BehaviorActivity;
import com.customview.activity.City58Activity;
import com.customview.activity.ColorTrackActivity;
import com.customview.activity.DragHelperActivity;
import com.customview.activity.HomeworkActivity;
import com.customview.activity.KugoSildMenuActivity;
import com.customview.activity.LetterBarActivity;
import com.customview.activity.ListDataScreenActivity;
import com.customview.activity.LoadingViewActivity;
import com.customview.activity.LockPatternActivity;
import com.customview.activity.LoveLayoutActivity;
import com.customview.activity.MessageBubbleActivity;
import com.customview.activity.ParallaxViewPagerActivity;
import com.customview.activity.QQSildMenuActivity;
import com.customview.activity.QQStepViewActivity;
import com.customview.activity.RatingBarActivity;
import com.customview.activity.TagLayoutActivity;
import com.customview.adapter.MainAdapter;
import com.customview.bean.MainDataBean;
import com.customview.imp.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MainAdapter adapter;

    @BindString(R.string.main_qqstep)
    String main_qqstep;//仿QQ运动步数进度条
    @BindString(R.string.main_colortrack)
    String main_colortrack;//玩转字体变色
    @BindString(R.string.main_progressbar)
    String main_progressbar;//炫酷进度条
    @BindString(R.string.main_ratingbar)
    String main_ratingbar;//仿淘宝评分
    @BindString(R.string.main_lettersidebar)
    String main_lettersidebar;//字母索引
    @BindString(R.string.main_taglayout)
    String main_taglayout;//流布局--
    @BindString(R.string.main_kugou_slidmenu)
    String main_kugou_slidmenu;//仿酷狗侧滑
    @BindString(R.string.main_qq_slidmenu)
    String main_qq_slidmenu;//仿QQ侧滑
    @BindString(R.string.main_draghelper)
    String main_draghelper;//仿汽车之家列表菜单
    @BindString(R.string.main_lockpattern)
    String main_lockpattern;//九宫格
    @BindString(R.string.main_behavior)
    String main_behavior;//main_behavior
    @BindString(R.string.main_anima_58city)//属性动画之-仿58同城加载动画
    String main_anima_58city;
    @BindString(R.string.main_listdata_screen)//常见多条目菜单筛选
    String main_listdata_screen;
    @BindString(R.string.main_loading_view)//花束直播加载动画
    String main_loading_view;
    @BindString(R.string.main_message_bubble)//仿QQ消息拖拽效果
    String main_message_bubble;
    @BindString(R.string.main_love_layout)//花束直播点赞效果
    String main_love_layout;
    @BindString(R.string.main_parallax_viewpager)//酷狗音乐引导页
    String main_parallax_viewpager;


    private List<MainDataBean> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initData();

        init();
    }

    private void init() {
        adapter=new MainAdapter(this,mDataList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(0==position){
                    QQStepViewActivity.launch(MainActivity.this);
                }else if(1==position){
                    ColorTrackActivity.launch(MainActivity.this);
                }else if(2==position){
                    HomeworkActivity.launch(MainActivity.this);
                }else if(3==position){
                    RatingBarActivity.launch(MainActivity.this);
                }else if(4==position){
                    LetterBarActivity.launch(MainActivity.this);
                }else if(5==position){
                    TagLayoutActivity.launch(MainActivity.this);
                }else if(6==position){
                    KugoSildMenuActivity.launch(MainActivity.this);
                }else if(7==position){
                    QQSildMenuActivity.launch(MainActivity.this);
                }else if(8==position){
                    DragHelperActivity.launch(MainActivity.this);
                }else if(9==position){
                    LockPatternActivity.launch(MainActivity.this);
                }else if(10==position){
                    BehaviorActivity.launch(MainActivity.this);
                }else if(11==position){
                    City58Activity.launch(MainActivity.this);
                }else if(12==position){
                    ListDataScreenActivity.launch(MainActivity.this);
                }else if(13==position){
                    LoadingViewActivity.launch(MainActivity.this);
                }else if(14==position){
                    MessageBubbleActivity.launch(MainActivity.this);
                }else if(15==position){
                    LoveLayoutActivity.launch(MainActivity.this);
                }else if(16==position){
                    ParallaxViewPagerActivity.launch(MainActivity.this);
                }
            }
        });

    }

    private void initData() {
        mDataList=new ArrayList<MainDataBean>();
        MainDataBean mData1=new MainDataBean();
        mData1.setId(1);
        mData1.setName(main_qqstep);
        mDataList.add(mData1);
        MainDataBean mData2=new MainDataBean();
        mData2.setId(2);
        mData2.setName(main_colortrack);
        mDataList.add(mData2);
        MainDataBean mData3=new MainDataBean();
        mData3.setId(3);
        mData3.setName(main_progressbar);
        mDataList.add(mData3);
        MainDataBean mData4=new MainDataBean();
        mData4.setId(4);
        mData4.setName(main_ratingbar);
        mDataList.add(mData4);
        MainDataBean mData5=new MainDataBean();
        mData5.setId(5);
        mData5.setName(main_lettersidebar);
        mDataList.add(mData5);
        MainDataBean mData6=new MainDataBean();
        mData6.setId(6);
        mData6.setName(main_taglayout);
        mDataList.add(mData6);
        MainDataBean mData7=new MainDataBean();
        mData7.setId(7);
        mData7.setName(main_kugou_slidmenu);
        mDataList.add(mData7);
        MainDataBean mData8=new MainDataBean();
        mData8.setId(8);
        mData8.setName(main_qq_slidmenu);
        mDataList.add(mData8);
        MainDataBean mData9=new MainDataBean();
        mData9.setId(9);
        mData9.setName(main_draghelper);
        mDataList.add(mData9);
        MainDataBean mData10=new MainDataBean();
        mData10.setId(10);
        mData10.setName(main_lockpattern);
        mDataList.add(mData10);
        MainDataBean mData11=new MainDataBean();
        mData11.setId(11);
        mData11.setName(main_behavior);
        mDataList.add(mData11);
        MainDataBean mData12=new MainDataBean();
        mData12.setId(12);
        mData12.setName(main_anima_58city);
        mDataList.add(mData12);
        MainDataBean mData13=new MainDataBean();
        mData13.setId(13);
        mData13.setName(main_listdata_screen);
        mDataList.add(mData13);
        MainDataBean mData14=new MainDataBean();
        mData14.setId(14);
        mData14.setName(main_loading_view);
        mDataList.add(mData14);
        MainDataBean mData15=new MainDataBean();
        mData15.setId(15);
        mData15.setName(main_message_bubble);
        mDataList.add(mData15);
        MainDataBean mData16=new MainDataBean();
        mData16.setId(16);
        mData16.setName(main_love_layout);
        mDataList.add(mData16);
        MainDataBean mData17=new MainDataBean();
        mData17.setId(16);
        mData17.setName(main_parallax_viewpager);
        mDataList.add(mData17);

    }


}
