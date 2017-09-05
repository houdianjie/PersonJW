package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.youshi.personjw.R;
import com.youshi.personjw.uitl.PostHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by dianjie on 2016/7/22.
 */
public class Person_Score extends Activity {
    private String cook,HOST;
    private Button btn5;
    private PostHttp postHttp;
    private Person_MyHandler myHandler;
    private Score []scores;
    private ListView person_score_lv1;
    private Spinner spn1,spn2;
    private String spn1_con = null,spn2_con = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_score);
        Intent intent = Person_Score.this.getIntent();
        Bundle bundle = intent.getExtras();
        cook = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
        init();
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spn1_con = spn1.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spn2_con = spn2.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void init(){
        btn5 = (Button) findViewById(R.id.person_score_btn1);
        postHttp = PostHttp.getPostHttp();
        myHandler = new Person_MyHandler();
        scores = new Score[30];
        person_score_lv1 = (ListView) findViewById(R.id.person_score_lv1);
        spn1 = (Spinner) findViewById(R.id.person_score_spn1);
        spn2 = (Spinner) findViewById(R.id.person_score_spn2);
        for(int i=0;i<30;i++){
            scores[i] = new Score();
        }
    }
    public void person_score_click1(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String term = 1+"";
                String year = 36+"";
                if(spn2_con.equals("秋季")){
                    term = 2+"";
                }
                //2009=29
                int years = 2009;
                years = Integer.parseInt(spn1_con)-1980;
                if(years >=29 && years <= 44) {
                    year = years+"";
                }
                String courhttp = "http://"+HOST+"/academic/manager/score/studentOwnScore.do?groupId=&moduleId=2020";
                String temp1 = null,temp2 = null;
                //在网络上获取课程表
                temp1 = postHttp.PostScore(HOST,courhttp,term,year,"查询","0",cook);
                temp2 = parsehtml(temp1);
                //向主线程发送带数据的消息
                Message msg1 = new Message();
                msg1.what = 100;
                Bundle b = new Bundle();
                b.putString("course_temp",temp2);
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
            if(msg.what == 100) {
                Bundle b = msg.getData();
                String result = b.getString("course_temp");
                //对每次操作都进行一次置空操作，保证数据正确性
                Set_Null();
                //网络返回的数据在这
                StoreResult(result);
                ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();/*在数组中存放数据*/
                for(int i=0;i<scores.length;i++){
                    if(scores[i].course_name != null){
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("course_name",scores[i].course_name);
                        map.put("teacher",scores[i].teacher);
                        map.put("final_score",scores[i].final_score);
                        map.put("grade_point",scores[i].grade_point);
                        map.put("credits",scores[i].credits);
                        map.put("check_way",scores[i].check_way);
                        map.put("flag_pass",scores[i].flag_pass);
                        listItem.add(map);
                        /*
                        //测试
                        System.out.println(scores[i].course_name);
                        System.out.println(scores[i].teacher);
                        System.out.println(scores[i].final_score);
                        System.out.println(scores[i].grade_point);
                        System.out.println(scores[i].credits);
                        System.out.println(scores[i].check_way);
                        System.out.println(scores[i].flag_pass);
                        */
                    }
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(Person_Score.this,listItem,R.layout.person_score_list_items,new String[]
                        {"course_name","teacher","final_score","grade_point","credits","check_way","flag_pass"},new int[]
                        {R.id.score_item_tv1,R.id.score_item_tv2,R.id.score_item_tv3,R.id.score_item_tv4,R.id.score_item_tv5,R.id.score_item_tv6,R.id.score_item_tv7});
                person_score_lv1.setAdapter(simpleAdapter);
                //person_score_tv1.setText(result);
            }
            super.handleMessage(msg);
        }
    }
    private String parsehtml(String result) {
        String total="",results = "";
        int i = 0;
        String score_temp = "";
        Document doc = Jsoup.parse(result);
      //  System.out.println( doc.title());
        Elements link_class = doc.select("tr td:gt(2)");
        //Elements link_class = doc.select("tr > td");
        for(Element link:link_class){
            String name;
            name = link.tagName();
           // Log.i("NAME",name);
            if((i++%14)==0){
                score_temp = link.text() + "||";
                //Log.i("NAME",score_temp);
                total += score_temp;
            }
           else {
                score_temp = link.text() + "#";
               // Log.i("NAME",score_temp);
                total += score_temp;
            }
            results = total;
        }
        return results;
    }
    private void Set_Null(){
        for(int i=0;i<scores.length;i++){
            if(scores[i].course_name != null){
                scores[i].course_name = null;
                scores[i].teacher = null;
                scores[i].final_score = null;
                scores[i].grade_point = null;
                scores[i].credits = null;
                scores[i].check_way = null;
                scores[i].flag_pass = null;
            }
        }
    }
    public void StoreResult(String a){
        String []splits1 = a.split("\\|\\|");
        String [][]splits2 = new String[splits1.length][] ;
        for(int i=0;i<splits1.length;i++){
            splits2[i] = splits1[i].split("#");
        }
        for(int i=1;i<splits1.length;i++) {
            for (int j = 0; j < splits2[i].length; j++) {
                if(j == 0){
                    scores[i].course_name = splits2[i][j];
                }
                if(j == 2){
                    scores[i].teacher = splits2[i][j];
                }
                if(j == 4){
                    scores[i].final_score = splits2[i][j];
                }
                if(j == 5){
                    scores[i].grade_point = splits2[i][j];
                }
                if(j == 6){
                    scores[i].credits = splits2[i][j];
                }
                if(j == 10){
                    scores[i].check_way = splits2[i][j];
                }
                if(j == 12){
                    scores[i].flag_pass = splits2[i][j];
                }
            }
        }
    }
    public class Score{
        String  course_name;
        String teacher;
        String final_score;
        String grade_point;
        String credits;
        String check_way;
        String flag_pass;
        public Score(){
            course_name = null;
            teacher = null;
            final_score = null;
            grade_point = null;
            credits = null;
            check_way = null;
            flag_pass = null;
        }
    }
}
