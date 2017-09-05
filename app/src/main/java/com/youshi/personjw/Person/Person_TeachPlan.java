package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.youshi.personjw.R;
import com.youshi.personjw.uitl.GetHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dianjie on 2016/7/25.
 */
public class Person_TeachPlan extends Activity {
    private GetHttp getTeachPlan;
    private Button person_teachplan_btn;
    private String COOK,HOST;
    private Person_MyHandler myHandler;
    private String p_teachplan_http;
    private String []term_subtitle;
    private String []term_personinfo;
    private String [][][]term_course_items;
    private ListView teachplan_lv;
    private EditText teachplan_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_teachplan);
        init();
        Intent intent = Person_TeachPlan.this.getIntent();
        Bundle bundle = intent.getExtras();
        COOK = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
    }
    private void init(){
        getTeachPlan = GetHttp.getGetHttp();
        person_teachplan_btn = (Button) findViewById(R.id.person_teachplan_btn1);
        myHandler = new Person_MyHandler();
        term_personinfo = null;
        term_subtitle = null;
        term_course_items = null;
        teachplan_lv = (ListView) findViewById(R.id.person_teachplan_lv);
        teachplan_et = (EditText) findViewById(R.id.person_teachplan_et);
    }
    public void person_teachplan_click(View v){
        term_course_items = null;
        term_subtitle = null;
        term_personinfo = null;
        Toast toast = Toast.makeText(Person_TeachPlan.this,"请稍后...",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String http = "http://"+HOST+"/academic/student/studyschedule/studyschedule.jsdo?groupId=&moduleId=2070";
                String result1,result2,temp = null,temps = null,temp2 = null;
                result1 = getTeachPlan.GetTeachPlanHttp(HOST,http,COOK);
                if(result1 != null){
                    temp2 = parsehtml_http(result1);
                }
                String []temp1 = temp2.split("#");
                p_teachplan_http = temp1[3].replace("scheduleJump.jsp?link=studentScheduleShowByTerm.do&",
                        "studentScheduleShowByTerm.do?z=z&");
                //System.out.println("final---->"+p_teachplan_http);
                String https = "http://" +HOST+ p_teachplan_http;
                //System.out.println("https----->"+https);
                result2 = getTeachPlan.GetTeachPlan(HOST,https,COOK);
                if(result2 != null) {
                    temp = parsehtml_subtitle(result2);
                    temps = parsehtml_Item(result2);
                }
                 Message msg1 = new Message();
                msg1.what = 130;
                Bundle b = new Bundle();
                //向主UI发送数据
                b.putString("teachplan_subtitle",temp);
                b.putString("teachplan_item",temps);
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
            if(msg.what == 130){
                Bundle b = msg.getData();
                String temp_subtitle,temp_item;
                //得到子线程发送的数据
                temp_subtitle = b.getString("teachplan_subtitle");
                temp_item = b.getString("teachplan_item");
                Store_subtitle(temp_subtitle);
                Store_Item(temp_item);
            }

            super.handleMessage(msg);
        }
    }
    //得到学期教学计划网址
    private String parsehtml_http(String result) {
        String total = "", results = "";
        int i = 0;
        String[] score = new String[5];
        Document doc = Jsoup.parse(result);
        Elements link_class = doc.getElementsByTag("a");
        for (Element link : link_class) {
            score[i] = link.attr("href")+ "#";
            //Log.i("TEXT",score[i]);
            total += score[i];
        }
        results = total;
        return results;
    }
    //利用Jsoup解析网页内容,获取学期
    private String parsehtml_subtitle(String result){
        String total="",results = "";
        int i = 0;
        String score="";
        Document doc = Jsoup.parse(result);
        Log.i("TITLE",doc.title());
        Elements link_class = doc.select("div.titletext");
        for(Element link:link_class){
            score = link.text()+"#";
            //Log.i("TITLE",score);
            total += score;
        }
        results = total;
        return results;
    }
    //利用Jsoup解析网页内容，获取学期课程
    private String parsehtml_Item(String result){
        String total="",results = "";
        String score ="";
        Document doc = Jsoup.parse(result);
        //System.out.println( doc.title());
        Elements link_term_item = doc.select("table.output_ctx");
        for(Element link:link_term_item){
            score = link.text()+"||";
            //Log.i("TABLE",score);
            total += score;
        }
        results = total;
        return results;
    }
    private void Store_subtitle(String result){
        term_subtitle = result.split("#");
       /* for(int i=0;i<term_subtitle.length;i++)
        System.out.println("学期---->"+term_subtitle[i]);*/
    }
    private void Store_Item(String result){
            String []temps1 = result.split("\\|\\|");
        //String temp1 = "";
        //从字符串数组中获取学生的信息,暂时不准备使用
        //term_personinfo = temp1.split(" ");
        //Log.i("temp1.len",temps1.length+"");
        String [][]temps2 = new String[temps1.length][];
        for(int i=0;i<temps1.length;i++){
            //将各个学期的课程分开并存储
            temps2[i] = temps1[i].split(" ");
           // Log.i("temp2.len",temps2.length+"");
           // Log.i("temp2[i].len",temps2[i].length+"");
        }
        //测试输出信息
        term_course_items = new String[temps1.length][][];

       for(int i=0;i<temps1.length;i++){
           int aa = temps2[i].length/9-1;
           if(temps2[i].length%9!=0)
               term_course_items[i] = new String[aa][9+temps2[i].length%9];
           else
           term_course_items[i] = new String[aa][9];
       }
        int a = 0,b = 0;
        for(int i=0;i<temps1.length;i++){
            //Log.i("temp1.len",temps2.length+"");
           for(int j=9;j<temps2[i].length;j++){
            //   Log.i("temp2.len",temps2.length+"");
             //  Log.i("temp2[i].len",temps2[i].length+"");
               if(temps2[i].length%9!=0) {
                  // Log.i("LENGTH", term_course_items[i].length+ ","+i+","+b);
                   term_course_items[i][b++ / (9 + temps2[i].length % 9)][a++ % (9 + temps2[i].length % 9)] = temps2[i][j];
               }
               else
               term_course_items[i][b++/9][a++%9] = temps2[i][j];
           }
            b = 0;
            a = 0;
        }
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();/*在数组中存放数据*/
        for (int i = 0; i < term_course_items.length; i++) {
            for(int j=0;j<term_course_items[i].length;j++){
                HashMap<String, Object> map = new HashMap<>();
                map.put("subtitle", term_subtitle[i]);
                map.put("course_num", term_course_items[i][j][0]);
                map.put("course", term_course_items[i][j][1]);
                map.put("checkway", term_course_items[i][j][2]);
                map.put("credit", term_course_items[i][j][3]);
                map.put("all_teachtime",term_course_items[i][j][4]);
                map.put("mingcheng", "NULL");
                map.put("exptime","NULL");
                map.put("test", "NULL");
                map.put("othertime", "NULL");
                map.put("teachtime", "NULL");
                map.put("fenzu", term_course_items[i][j][6]);
                map.put("leibie", term_course_items[i][j][5]);
                map.put("yaoqiu", term_course_items[i][j][7]);
                map.put("fangxiang", term_course_items[i][j][8]);
                map.put("passflag","NULL");
                listItem.add(map);
            }

        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(Person_TeachPlan.this, listItem, R.layout.person_teachplan_list_items, new String[]
                {"subtitle", "course_num", "course", "checkway", "credit", "all_teachtime", "mingcheng", "exptime", "test", "othertime", "teachtime",
                        "fenzu", "leibie", "yaoqiu", "fangxiang", "passflag"}, new int[]
                {R.id.teachpan_item_term,R.id.teachpan_item_course_num,R.id.teachpan_item_course,R.id.teachpan_item_checkway,R.id.teachpan_item_credit,
                R.id.teachpan_item_all_teachtime,R.id.teachpan_item_mingcheng,R.id.teachpan_item_exptime,R.id.teachpan_item_test,R.id.teachpan_item_othertime,
                R.id.teachpan_item_teachtime,R.id.teachpan_item_fenzu,R.id.teachpan_item_leibie,R.id.teachpan_item_yaoqiu,R.id.teachpan_item_fangxiang,R.id.teachpan_item_passflag});
        teachplan_lv.setAdapter(simpleAdapter);
    }
    public void person_teachplan_search_click(View v){
        String temp = "-1";
              temp =  teachplan_et.getText().toString();
        Pattern pattern = Pattern.compile(temp);
        if(term_course_items == null){
            Toast t = Toast.makeText(Person_TeachPlan.this,"请先获取教学计划！",Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else {
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();/*在数组中存放数据*/
            for (int i = 0; i < term_course_items.length; i++) {
                Matcher matcher = pattern.matcher(term_subtitle[i]);
                boolean bool = matcher.find();
                if (bool) {
                    for (int j = 0; j < term_course_items[i].length; j++) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("subtitle", term_subtitle[i]);
                        map.put("course_num", term_course_items[i][j][0]);
                        map.put("course", term_course_items[i][j][1]);
                        map.put("checkway", term_course_items[i][j][2]);
                        map.put("credit", term_course_items[i][j][3]);
                        map.put("all_teachtime",term_course_items[i][j][4]);
                        map.put("mingcheng", "NULL");
                        map.put("exptime","NULL");
                        map.put("test", "NULL");
                        map.put("othertime", "NULL");
                        map.put("teachtime", "NULL");
                        map.put("fenzu", term_course_items[i][j][6]);
                        map.put("leibie", term_course_items[i][j][5]);
                        map.put("yaoqiu", term_course_items[i][j][7]);
                        map.put("fangxiang", term_course_items[i][j][8]);
                        map.put("passflag","NULL");
                        listItem.add(map);
                    }

                }
            }
                SimpleAdapter simpleAdapter = new SimpleAdapter(Person_TeachPlan.this, listItem, R.layout.person_teachplan_list_items, new String[]
                        {"subtitle", "course_num", "course", "checkway", "credit", "all_teachtime", "mingcheng", "exptime", "test", "othertime", "teachtime",
                                "fenzu", "leibie", "yaoqiu", "fangxiang", "passflag"}, new int[]
                        {R.id.teachpan_item_term, R.id.teachpan_item_course_num, R.id.teachpan_item_course, R.id.teachpan_item_checkway, R.id.teachpan_item_credit,
                                R.id.teachpan_item_all_teachtime, R.id.teachpan_item_mingcheng, R.id.teachpan_item_exptime, R.id.teachpan_item_test, R.id.teachpan_item_othertime,
                                R.id.teachpan_item_teachtime, R.id.teachpan_item_fenzu, R.id.teachpan_item_leibie, R.id.teachpan_item_yaoqiu, R.id.teachpan_item_fangxiang, R.id.teachpan_item_passflag});
                teachplan_lv.setAdapter(simpleAdapter);
            }

    }
}
