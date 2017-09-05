package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.youshi.personjw.R;
import com.youshi.personjw.uitl.GetHttp;
import com.youshi.personjw.uitl.OPT_Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dianjie on 2016/11/30.
 */
public class Person_Fail_Course extends Activity {
    private String HTTP,COOKIE,HOST;
    private GetHttp getHttp;
    private MyHandler myHandler;
    private ListView listView;
    private Fail_Course fail_course[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_fail_course);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        COOKIE = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
        init();
        if(OPT_Data.File_Exist("fail_course")){
            try {
                Store_Result(OPT_Data.Read_File("fail_course"));
            } catch (IOException e) {
                Toast.makeText(Person_Fail_Course.this,"读取缓存数据失败！\n请重新获取！",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private void init(){
        myHandler = new MyHandler();
        getHttp = GetHttp.getGetHttp();
        HTTP = "http://"+HOST+"/academic/manager/score/studentStudyFailNum.do?groupId=&moduleId=2026";
        listView = (ListView) findViewById(R.id.fail_course_lv);
    }
    public void fail_course_Click(View view){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = getHttp.GetInfo_Maintain(HOST,HTTP,COOKIE);
                String parse_result = parse_html(result);
                Bundle bundle = new Bundle();
                bundle.putString("result",parse_result);
                Message message = new Message();
                message.setData(bundle);
                message.what=0x100;
                myHandler.sendMessage(message);
            }
        }).start();

    }
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x100){
                String result = msg.getData().getString("result");
                try {
                    OPT_Data.WriteDatas("fail_course",Person_Fail_Course.this,result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Store_Result(result);
            }
            super.handleMessage(msg);
        }
    }
    private String parse_html(String result){
        String total="",results = "";
        String score ="";
        Document doc = Jsoup.parse(result);
        //System.out.println( doc.title());
        Elements link_term_item = doc.select("td");
        for(Element link:link_term_item){
            score = link.text()+"||";
            //Log.i("TABLE",score);
            total += score;
        }
        results = total;
        return results;
    }
    private void Store_Result(String result){
        String temp[] = result.split("\\|\\|");
        if(temp.length%10!=0) return;
        fail_course = new Fail_Course[temp.length/10];
        for(int i=0;i<fail_course.length;i++){
            fail_course[i] = new Fail_Course();
        }
        for(int k=0;k<temp.length;k++){
            if(k%10==0) fail_course[k/10].course_num = temp[k];
            else if(k%10==1) fail_course[k/10].course_name = temp[k];
            else if(k%10==2) fail_course[k/10].course_credit = temp[k];
            else if(k%10==3) fail_course[k/10].course_attr = temp[k];
            else if(k%10==4) fail_course[k/10].normal_test = temp[k];
            else if(k%10==5) fail_course[k/10].restudy = temp[k];
            else if(k%10==6) fail_course[k/10].recheck = temp[k];
            else if(k%10==7) fail_course[k/10].add_check = temp[k];
            else if(k%10==8) fail_course[k/10].other = temp[k];
            else if(k%10==9) fail_course[k/10].highest_score = temp[k];
        }
        //此处将数据显示到界面上
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();/*在数组中存放数据*/
        for (int i = 0; i < fail_course.length; i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("course_name", fail_course[i].course_name);
            map.put("course_num", fail_course[i].course_num);
                map.put("course_credit", fail_course[i].course_credit);
                  map.put("course_attr", fail_course[i].course_attr);
                  map.put("normal_test", fail_course[i].normal_test);
                       map.put("restudy",fail_course[i].restudy);
                        map.put("recheck",fail_course[i].recheck);
                map.put("add_check", fail_course[i].add_check);
                map.put("other",fail_course[i].other);
                map.put("highest_score", fail_course[i].highest_score);
                listItem.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(Person_Fail_Course.this, listItem, R.layout.person_fail_course_item,
                new String[]{"course_num", "course_name", "course_credit", "course_attr", "normal_test", "restudy", "recheck",
                        "add_check", "other", "highest_score"},
                new int[]{R.id.fail_course_num,R.id.fail_course_name,R.id.fail_course_credit,R.id.fail_course_attr,R.id.fail_course_normal_test,
                R.id.fail_course_restudy,R.id.fail_course_recheck,R.id.fail_course_add_check,R.id.fail_course_other,R.id.fail_course_highest});
        listView.setAdapter(simpleAdapter);
    }
    //内部数据类
    class Fail_Course{
        public String course_num,course_name,course_credit,course_attr;
        public String normal_test,restudy,recheck,add_check,other,highest_score;
        public Fail_Course(){
                course_attr=null;course_credit=null;course_name=null;course_num=null;
            normal_test=null;restudy=null;recheck=null;add_check=null;other=null;highest_score=null;
        }
    }
}
