package com.example.mypotplant.View;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by MXL on 2020/7/19
 * <br>类描述：该类实现EditText右侧图标改变输入框编辑效果 <br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class ModifyEditText extends AppCompatEditText {

    private Drawable dRight;
    private Rect rBounds;
    private  boolean isEdit=false;

    public ModifyEditText(Context context) {
        super(context);
    }

    public ModifyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ModifyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEditTextEnable(this,isEdit);
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if(right!=null){
            dRight=right;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP&&dRight!=null){
            rBounds=dRight.getBounds();
            final int x=(int)event.getX();
            final int y=(int)event.getY();
            if(x>(this.getRight()-rBounds.width())&&x<(this.getRight()-this.getPaddingRight())&&y>=this.getPaddingTop()&&y<(this.getHeight()-this.getPaddingBottom())){
               if(isEdit){ //设置不可编辑
                   setEditTextEnable(this,false);
                   isEdit=false;
                 //  Toast.makeText(getContext(),"不可编辑",Toast.LENGTH_SHORT).show();
               } else { //设置可编辑
                   setEditTextEnable(this,true);
                 //  Toast.makeText(getContext(),"可编辑",Toast.LENGTH_SHORT).show();
                   isEdit=true;
               }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        dRight=null;
        rBounds=null;
        super.finalize();
    }

    /**
     * 设置编辑状态
     * @param editText
     * @param mode
     */
    private  void setEditTextEnable(EditText editText, boolean mode){
        editText.setFocusable(mode);
        editText.setFocusableInTouchMode(mode);
        editText.setLongClickable(mode);
        editText.setInputType(mode? InputType.TYPE_CLASS_TEXT:InputType.TYPE_NULL);
    };
}
