package com.example.mypotplant.Judgehelps;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述：该类对字符串检测进行基本操作<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class JudgeHelpUtil {
    /**
     * 判断是否含有特殊字符
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 判断字符串是否包含汉字
     * @param str 待检测字符串
     * @return true 包含 false 不包含
     */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    /**
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
