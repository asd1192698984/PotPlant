package com.example.mypotplant.homepager.flowerRank;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.R;
import com.example.mypotplant.homepager.flower.Flower;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/7/26
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class RankFragment extends LazyBaseFragment {
    public  final static  String TAG="RankFragment";
    public static int WAIT_TIME=1000;  //最多等待五秒
    RecyclerView recyclerView;
    ArrayList<Flower> flowers;
    RankAdapt rankAdapt;
    Bundle data;
    private  ArrayList<String>  names;
    @Nullable
    @Override
    public  View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rank,container,false);
    }

    private  RankFragment(){

    }
    public static RankFragment newInstance(){
        RankFragment fragment=new RankFragment();
        Bundle bundle=new Bundle();
        fragment.setArguments(bundle);
        return  fragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    @Override
    public View initView() {
        return null;
    }

    @Override
    public void initData() {
        data=getArguments();
//         Search_Activity.getList(getActivity(), new Search_Activity.LoadFlowerListener() {
//            @Override
//            public void afterload(ArrayList<Flower> list) {
//                flowers=list;
//                Log.d(TAG, "afterload: "+list.size());
//                fillRecyclerview();
//            }
//        });
        initRecyclerView();
    }

    @Override
    protected void lazyLoad() {

    }

    class RankAdapt extends RecyclerView.Adapter<RankAdapt.RankHolder>{
        Context context;
        ArrayList<Flower> flowers;
        RankAdapt(Context context, ArrayList<Flower> list){
            this.context=context;
            this.flowers=list;
        }
        @NonNull
        @Override
        public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item,parent,false);
            return new RankHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RankHolder holder, int position) {
           Flower flower= flowers.get(position);
          holder.name.setText(flower.getName());
//          holder.image.setImageResource(flower.getIconID());
            Glide.with(getActivity()).load(flowers.get(position).getUrl()).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return flowers.size();
        }


        class RankHolder extends RecyclerView.ViewHolder{
            ImageView image;
            TextView name;
            RankHolder(View itemview){
                super(itemview);
                image=itemview.findViewById(R.id.image);
                name=itemview.findViewById(R.id.name);
            }
        }

    }
    private void fillRecyclerview(){
       recyclerView=getView().findViewById(R.id.recyclerview_rank);
       if(flowers!=null){
           rankAdapt=new RankAdapt(getActivity(),flowers);
       }
       recyclerView.setAdapter(rankAdapt);
       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
       recyclerView.setLayoutManager(linearLayoutManager);
    }
    /**
     * 装填适配器
     */
    private  void initRecyclerView(){
        flowers=new ArrayList<>();
        names=new ArrayList<>();
        names.add("君子兰");
        names.add("中国兰花");
        names.add("绿萝");
        names.add("向日葵");
        names.add("茉莉花");
        OkHttpHelper helper=OkHttpHelper.getinstance();
        for (int i = 0; i < names.size(); i++) {
            Map data = new HashMap();
            data.put("name", names.get(i));
            helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_FLOWER_GETALL, new Gson().toJson(data), new BaseCallback<String>() {
                @Override
                public void onRequestBefore() {
                    Log.d(TAG, "onRequestBefore: ");
                }

                @Override
                public void onFailure(Request request, Exception e) {
                    Log.d(TAG, "onFailure: ");
                }

                @Override
                public void onSuccess(Response response, String s) {
                    Flower flower = new Flower();
                    Log.d(TAG, "onSuccess: " + s);
                    JSONObject o = null;
                    try {
                        o = new JSONObject(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        flower.setLight_condition(o.getString("light_condition"));
                        flower.setLight_condition(o.getString("light_request"));
                        flower.setName(o.getString("name"));
                        flower.setNo_suit_temp(o.getString("no_suit_temp"));
                        flower.setOpening_word(o.getString("opening_word"));
                        flower.setPh_value(o.getString("ph_value"));
                        flower.setRecom_fertilizer(o.getString("recom_fertilizer"));
                        flower.setRecom_soil(o.getString("recom_soil"));
                        flower.setSuit_humidity(o.getString("suit_humidity"));
                        flower.setSuit_temp(o.getString("suit_temp"));
                        flower.setUrl(o.getString("url"));
                        if(flowers.size()<names.size()) {
                            flowers.add(flower);
                        }
                        if(flowers.size()==names.size()) {  //这里执行装配工作
                            fillRecyclerview();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.d(TAG, "onError: " + errorCode);
                }
            });
        }
    }
}
