package com.example.mypotplant.Exception;

import android.content.Context;

import java.util.regex.Pattern;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述：对错误码进行解析的工具类<br/>
 * @version 1.0
 * @since 1.0
 */
public class Converter {
    /**
     * <br>引用类：{@link }<br/>
     *
     * @param context 上下文
     * @param errorCode 错误码
     * @return 错误语句
     */
    public static CharSequence getErrorString(Context context, String errorCode) {
        //判断传进来的错误码是否全为数字
        Pattern pattern = Pattern.compile("[0-9]*");
        boolean isNum = pattern.matcher(errorCode.trim()).matches();
        int resId;
        if (isNum) { //全为数字，则返回错误码
            return "errorCode: " + errorCode;
        } else { //否则对错误码进行转换
            resId = context.getResources().getIdentifier(errorCode, "error_strings", context.getPackageName());
            if (resId == 0)
                return errorCode;
        }
        return context.getResources().getText(resId);
    }
}
