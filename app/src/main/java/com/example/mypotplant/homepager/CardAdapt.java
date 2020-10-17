package com.example.mypotplant.homepager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mypotplant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/2/23
 * <br>类描述：首页卡片的适配器<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class CardAdapt extends RecyclerView.Adapter<CardAdapt.Cardholder> {
    ArrayList<Carditem> mlist;
    Map<Integer,Boolean> thumbs;
    Map<Integer,Boolean> collects;
    Context mContext;
    CardAdapt(ArrayList<Carditem> cardlist, Context context){
        mlist=cardlist;
        mContext=context;
        thumbs=new HashMap<>();
        collects=new HashMap<>();
    }
    @NonNull
    @Override
    public Cardholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Cardholder mHolder=new Cardholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepager_recyclerview_item,parent,false));
        mHolder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=mHolder.getAdapterPosition();
                CardActivity.actionStart(mContext,mlist.get(position).getImageViewID());
            }
        });
        mHolder.thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thumbs.get(mHolder.getAdapterPosition())==null||!thumbs.get(mHolder.getAdapterPosition())){
                     thumbs.put(mHolder.getAdapterPosition(),true);
                     turnThumb(mHolder.thumb,mHolder.getAdapterPosition());
                }else {
                    thumbs.put(mHolder.getAdapterPosition(),false);
                    turnThumb(mHolder.thumb,mHolder.getAdapterPosition());
                }
            }
        });
        mHolder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collects.get(mHolder.getAdapterPosition())==null||!collects.get(mHolder.getAdapterPosition())){
                    collects.put(mHolder.getAdapterPosition(),true);
                  turnCollect(mHolder.collect,mHolder.getAdapterPosition());
                }else {
                    collects.put(mHolder.getAdapterPosition(),false);
                    turnCollect(mHolder.collect,mHolder.getAdapterPosition());
                }
            }
        });
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Cardholder holder, int position) {
        holder.mview.setImageResource(mlist.get(position).getImageViewID());
    }
    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class Cardholder extends RecyclerView.ViewHolder {
        public ImageView mview;
        public ImageView thumb;
        public ImageView collect;

        public Cardholder(@NonNull View itemView) {
            super(itemView);
            mview = itemView.findViewById(R.id.ima_view_carditem);
            thumb = itemView.findViewById(R.id.ima_view_item_thumb);
            collect=itemView.findViewById(R.id.ima_view_item_collect);
        }
    }
    private void turnThumb(ImageView view,int i){
        if(thumbs.get(i)==null){
            return;
        }
        if(thumbs.get(i)){  //已经点赞 熄灭点赞
            view.setBackgroundResource(R.mipmap.thumb_red);
        }else { //点亮
            view.setBackgroundResource(R.mipmap.thumb_black);
        }
    }
    private void turnCollect(ImageView view,int i){
        if(collects.get(i)==null){
            return;
        }
        if(collects.get(i)){  //已经点赞 熄灭点赞
            view.setBackgroundResource(R.mipmap.collect_yellow);
        }else { //点亮
            view.setBackgroundResource(R.mipmap.collect_black);
        }
    }
}
