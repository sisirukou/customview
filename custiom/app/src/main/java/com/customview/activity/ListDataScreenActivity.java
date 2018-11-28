package com.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.customview.R;
import com.customview.adapter.ListScreenMenuAdapter;
import com.customview.view.ListDataScreen;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/26 10:40
 */
public class ListDataScreenActivity extends AppCompatActivity {

    @BindView(R.id.listDataScreen)
    ListDataScreen listDataScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listdata_screen);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        listDataScreen.setAdapter(new ListScreenMenuAdapter(this));
    }


    public static void launch(Context context){
        Intent intent=new Intent(context, ListDataScreenActivity.class);
        context.startActivity(intent);
    }
}
