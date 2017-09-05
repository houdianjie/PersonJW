package com.youshi.personjw.Service;

import android.content.Context;

import java.io.FileOutputStream;

/**
 * Created by dianjie on 2016/7/26.
 */
public class FileService {
    private Context context;

        public FileService(Context context){
           this.context = context;
       }
   /**
     * 把用户名和密码保存到手机ROM
     * @param password 输入要保存的密码
     * @param username 要保存的用户名
     * @param filename 保存到哪个文件
     * @return
     */
       public boolean saveToRom(String password,String username,String filename) throws Exception{
           //以私有的方式打开一个文件
           FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
           String result = username+":"+password;
           fos.write(result.getBytes());
           fos.flush();
           fos.close();
           return true;
       }
}
