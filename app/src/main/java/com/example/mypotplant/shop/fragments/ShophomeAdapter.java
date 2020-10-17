package com.example.mypotplant.shop.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypotplant.R;
import com.example.mypotplant.shop.Bean.CommodityMsg;
import com.example.mypotplant.utils.AppUtils;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/7/30
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class ShophomeAdapter extends RecyclerView.Adapter<ShophomeAdapter.ViewHolder> {

    List<CommodityMsg> commodities;
    Context context;
    ShophomeAdapter(Context context, List commodities){
        this.context=context;
        this.commodities=commodities;
        Collections.shuffle(commodities);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shophomeitem, parent, false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTaoBao(context,commodities.get(viewHolder.getAdapterPosition()).getLinkUrl());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//       holder.imageView.setImageBitmap(ImageHelper.getPicFromBytes(commodities.get(position).getImg(),new BitmapFactory.Options()));
        holder.imageView.setImageResource(commodities.get(position).getImgID());
        holder.name.setText(commodities.get(position).getName());
        holder.price.setText("￥"+commodities.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return commodities.size();
    }

        class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
        }
    }
    public void  addCommdity(CommodityMsg commodity){
        commodities.add(commodity);
        notifyItemChanged(commodities.size()-1);
    }
    //跳转淘宝界面
    private void toTaoBao(Context context,String tUri){
        if(AppUtils.isPkgInstalled(context,"com.taobao.taobao")){
            Intent intent=new Intent( );
            intent.setAction("Android.intent.action.VIEW");
            Uri uri= Uri.parse(tUri);
            intent.setData(uri);
            intent.setClassName("com.taobao.taobao","com.taobao.tao.detail.activity.DetailActivity");
            context.startActivity(intent);
        }else {
            //SSl证书获取不了
//          TaobaoWebActivity.actionStart(context,tUri);

        }
    }

}