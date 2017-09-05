package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.regex.Matcher;

/**
 * Created by dianjie on 2016/7/25.
 */
public class Person_ExamPlan extends Activity {
    private GetHttp getExamPlan;

    private String COOK,HOST;
    private Person_MyHandler myHandler;
    private String [][]ExamPlan;
    private ListView exam_paln_lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_examplan);
        init();
        Intent intent = Person_ExamPlan.this.getIntent();
        Bundle bundle = intent.getExtras();
        COOK = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
        if(OPT_Data.File_Exist("examplan")){
            try {
                StoreResult(OPT_Data.Read_File("examplan"));
            } catch (IOException e) {
                Toast.makeText(Person_ExamPlan.this,"读取缓存数据失败！\n请重新获取！",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private void init(){
        getExamPlan = GetHttp.getGetHttp();
        myHandler = new Person_MyHandler();
        exam_paln_lv = (ListView) findViewById(R.id.person_examplan_lv);
        ExamPlan = null;
    }
    public void examplan_click(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null,temp = null;
                String http = "http://"+HOST+"/academic/manager/examstu/studentQueryAllExam.do?" +
                        "pagingPageVLID=1&sortDirectionVLID=-1&sortColumnVLID=s.examRoom.exam.endTime&pagingNumberPerVLID=50";
                result = getExamPlan.GetExamPlan(HOST,http,COOK);
                if(result != null){
                    temp = parsehtml(result);
                }
                Message msg1 = new Message();
                msg1.what = 300;
                Bundle b = new Bundle();
                b.putString("course_temp",temp);
                msg1.setData(b);
                myHandler.sendMessage(msg1);
            }
        }).start();
    }
    class Person_MyHandler extends Handler {
        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            // 此处可以更新UI
            if(msg.what == 300){
                Bundle b = msg.getData();
                String result = b.getString("course_temp");
                //写排版代码
                StoreResult(result);
                try {
                    OPT_Data.WriteDatas("examplan",Person_ExamPlan.this,result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<ExamPlan.length;i++){
                    for(int j=0;j<ExamPlan[i].length;j++){
                        //System.out.println(j + "<---->" +ExamPlan[i][j]);
                    }
                }
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
        //System.out.println( doc.title());
        Elements link_class = doc.select("table.datalist");
        for(Element link:link_class) {
            score[i] = link.text();
            total += score[i];
            //System.out.println("----->"+score[i]);
        }
        results = total;
        return results;
    }
    //排版函数
    private void StoreResult(String result){
        String []temp_one = result.split(" ");
        int a = 0,k=1;
        if(temp_one.length>5) {
            for (int i = 5; i < temp_one.length; i++) {
                if(temp_one[i] != null) {
                    k++;
                }
            }
            ExamPlan = new String[(k - 5) / 6 + 1][6];

            for (int i = 5; i < temp_one.length; i++) {
                if(temp_one[i] != null) {
                    ExamPlan[(i - 5) / 6][(a++ % 6)] = temp_one[i];
                    //System.out.println(i+"<--->"+temp_one[i]);
                }
            }
        }
        k = 1;
        a = 0;
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();/*在数组中存放数据*/
        for(int i=0;i<ExamPlan.length;i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("lesson_number", ExamPlan[i][0]);
            map.put("class_name", ExamPlan[i][1]);
            map.put("test_time", ExamPlan[i][2] +" " + ExamPlan[i][3]);
            map.put("test_add", ExamPlan[i][4]);
            map.put("test_nature", ExamPlan[i][5]);
            listItem.add(map);

        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(Person_ExamPlan.this, listItem, R.layout.person_exam_items, new String[]
                {"lesson_number", "class_name", "test_time", "test_add", "test_nature"}, new int[]
                {R.id.person_exam_item_tv1,R.id.person_exam_item_tv2,R.id.person_exam_item_tv3,R.id.person_exam_item_tv4,R.id.person_exam_item_tv5});
        exam_paln_lv.setAdapter(simpleAdapter);
    }
}
