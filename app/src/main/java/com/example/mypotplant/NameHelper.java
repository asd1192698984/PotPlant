package com.example.mypotplant;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by MXL on 2020/1/14
 * <br>类描述：文件命名帮助类<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class NameHelper {
    public static String[] getFileInfo(File from){
        String fileName=from.getName();
        int index = fileName.lastIndexOf(".");
        String toPrefix="";
        String toSuffix="";
        if(index==-1){
            toPrefix=fileName;
        }else{
            toPrefix=fileName.substring(0,index);
            toSuffix=fileName.substring(index,fileName.length());
        }
        return new String[]{toPrefix,toSuffix};
    }

    public static String generateSuffix() {
        // 获得当前时间
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        // 转换为字符串
        String formatDate = format.format(new Date());
        // 随机生成文件编号
        int random = new Random().nextInt(10000);
        return new StringBuffer().append(formatDate).append(
                random).toString();
    }

    /**
     * <br>给文件加后缀避免重复<br/>
     *
     * @param from 文件类
     * @return 加了时间后缀的文件类
     */
    public static File createFileWithCurDate(File from){
        String[] fileInfo = getFileInfo(from);
        String toPrefix=fileInfo[0]+generateSuffix();
        String toSuffix=fileInfo[1];
        return new File(from.getParent(),toPrefix+toSuffix);
    }
}
