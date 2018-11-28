package com.customview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.customview.R;
import com.customview.bean.MainDataBean;
import com.customview.imp.OnItemClickListener;

import java.util.List;

/**
 * des:
 * author: Yang Weisi
 * date 2018/11/22 21:48
 */
public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private LayoutInflater inflater;
    private Context mContext;
    private List<MainDataBean> mDataList;

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MainAdapter(Context mContext, List<MainDataBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        inflater=LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_main_item,parent,false);
        MainViewHolder viewHolder=new MainViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        holder.textView.setText(mDataList.get(position).getName());
        if (mOnItemClickListener != null) {
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}

class MainViewHolder extends RecyclerView.ViewHolder{

    TextView textView;

    public MainViewHolder(View itemView) {
        super(itemView);
        textView=(TextView)itemView.findViewById(R.id.textView);
    }

}

