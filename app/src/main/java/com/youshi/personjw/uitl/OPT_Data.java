package com.youshi.personjw.uitl;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import static java.lang.System.in;

/**
 * Created by dianjie on 2017/1/12.
 */

public class OPT_Data {
    private static File downLoadFile;
    private static File sdCardFile;
    private static OutputStream out;
    private static InputStream in;

    public OPT_Data() {

    }

    //写入缓存文件
    public static int WriteDatas(String filename, Context context,String data) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SD卡不可用！", Toast.LENGTH_SHORT).show();
            return -1;
        }
        int length = 0;
        downLoadFile = Environment.getExternalStorageDirectory();
        File f = new File(downLoadFile + "/Person_QDU/");
        downLoadFile = f;
        if (!f.exists()) {
            f.mkdir();
        }
        sdCardFile = new File(f, filename);
        if (sdCardFile.exists()) {
            sdCardFile.delete();
        }
        try{
            out = new FileOutputStream(sdCardFile);
            byte[] b = data.getBytes();
            out.write(b);
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    //检测缓存文件是否存在
    public static boolean File_Exist(String filename) {
        downLoadFile = Environment.getExternalStorageDirectory();
        File f = new File(downLoadFile + "/Person_QDU/");
        downLoadFile = f;
        if (!f.exists()) {
            f.mkdir();
        }
        sdCardFile = new File(f, filename);
        if (sdCardFile.exists()) {
            return true;
        }
        return false;
    }

    //读取缓存文件
    public static String Read_File(String filename) throws IOException {
        downLoadFile = Environment.getExternalStorageDirectory();
        String ss = "";
        File f = new File(downLoadFile + "/Person_QDU/");
        downLoadFile = f;
        if (!f.exists()) {
            f.mkdir();
        }
        sdCardFile = new File(f, filename);
        if (sdCardFile.exists()) {
            String total = "";
            try {
                in = new FileInputStream(sdCardFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            //输出结果。校验你是否操作成功
            while ((ss = bufferedReader.readLine()) != null) {
                total += ss;
                // System.out.println(ss);
            }
            return total;
        }
        return "";
    }
}

