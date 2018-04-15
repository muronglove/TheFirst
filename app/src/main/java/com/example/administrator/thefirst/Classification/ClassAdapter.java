package com.example.administrator.thefirst.Classification;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.administrator.thefirst.R;

import java.util.List;

/**
 * Created by ZZh on 2018/3/15.
 */

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder>{
    private List<Classification> mList;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void clicked(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener=listener;
    }

    public ClassAdapter(List<Classification> list){
        mList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.classification_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Classification classification=mList.get(position);
        holder.textView.setText(classification.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.clicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView;
            textView=itemView.findViewById(R.id.txt_box);
        }
    }
}
