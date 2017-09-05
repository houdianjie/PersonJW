package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
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
 * Created by dianjie on 2016/8/25.
 */
//修读进度
public class Person_Get_SP extends Activity {
    private My_Handler my_handler;
    private String Cookie,HOST;
    private GetHttp getHttp;
    private String parse1[],parse_end[][];
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_get_sp);
        Intent intent = Person_Get_SP.this.getIntent();
        Bundle bundle = intent.getExtras();
        Cookie = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
        init();
    }
    private void init(){
        my_handler = new My_Handler();
        getHttp = GetHttp.getGetHttp();
        parse1=null;parse_end=null;
        listView = (ListView) findViewById(R.id.person_study_proc_lv);
    }
    private void Display(){
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<>();
       for(int i=1;i<parse_end.length;i++){
           HashMap<String,Object> hashMap = new HashMap<>();
           hashMap.put("classes",parse_end[i][1]);
           hashMap.put("attr",parse_end[i][2]);
           hashMap.put("req_credit",parse_end[i][3]);
           hashMap.put("get_credit",parse_end[i][4]);
           hashMap.put("req_num",parse_end[i][5]);
           hashMap.put("get_num",parse_end[i][6]);
           hashMap.put("ispassed",parse_end[i][7]);
           arrayList.add(hashMap);
       }
        SimpleAdapter adapter = new SimpleAdapter(Person_Get_SP.this,arrayList,R.layout.person_studyproc_ls_item,
                new String[]{"classes","attr","req_credit","get_credit","req_num","get_num","ispassed"},
                new int[]{R.id.stu_proc_class,R.id.stu_proc_attr,R.id.stu_proc_req_credit,R.id.stu_proc_get_credit,R.id.stu_proc_req_num,
                R.id.stu_proc_pass_num,R.id.stu_proc_ispassed});
        listView.setAdapter(adapter);
    }
    public void person_getsp_btn_click(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                String s = "http://"+HOST+"/academic/manager/score/studentStudyProcess.do?groupId=&moduleId=2025";
                //result =postHttp.PostAndGetCET(http,Kaohao,Name,Cookie);
                result = getHttp.Get_Person_SP(HOST,s,Cookie);
                if(result != null){
                    parsehtml(result);
                }
                Message msg1 = new Message();
                msg1.what = 300;
                my_handler.sendMessage(msg1);
            }
        }).start();
    }
    class My_Handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 300){
            StoreResult();
                Display();
            }
            super.handleMessage(msg);
        }
    }
    //利用Jsoup解析网页内容
    private void parsehtml(String result){
        int i = 0;
        String score = null;
        Document doc = Jsoup.parse(result);
       // System.out.println("title"+ doc.title());
        Elements link_class = doc.select("tr");
        parse1 = new String[link_class.size()];
        for(Element link:link_class) {
            score = link.text();
            parse1[i++] = score;
            //System.out.println("TR----->"+parse1[i-1]);
        }
    }
    private void StoreResult(){
        int count=0,num=0;
        String temp[][] = new String[parse1.length][];
        for(int i=0;i<parse1.length;i++){
            temp[i] = parse1[i].split(" ");
            if(temp[i].length>6){
                num++;
            }
        }
        parse_end = new String[num][];
        for(int k=0;k<temp.length;k++){
            if(temp[k].length > 6){
                parse_end[count++] = new String[temp[k].length];
                for(int j=0;j<temp[k].length;j++){
                        parse_end[count-1][j] = temp[k][j];
                    //System.out.println("RESULT====>"+count);
                }
            }
        }
    }
}
