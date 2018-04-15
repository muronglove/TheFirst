package com.example.administrator.thefirst.Collection;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.thefirst.R;

import java.util.List;

/**
 * Created by ZZh on 2018/3/12.
 */

public class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.ViewHolder>{
    private List<Advice> mList;
    private List<Bitmap> bitmaps;

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mItemClickListener=listener;
    }

    public AdviceAdapter(List<Advice> list,List<Bitmap> bitmaps){
        mList=list;
        this.bitmaps=bitmaps;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.advice_item,parent,false);


//        final ViewHolder holder=new ViewHolder(view);
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(parent.getContext(),"Chicked",Toast.LENGTH_LONG).show();
//
//                mItemClickListener.onItemClickListener();
//
//            }
//        });


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Advice advice=mList.get(position);
        Bitmap bitmap=bitmaps.get(position);
        holder.title.setText(advice.getCaption());
        holder.time.setText(advice.getTime());
        holder.detail.setText(advice.getDetail());
        holder.tag.setText(advice.getTag());
        holder.pic.setImageBitmap(bitmap);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        TextView detail;
        TextView tag;
        ImageView pic;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView;
            title=itemView.findViewById(R.id.caption);
            time=itemView.findViewById(R.id.time);
            detail=itemView.findViewById(R.id.detail);
            tag=itemView.findViewById(R.id.tag);
            pic=itemView.findViewById(R.id.pic);
        }
    }
}
