package com.customview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.customview.R;

/**
 * des:
 * author: Yang Weisi
 * date 2018/7/13 下午3:36
 */
public class ItemFragment extends Fragment {

    public static ItemFragment newInstance(String item){
        ItemFragment itemFragment=new ItemFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title",item);
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_colortrack_fragment_item,null);
        TextView tv=view.findViewById(R.id.tvItem);
        Bundle bundle=getArguments();
        tv.setText(bundle.getString("title"));
        return view;
    }
}
