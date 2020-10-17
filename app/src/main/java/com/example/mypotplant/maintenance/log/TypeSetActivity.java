package com.example.mypotplant.maintenance.log;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mypotplant.BaseActivity;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class TypeSetActivity extends BaseActivity {
    public final  static  String TAG="TypeSetActivity";
    public final static  int MAX_LIST_SIZE=5;
    public  static  void actionStart(Context context){
        Intent intent =new Intent(context, TypeSetActivity.class);
        context.startActivity(intent);
    }
    EditText search;
    TextView cancel;
    private ListPopupWindow mPopup;
    private ArrayList<String> list;
    private ArrayAdapter adapter;
    private  ArrayList<String>  names;
    private Map<String,Character> fnames;
    private  ArrayList<Flower> flowers;
    private  ArrayList<String> alphas;
    private ArrayList<Integer> types;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_set);
        initdata();
        initview();

    }
    private  void initview(){
        search=findViewById(R.id.edit_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(mPopup!=null){
                    mPopup.show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mPopup!=null){
                    adapter.clear();
                    if(flowers!=null) {
                        int len=0;
                        for (Flower flower : flowers) {
                           if(flower.getName().contains(s.toString())){
                               if(len<MAX_LIST_SIZE) {
                                   adapter.add(flower.getName());
                                   len++;
                               }
                           }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    //mPopup.show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initListPopupwindow();
        cancel=findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPopup!=null){
                    mPopup.dismiss();

                }
            }
        });
        initRecyclerView();
    }
    private  void initdata(){
        list=new ArrayList<>();
        alphas=new ArrayList<>();
        for(char item='A';item<='Z';item++){
            alphas.add(item+"");
            Log.d(TAG, "item: "+item);
        }
    }

    private  void initListPopupwindow(){
        mPopup=new ListPopupWindow(this);
        adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        mPopup.setAdapter(adapter);
        mPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopup.setAnchorView(search);
        mPopup.setOnItemClickListener(new  AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopup.dismiss();
                /**
                 * 确定类型选择的逻辑
                 * 设置返回值
                 * 返回类型的名称
                 * 结束当前活动
                 */
                Intent intent=new Intent();
                intent.putExtra("type",(String)adapter.getItem(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    /**
     * 装填适配器
     */
    private  void initRecyclerView(){
        recyclerView=findViewById(R.id.recycler_view);
        flowers=new ArrayList<>();
        fnames=new HashMap<>();
        types=new ArrayList<>();
        names=new ArrayList<>();
        names.add("君子兰");
        names.add("中国兰花");
        names.add("绿萝");
        names.add("向日葵");
        names.add("茉莉花");
        fnames.put("君子兰",'J');
        fnames.put("中国兰花",'Z');
        fnames.put("绿萝",'L');
        fnames.put("向日葵",'X');
        fnames.put("茉莉花",'M');
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
                            flower.setFname(fnames.get(flower.getName()));//首字母赋值
                            if(flowers.size()<names.size()) {
                                flowers.add(flower);
                            }
                            if(flowers.size()==names.size()) {  //这里执行装配工作
                               /*
                                * 将flowers按照首字母排序
                                * 计算出types
                                * 初始化适配器
                                */
                                Collections.sort(flowers);
                                for(String alpha:alphas){
                                    Log.d(TAG, "alpha "+alpha);
                                    types.add(2);
                                    for(Flower t:flowers){
                                       if(t.getFname().toString().equals(alpha)){
                                           types.add(1);
                                       }
                                    }
                                }
                                Log.d(TAG, "types: "+types.size());
                                StringBuilder builder=new StringBuilder();
                                for(int i=0;i<types.size();i++){
                                    builder.append(types.get(i)+" ");
                                }
                                Log.d(TAG, "types: "+builder.toString());
                                TypeAdapter adapter=new TypeAdapter(flowers,alphas,types);
                                recyclerView.setAdapter(adapter);
                                LinearLayoutManager lp=new LinearLayoutManager(TypeSetActivity.this);
                                recyclerView.setLayoutManager(lp);
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

    class  TypeAdapter extends RecyclerView.Adapter{
        private   ArrayList<Flower>  flowers;
        private  ArrayList<String> alphas;
        private ArrayList<Integer> types;
        private  int[] sum; //sum[i]表示为第i个元素实际在flowers或alpha中的坐标
        TypeAdapter(  ArrayList<Flower> flowers,ArrayList<String> alphas,ArrayList<Integer> types){
            this.flowers=flowers;
            this.alphas=alphas;
            this.types=types;
            sum=new int[types.size()+1];
            int a=0,b=0;
            for(int i=0;i<types.size();i++){
                if(types.get(i)==1){
                    sum[i]=a++;
                }else{
                    sum[i]=b++;
                }
            }
            StringBuilder builder=new StringBuilder();
            for(int i=0;i<types.size();i++){
                builder.append(sum[i]+" ");
            }
            Log.d(TAG, "sum: "+builder.toString());
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final RecyclerView.ViewHolder viewHolder;
            View view;
            if(viewType==1){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item,parent,false);
                viewHolder= new TypeHolder(view);
                ((TypeHolder)viewHolder).parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {  //点击布局后弹出对话框完成选择
                        alert("确定", "选择类型为" + flowers.get(sum[viewHolder.getAdapterPosition()]).getName(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**
                                         * 确定类型选择的逻辑
                                         * 设置返回值
                                         * 返回类型的名称
                                         * 结束当前活动
                                         */
                                        Intent intent=new Intent();
                                        intent.putExtra("type",flowers.get(sum[viewHolder.getAdapterPosition()]).getName());
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }
                                });
                    }
                });
                return  viewHolder;
            }else {
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_tv_item,parent,false);
                return  new TvHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position)==1){
                ((TypeHolder)holder).textView.setText(flowers.get(sum[position]).getName());
                Glide.with(TypeSetActivity.this).load(flowers.get(sum[position]).getUrl()).into( ((TypeHolder)holder).imageView);
            }else {
                ((TvHolder)holder).textView.setText(alphas.get(sum[position]));
            }
        }

        @Override
        public int getItemCount() {
            return types.size();
        }

        @Override
        public int getItemViewType(int position) {
            return types.get(position);
        }
        class  TypeHolder extends  RecyclerView.ViewHolder{
           CircleImageView imageView;
           TextView textView;
           LinearLayout parent;
            public TypeHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.image);
                textView=itemView.findViewById(R.id.name);
                parent=itemView.findViewById(R.id.parent);
            }
        }

        class  TvHolder extends  RecyclerView.ViewHolder{
            TextView textView;
            public TvHolder(@NonNull View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.alpha);
            }
        }
    }
    private void alert(String title, String content, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);// 设置标题
        // builder.setIcon(R.drawable.ic_launcher);//设置图标
        builder.setMessage(content);// 为对话框设置内容
        // 为对话框设置取消按钮
        final AlertDialog dialog = builder.create();
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", listener);
        builder.create().show();// 使用show()方法显示对话框
    }
}
