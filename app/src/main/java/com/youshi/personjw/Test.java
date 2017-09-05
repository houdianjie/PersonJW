package com.youshi.personjw;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.youshi.personjw.uitl.GetHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by dianjie on 2016/7/18.
 */
public class Test extends Activity {
    private String cook;
     private  Button person_classroom_btn1;
    private GetHttp getHttp;
    private Person_MyHandler myHandler;
    private TextView [][]person_course_tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_classroom);
        init();

    }
    private void init(){
        person_classroom_btn1 = (Button) findViewById(R.id.person_classroom_btn1);
        getHttp = GetHttp.getGetHttp();
        myHandler = new Person_MyHandler();
       // person_classroom_tv1 = (TextView) findViewById(R.id.person_classroom_tv1);
        person_course_tv = new TextView[5][7];
        sub_inti();


    }
    private void sub_inti(){
        person_course_tv[0][0] = (TextView) findViewById(R.id.person_course_tv11);person_course_tv[0][1] = (TextView) findViewById(R.id.person_course_tv12);person_course_tv[0][2] = (TextView) findViewById(R.id.person_course_tv13);
        person_course_tv[0][3] = (TextView) findViewById(R.id.person_course_tv14);person_course_tv[0][4] = (TextView) findViewById(R.id.person_course_tv15);person_course_tv[0][5] = (TextView) findViewById(R.id.person_course_tv16);
        person_course_tv[0][6] = (TextView) findViewById(R.id.person_course_tv17);person_course_tv[1][0] = (TextView) findViewById(R.id.person_course_tv21);person_course_tv[1][1] = (TextView) findViewById(R.id.person_course_tv22);
        person_course_tv[1][2] = (TextView) findViewById(R.id.person_course_tv23);person_course_tv[1][3] = (TextView) findViewById(R.id.person_course_tv24);person_course_tv[1][4] = (TextView) findViewById(R.id.person_course_tv25);
        person_course_tv[1][5] = (TextView) findViewById(R.id.person_course_tv26);person_course_tv[1][6] = (TextView) findViewById(R.id.person_course_tv27);person_course_tv[2][0] = (TextView) findViewById(R.id.person_course_tv31);
        person_course_tv[2][1] = (TextView) findViewById(R.id.person_course_tv32);person_course_tv[2][2] = (TextView) findViewById(R.id.person_course_tv33);person_course_tv[2][3] = (TextView) findViewById(R.id.person_course_tv34);
        person_course_tv[2][4] = (TextView) findViewById(R.id.person_course_tv35);person_course_tv[2][5] = (TextView) findViewById(R.id.person_course_tv36);person_course_tv[2][6] = (TextView) findViewById(R.id.person_course_tv37);
        person_course_tv[3][0] = (TextView) findViewById(R.id.person_course_tv41);person_course_tv[3][1] = (TextView) findViewById(R.id.person_course_tv42);person_course_tv[3][2] = (TextView) findViewById(R.id.person_course_tv43);
        person_course_tv[3][3] = (TextView) findViewById(R.id.person_course_tv44);person_course_tv[3][4] = (TextView) findViewById(R.id.person_course_tv45);person_course_tv[3][5] = (TextView) findViewById(R.id.person_course_tv46);
        person_course_tv[3][6] = (TextView) findViewById(R.id.person_course_tv47);person_course_tv[4][0] = (TextView) findViewById(R.id.person_course_tv51);person_course_tv[4][1] = (TextView) findViewById(R.id.person_course_tv52);
        person_course_tv[4][2] = (TextView) findViewById(R.id.person_course_tv53);person_course_tv[4][3] = (TextView) findViewById(R.id.person_course_tv54);person_course_tv[4][4] = (TextView) findViewById(R.id.person_course_tv55);
        person_course_tv[4][5] = (TextView) findViewById(R.id.person_course_tv56);person_course_tv[4][6] = (TextView) findViewById(R.id.person_course_tv57);
    }
    public void classroom_btn1_click(View v){
        String s = parsehtml(s());




    }


    public void StoreResult(String a){
        String []splits1 = a.split("\\|\\|");
        String [][]splits2 = new String[splits1.length][] ;
        for(int i=0;i<splits1.length;i++){
            splits2[i] = splits1[i].split("#");
        }
    }
    //重写Handler方法，主要为了实现子线程与主线程之间的 数据传递
    class Person_MyHandler extends Handler {
        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            // 此处可以更新UI
            if(msg.what == 100){
                Bundle b = msg.getData();
                String result = b.getString("course_temp");
                //tv2.setText(result);
            }
            super.handleMessage(msg);
        }
    }
    //利用Jsoup解析网页内容
    private String parsehtml(String result){
        String total="",results = "";
        int i = 0;
        String []score = new String[100];
        Document doc = Jsoup.parse(result);
        System.out.println( doc.title());
        Elements link_class = doc.select("tr.infolist_common[style=\"display:\"]");
       // Elements link_class = doc.select("td.center > a");
        for(Element link:link_class){
         /*   String name;
            name = link.tagName();
            score[i] = link.text();
            if(name.equals("a") && flag == 0){
                total += score[i]+",";
                flag = 1;
            } else if (name.equals("a") && flag == 1) {
                total += score[i]+" ";
            } else {
                total += ","+score[i];
                results += total + "||";
                System.out.println(total);
                flag = 0;
                total = "";
            }*/
            String name;
            name = link.tagName();
            System.out.println(name);
            score[i] = link.text();
            total += score[i]+"||\n";
            System.out.println(score[i]);
            results = total;
        }
        return results;
    }


    //测试用的html字符串
    private String s(){

        return "aa";
    }
}

