package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youshi.personjw.R;
import com.youshi.personjw.uitl.GetHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by dianjie on 2016/7/26.
 */
public class Person_Info extends Activity {
    private GetHttp getHttp_info;
    private ImageView Info_img;
    URL myFileUrl = null;
    Bitmap bitmap = null;
    private String COOK,HOST;
    private Person_MyHandler myHandler;
    private String []s1;
    private String []s2;
    private String []s3;
    private String Http_Img;
    private TextView []info_tv;
    private TextView []info_c_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info);
        init();
        Intent intent = Person_Info.this.getIntent();
        Bundle bundle = intent.getExtras();
        COOK = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
    }
    private void init(){
        getHttp_info = GetHttp.getGetHttp();
        Info_img = (ImageView) findViewById(R.id.person_info_img1);
        COOK = null;
        myHandler = new Person_MyHandler();
        subinit();
        s1 = null;
        s2 = null;
        s3 = null;
        Http_Img = null;
    }
    private void subinit(){
        info_tv = new TextView[14];
        info_c_tv = new TextView[5];
        info_tv[0] = (TextView) findViewById(R.id.info_tv1); info_tv[1] = (TextView) findViewById(R.id.info_tv2); info_tv[2] = (TextView) findViewById(R.id.info_tv3);
        info_tv[3] = (TextView) findViewById(R.id.info_tv4); info_tv[4] = (TextView) findViewById(R.id.info_tv5); info_tv[5] = (TextView) findViewById(R.id.info_tv6);
        info_tv[6] = (TextView) findViewById(R.id.info_tv7); info_tv[7] = (TextView) findViewById(R.id.info_tv8); info_tv[8] = (TextView) findViewById(R.id.info_tv9);
        info_tv[9] = (TextView) findViewById(R.id.info_tv10); info_tv[10] = (TextView) findViewById(R.id.info_tv11); info_tv[11] = (TextView) findViewById(R.id.info_tv12);
        info_tv[12] = (TextView) findViewById(R.id.info_tv13); info_tv[13] = (TextView) findViewById(R.id.info_tv14);
        info_c_tv[0] = (TextView) findViewById(R.id.info_c_tv1);  info_c_tv[1] = (TextView) findViewById(R.id.info_c_tv2);  info_c_tv[2] = (TextView) findViewById(R.id.info_c_tv3);
        info_c_tv[3] = (TextView) findViewById(R.id.info_c_tv4);  info_c_tv[4] = (TextView) findViewById(R.id.info_c_tv5);
    }
    public void person_info_click(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null,temp = null,temp2 = null;
                String http = "http://"+HOST+"/academic/showPersonalInfo.do";
                result = getHttp_info.GetInfo(HOST,http,COOK);
                if(result != null){
                    temp = parsehtml(result);
                    temp2 = parsehtml_img(result);
                }
                Message msg1 = new Message();
                msg1.what = 120;
                Bundle b = new Bundle();
                b.putString("course_temp",temp);
                b.putString("img_html",temp2);
                msg1.setData(b);
                myHandler.sendMessage(msg1);
            }
        }).start();

    }
    //重写Handler方法，主要为了实现子线程与主线程之间的 数据传递
    class Person_MyHandler extends Handler {
        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            // 此处可以更新UI
            if(msg.what == 120){
                Bundle b = msg.getData();
                String result = b.getString("course_temp");
                String result_img = b.getString("img_html");
                Http_Img = result_img;
                  getBitmap();
               StoreResult(result);
            }
            if(msg.what == 100){
                if(bitmap!=null)
                Info_img.setImageBitmap(bitmap);
            }
            super.handleMessage(msg);
        }
    }
    //获得用户头像
    public  void getBitmap(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sss = "http://"+HOST+Http_Img;
                    myFileUrl = new URL(sss);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("Cookie",COOK);
                    conn.addRequestProperty("Host",HOST);
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    if (conn.getResponseCode() == 200) {
                    Message msg = new Message();
                        msg.what = 100;
                        myHandler.sendMessage(msg);
                        //System.out.println("获取图片成功！");
                    } else {
                        //System.out.println("获取图片失败！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //利用Jsoup解析网页内容
    private String parsehtml(String result){
        String total="",results = "";
        int i = 0;
        String []score = new String[100];
        Document doc = Jsoup.parse(result);
        //System.out.println( doc.title());
        Elements link_class = doc.select("table.form,tr.infolist_hr_common");
        for(Element link:link_class){
            score[i] = link.text()+"#";
            total += score[i];
        }
        results = total;
        return results;
    }
//获取照片地址
    private String parsehtml_img(String result){
        String total="",results = "";
        int i = 0;
        String []score = new String[100];
        Document doc = Jsoup.parse(result);
        //System.out.println( doc.title());
        Elements link_class = doc.getElementsByTag("img");
        for(Element link:link_class){
            score[i] = link.attr("src");

            total += score[i];
            //System.out.println("img="+score[i]);
        }
        results = total;
        return results;
    }
    //排版函数
    private void StoreResult(String result){
        String []temp1 = result.split("#");
        String temp2[][] = new String[temp1.length][];
        for(int i=0;i<temp1.length;i++){
            temp2[i] = temp1[i].split(" ");
        }
        s1 = new String[temp2[0].length];
        s2 = new String[temp2[1].length];
        int k = 0;
        for(int i=0;i<s1.length;i++){
            if((i%2) ==1) {
                s1[i] = temp2[0][i];
                info_tv[k++].setText(temp2[0][i]);
            }

        }
        for(int i=0;i<s2.length;i++){
            s2[i] = temp2[1][i];
            info_c_tv[i].setText(temp2[1][i]);
        }
    }
}
