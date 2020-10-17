package com.example.mypotplant.person.login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.person.register.MobActivity;

import androidx.appcompat.app.AlertDialog;

public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {

    public  static void actionStart(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
    private TextView mBtnLogin;   /** 登录按钮*/
    private View progress;        /** 进度部分布局*/
    private View mInputLayout;    /** 输入部分布局*/
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;  /** 用户名框和密码框*/
    private EditText mEditUsername;
    private EditText mEditPassword;
    private TextView sign_up;
    private LoginPresenter mPresenter=new LoginPresenter(this,this);
    private CheckBox save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();    //实例化控件
        mPresenter.recoverPassword(mEditUsername,mEditPassword,save);
    }

    @Override
    public void initview() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        mEditUsername=(EditText)findViewById(R.id.edit_username);
        mEditPassword=(EditText)findViewById(R.id.edit_password);
        sign_up=(TextView)findViewById(R.id.register_bt);
        mBtnLogin.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        save=findViewById(R.id.save);
    }

    /**
     *  完成登录后的动画显示
     */
    @Override
    public void showLoginAnimation() {
        // 计算出控件的高与宽
        mWidth = mBtnLogin.getMeasuredWidth();
        mHeight = mBtnLogin.getMeasuredHeight();
        // 隐藏输入框
        mName.setVisibility(View.INVISIBLE);
        mPsw.setVisibility(View.INVISIBLE);
        inputAnimator(mInputLayout, mWidth, mHeight);
    }


    /*
     * 登录失败弹出对话框
     */
    @Override
    public void showAlertDialog(String mesg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("登录失败");
        dialog.setMessage(mesg);
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public void showToast(String mesg) {
        Toast.makeText(this,mesg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void initLogin() {
            progress.setVisibility(View.INVISIBLE);
            mInputLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()) {
           case R.id.main_btn_login:
           mPresenter.doLogin(mEditUsername.getText().toString(), mEditPassword.getText().toString(),save.isChecked());
           break;
           case R.id.register_bt:
               //更换为短信验证
//               RegisterActivity.actionStart(this);
               MobActivity.actionStart(this);
               break;
       }
    }

    /**
     * 输入框的动画效果
     *
     * @param view 控件
     * @param w 宽
     * @param h 高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }
}

