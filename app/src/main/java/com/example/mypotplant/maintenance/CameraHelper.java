package com.example.mypotplant.maintenance;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mypotplant.NameHelper;
import com.example.mypotplant.person.login.LoginModel;

import java.io.File;
import java.io.IOException;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

/**
 * Created by MXL on 2020/3/11
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public  class CameraHelper {

   private  static CameraHelper mHelper;
   public  static  String filedirs="photo";
    public static  String logdirs="logdir";
    private   static String testUsername="123";
    private  CameraHelper(){

    }
    public  static final int TAKE_PHOTO=1;
    public  static  final  int CHOOSE_PHOTO=2;

    /**
     * 拍照添加照片
     * @param fragment
     * @param file
     * @return 保存文件
     */
   public  File  do_take_photo(Fragment fragment, File file){
      Uri imageUri;
      try {
          if (file.exists()) {
              file.delete();
          }
          file.createNewFile();

      }catch (IOException e){
          e.printStackTrace();
      }
     if(Build.VERSION.SDK_INT>=24){
         imageUri= FileProvider.getUriForFile(fragment.getActivity(),"com.example.mypotplant.maintenance.fileprovider",file);
     } else{
         imageUri=Uri.fromFile(file);
     }
     //启动相机
      Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
     intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
       //可裁剪
       intent.putExtra("crop", true);
       //按比例
       intent.putExtra("scale", true);
       intent.putExtra("outputX", 100);
       intent.putExtra("outputY", 100);
     fragment.startActivityForResult(intent,TAKE_PHOTO);
     return file;
  }

    public  File getPhotofilename(Context context, String filename,String parent){
        String path;
        if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            /*
             * 文件路径从用户名修改为手机号
             * Add by MXL 2020/7/15
             */
        path= Environment.getExternalStorageDirectory() + File.separator + parent + File.separator+ LoginModel.getUser().getUser_phone_number()+File.separator;
        }else {
            /*
             * 文件路径从用户名修改为手机号
             * Add by MXL 2020/7/15
             */
            path=parent + File.separator+LoginModel.getUser().getUser_phone_number()+File.separator;
        }
        File file=new File(path);
       if(!file.exists()){
           file.mkdirs();
       }
        File file2=new File(file.toString(),filename+".jpg");
       //为文件加上时间后缀避免重复
       File file3= NameHelper.createFileWithCurDate(file2);
        Log.d("CameraHelper", "getPhotofilename: "+file3);
       return file3;
    }
    public  static  CameraHelper getInstance(){
       if(mHelper!=null){
           return  mHelper;
       }else {
           mHelper=new CameraHelper();
           return  mHelper;
       }
    }

    /**
     * 删除照片
     * @param plant
     * @return
     */
    public  static boolean deletePhoto(Plant plant){
        String path;
        if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            /*
             * 文件路径从用户名修改为手机号
             * Add by MXL 2020/7/15
             */
            path= Environment.getExternalStorageDirectory() + File.separator + filedirs + File.separator+ LoginModel.getUser().getUser_phone_number()+File.separator;
        }else {
            path=filedirs + File.separator+LoginModel.getUser().getUser_phone_number()+File.separator;
        }
       File file=new File(path,plant.getFilename());
       if(!file.exists()){
          return  false;
       }
       else {
           file.delete();
           return  true;
       }
   };

    /**
     * 启动相册
     */
    public void  openAlbum(Activity activity){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        activity.startActivityForResult(intent,CHOOSE_PHOTO);
    }

    /**
     *  4.4以上版本的相册处理方法
     * @param data
     * @return  图片文件路径
     */
    public  String handleImageOnKitKat(Context context,Intent data){
      String imagepath=null;
      Uri uri=data.getData();
      if(DocumentsContract.isDocumentUri(context,uri)){
          //document类型的uri，则通过document id处理
          String docID=DocumentsContract.getDocumentId(uri);
          if("com.android.providers.media.documents".equals(uri.getAuthority())){
              String id=docID.split(":")[1];//解析出数字格式ID
              String selection=MediaStore.Images.Media._ID+"="+id;
              imagepath=getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
          }else  if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
              Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docID));
              imagepath=getImagePath(context,contentUri,null);
          }
      }else  if("content".equalsIgnoreCase(uri.getScheme())){
          //content类型的uri 普通方式处理
            imagepath=getImagePath(context,uri,null);
      }else if("file".equalsIgnoreCase(uri.getScheme())){
         imagepath=uri.getPath();
      }
      return  imagepath;
    }

    /**
     * 4.4以下版本的相册处理
     * @param context
     * @param data
     * @return
     */
    public String handleImageBeforeKitKat(Context context,Intent data){
        Uri uri=data.getData();
        String imagepath=getImagePath(context,uri,null);
        return  imagepath;
    }
    private  String getImagePath(Context context,Uri uri,String selection){
        String path=null;
        Cursor cursor=context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    interface  PhotoListener{
        void afterPhoto();
    }
}
