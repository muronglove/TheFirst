package com.example.administrator.thefirst;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/3/18.
 */

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder> {

    private Context mContext;

    private List<Clothes> mClothesList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView clothesImage;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            clothesImage = (ImageView) view.findViewById(R.id.clothes_image);
        }
    }

    public ClothesAdapter(List<Clothes> clothesList){
        mClothesList = clothesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.picture_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int position = holder.getAdapterPosition();
                Clothes clothes = mClothesList.get(position);
                Intent intent = new Intent(mContext,IntroductionActivity.class);
                intent.putExtra(IntroductionActivity.CLOTHES_IMAGE_ID,clothes.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Clothes clothes = mClothesList.get(position);
        Glide.with(mContext).load(clothes.getImageId()).into(holder.clothesImage);
    }

    @Override
    public int getItemCount(){
        return mClothesList.size();
    }
}
