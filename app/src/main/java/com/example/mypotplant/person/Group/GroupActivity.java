package com.example.mypotplant.person.Group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.person.ModifyInfoActivity;
import com.example.mypotplant.person.register.User;
import com.example.mypotplant.utils.NetWorkUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupActivity extends BaseActivity {

  public final  static  String TAG="GroupActivity";
    public  static  void actionStart(Context context){
        Intent intent =new Intent(context,GroupActivity.class);
        context.startActivity(intent);
    }
    EditText search;
    private ListPopupWindow mPopup;
    private ArrayList<String> list;
    private  ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        initdata();
        initview();
    }
    private  void initview(){
        search=findViewById(R.id.search_friend);
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
                   adapter.notifyDataSetChanged();
                   adapter.add("找人: "+s);
                   //mPopup.show();
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initListPopupwindow();
    }
    private  void initdata(){
       list=new ArrayList<>();
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
               getinfo((search.getText().toString().trim()));
           }
       });
    }

    /**
     *  格式不进行验证
     *  要求联网
     *  搜索不到，提醒无此人
     * @param number 手机号
     */
    private  void getinfo(final String number){
        if(NetWorkUtils.networkAvailable(this)) {
            OkHttpHelper helper = OkHttpHelper.getinstance();
            Map data = new HashMap();
            data.put("user_id", number);
            Log.d(TAG, "getinfo: "+new Gson().toJson(data));
            helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_GET_INFO, new Gson().toJson(data), new BaseCallback<String>() {
                @Override
                public void onRequestBefore() {

                }

                @Override
                public void onFailure(Request request, Exception e) {

                }

                @Override
                public void onSuccess(Response response, String s) {
                    Log.d(TAG, "onSuccess: "+s);
                    if(s!=null&&!s.equals("")){
                        try {
                            Log.d(TAG, "onSuccess: "+1111);
                            JSONObject jsonObject=new JSONObject(s);
                            User user=new User();
                            user.setUser_phone_number(number);
                            user.setUser_name(jsonObject.getString("user_name"));
                            user.setAddress(jsonObject.getString("user_address").equals("")?User.DEFAULT_ADDRESS:jsonObject.getString("user_address"));
                            user.setSign(jsonObject.getString("user_signature").equals("")?User.DEFAULT_SING:jsonObject.getString("user_address"));
                            user.setUrl(jsonObject.getString("user_pic"));
                            ModifyInfoActivity.actionStart(GroupActivity.this,user,ModifyInfoActivity.MODE_FIND);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        Toast.makeText(GroupActivity.this,"没有找到该用户",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.d(TAG, "onError: "+errorCode);
                }
            });
        }else {
            Toast.makeText(GroupActivity.this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }
}
