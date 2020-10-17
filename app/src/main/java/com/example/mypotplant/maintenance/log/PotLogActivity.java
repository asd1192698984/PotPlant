package com.example.mypotplant.maintenance.log;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.maintenance.MaintenanceModel;
import com.example.mypotplant.maintenance.Plant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 根据plant主键生成养护日志
 */
public class PotLogActivity extends BaseActivity implements IPotLogView {

    private  IPotLogPresenter presenter; //控制类
    RecyclerView recyclerView;
    ImageButton setting;
    ImageButton addpotlog;
    TextView type;
    TextView change;
    View dialogView;
    Button submit;
    Spinner spinner;
    View potSelectView;
    Button[] pots;
    final static  String TAG="PotLogActivity";
    /*
     * 结果数组
     * 0存放Type code
     * 1存放养护模式
     *
     */
    Object[] setting_res=new Object[]{"无",0,new Object()};
    DialogListener dialogListener;
    public final static int TYPESET=1;
    public final static int EDIT_POTLOG=2;
    public static void  actionStart(Context context, Plant plant){
        Intent intent=new Intent(context,PotLogActivity.class);
        intent.putExtra("plant",plant);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot_log);
        initdata();
        initview();
    }

    /**
     * 初始化控件
     */
    private  void initview(){
        setting=findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.domodifySetting();
            }
        });
        addpotlog=findViewById(R.id.addpotlog);
        addpotlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.editPotLog();
            }
        });
        presenter.initRecycleview();
    }

    /**
     * 初始化数据
     */
    private  void initdata(){
        presenter=new PotLogPresenter(this,this,(Plant)getIntent().getSerializableExtra("plant"));
    }
    @Override
    public void fillRecycleview(PotLogAdapt adapt) {
         recyclerView=findViewById(R.id.recyclerview_potlog);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapt);
    }
//    public String getFilename() {
//        return plant.getFilename();
//    }

    @Override
    public void modifySetting(final Plant plant) {
        initdialog();
        //恢复原来设置
        if(plant!=null) {
            type.setText(plant.getType());
            spinner.setSelection(plant.getIs_intelligent());
        }
        //构造对话框
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog dialog= builder.create();
        dialog.show();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启植株搜索界面进行类型选择
              presenter.dotypeset();
            }
        });
        spinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 判断结果 如果结果为自动养护 position=1 进入选择盆栽栏位
                 * 首先初始化栏位状态
                 */
                final AlertDialog.Builder builder2=new AlertDialog.Builder(PotLogActivity.this);
                builder2.setView(potSelectView);//布局
                builder2.setCancelable(true); //不可取消
                final AlertDialog dialog2= builder2.create();
                //初始化 盆栽状态 设置监听
                for(int i=0;i<pots.length;i++){
                   if(MaintenanceModel.getMyPot().getPot(i)==MaintenanceModel.myPot.POT_AVAILABLE){
                      cancelSelectPot(i);
                   }else {
                       selectPot(i);
                   }
                   pots[i].setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           int potpos=0;  //确定控件位置
                           switch (v.getId()){
                               case R.id.pot1:
                                   potpos=0;
                                   break;
                               case R.id.pot2:
                                   potpos=1;
                                   break;
                               case R.id.pot3:
                                   potpos=2;
                                   break;
                               case R.id.pot4:
                                   potpos=3;
                                   break;
                           }
                           if(MaintenanceModel.getMyPot().getPot(potpos)==MaintenanceModel.myPot.POT_AVAILABLE) { //判断位置可用
                               int selectpot=-1;
                               if ((selectpot=MaintenanceModel.getMyPot().iscontain(plant.getFilename()))==-1){  //没有包含植物
                                   selectPot(potpos);  //ui显示
                                   MaintenanceModel.getMyPot().setPlant(potpos, plant);//改变花盆数据
                                   dialog2.dismiss();  //关闭弹窗
                                }else {  //弹出对话框提示是否需要更换植物位置
                                   final int finalPotpos = potpos;
                                   if(finalPotpos!=selectpot) {  //选择的位置跟植株当前所在位置不一致 则替换
                                       alert("confirm", "更换植株位置", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               //执行替换操作
                                               MaintenanceModel.getMyPot().replace(plant.getFilename(), finalPotpos);
                                           }

                                       });
                                   }else { //否则提示删除
                                       alert("confirm", "确认移除植株", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               //执行替换操作
                                               MaintenanceModel.getMyPot().deletePlant(finalPotpos);
                                           }
                                       });
                                   }
                               }
                           }else {
                               int selectpot=-1;
                               if ((selectpot=MaintenanceModel.getMyPot().iscontain(plant.getFilename()))==-1){  //没有包含植物
                                   selectPot(potpos);  //ui显示
                                   MaintenanceModel.getMyPot().setPlant(potpos, plant);//改变花盆数据
                                   dialog2.dismiss();  //关闭弹窗
                               }else {
                                   Toast.makeText(PotLogActivity.this,"该位置被占用",Toast.LENGTH_LONG).show();
                               }
                           }
                       }
                   });
                }
                Log.d(TAG, "onItemSelected: "+setting_res[1]+" "+position);
                if(((Integer)setting_res[1]!=1&&position==1)||((Integer)setting_res[1]==1&&position==1))  //如果之前不为自动养护 用户选择了自动养护 则弹出弹窗
                dialog2.show();
                //储存结果
                setting_res[1]=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭对话框
                //调用接口
                 dialog.dismiss();
                 dialogListener.afterSetting(setting_res);
            }
        });
        return;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void alert(String title,String content,DialogInterface.OnClickListener listener) {
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

    interface  DialogListener{
        void afterSetting(Object[] res);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TYPESET:
                if(resultCode== RESULT_OK){  //执行植株类型选择
                    //获取Intent中type
                    //然后
//                    Flower flower=(Flower) data.getSerializableExtra("flower");
//                    setting_res[0]=flower.getCode();
//                    type.setText(flower.getName());
                    String type=data.getStringExtra("type");
                    setting_res[0]=type;
                    this.type.setText(type);
                }
                break;
            case EDIT_POTLOG:
                if(resultCode== RESULT_OK){
                   PotLog potLog=(PotLog) data.getSerializableExtra("potlog");
                   presenter.addPotLog(potLog);  //添加日志
                }
                break;
             default:
                 break;
        }
    }
    private void selectPot(int position){
       pots[position].setBackgroundColor(getResources().getColor(R.color.gray));
       pots[position].setText("已选");
    }
    private void cancelSelectPot(int position){
//        pots[position].setBackgroundColor(getResources().getColor(R.color.mediumslateblue));
        pots[position].setBackgroundResource(R.drawable.radius_bt);
        pots[position].setText("选择");
    }
   private void initdialog(){
       dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_setting,null);
       type=dialogView.findViewById(R.id.tv_type);
       change=dialogView.findViewById(R.id.tv_change);
       submit=dialogView.findViewById(R.id.submit);
       spinner=dialogView.findViewById(R.id.mode);
       potSelectView=LayoutInflater.from(this).inflate(R.layout.pot_select,null);
       pots=new Button[4];
       pots[0]=potSelectView.findViewById(R.id.pot1);
       pots[1]=potSelectView.findViewById(R.id.pot2);
       pots[2]=potSelectView.findViewById(R.id.pot3);
       pots[3]=potSelectView.findViewById(R.id.pot4);
   }

}
