package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.youshi.personjw.R;
import com.youshi.personjw.uitl.GetHttp;
import com.youshi.personjw.uitl.OPT_Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dianjie on 2016/7/18.
 */
public class Person_Course extends Activity {
    private String COOK,HOST;
    private Button btn5;
    private GetHttp getHttp;
    private Person_MyHandler myHandler;
    private static int flag;
    private CourseTable2 []courseTable2s;
    private TextView [][]person_course_tv;
    private Spinner spn1,spn2;
    private String spn1_con = null,spn2_con = null;
    private TypedArray bg_arr;
    private int []bg_Ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_course);
        init();
        Intent intent = Person_Course.this.getIntent();
        Bundle bundle = intent.getExtras();
        COOK = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
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
        if(OPT_Data.File_Exist("course")){
            try {
                StoreResult(OPT_Data.Read_File("course"));
            } catch (IOException e) {
                Toast.makeText(Person_Course.this,"读取缓存数据失败！\n请重新获取！",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        //检测cookie是否一致
       // System.out.println(COOK);
    }
    private void init(){
        btn5 = (Button) findViewById(R.id.person_course_btn5);
        person_course_tv = new TextView[5][7];
        sub_inti();
        getHttp = GetHttp.getGetHttp();
        myHandler = new Person_MyHandler();
        courseTable2s = new CourseTable2[20];
        spn1 = (Spinner) findViewById(R.id.person_course_spn1);
        spn2 = (Spinner) findViewById(R.id.person_course_spn2);
        bg_arr = Person_Course.this.getResources().obtainTypedArray(R.array.cour_tvs_bg);
        int bg_arr_len = bg_arr.length();
        bg_Ids = new int[bg_arr_len];
        for(int i=0;i<bg_arr_len;i++){
            bg_Ids[i] = bg_arr.getResourceId(i,0);
        }
        for(int i=0;i<20;i++){
            courseTable2s[i] =new CourseTable2();
        }
        flag = 0;
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
    public void btn_get_class_click(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String term = "term=1";
                String year = "year=36";
                if(spn2_con.equals("秋季")){
                    term = "term=2";
                }
                //2009=29
                int years = 2009;
                years = Integer.parseInt(spn1_con)-1980;
                if(years >=29 && years <= 44) {
                    year = "year=" + years;
                }
                String courhttp = "http://"+HOST+"/academic/student/currcourse/currcourse.jsdo?"+year+"&"+term;
                String temp1 = null,temp2 = null;
                //在网络上获取课程表
                temp1 = getHttp.GetCourse(HOST,courhttp,COOK);
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
            if(msg.what == 100){
            Bundle b = msg.getData();
            String result = b.getString("course_temp");
                try {
                    OPT_Data.WriteDatas("course",Person_Course.this,result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //对每次操作都进行一次置空操作，保证数据正确性
                Set_Null();
                //网络返回的数据在这
                StoreResult(result);
            }
            super.handleMessage(msg);
        }
    }

    //对数据置空操作，防止二次请求出错
    private void Set_Null(){
        for(int i=0;i<courseTable2s.length;i++){
            if(courseTable2s[i].name != null){
               courseTable2s[i].name = null;
                for(int j=0;j<courseTable2s[i].teacher.length;j++) {
                    if (courseTable2s[i].teacher[j] != null) {
                        courseTable2s[i].teacher[j] = null;
                        for (int k = 0; k < courseTable2s[i].course1s.length; k++) {
                            if (courseTable2s[i].course1s[k].have_calss != null) {
                           courseTable2s[i].course1s[k].allweeks = null;
                               courseTable2s[i].course1s[k].have_calss = null;
                               courseTable2s[i].course1s[k].class_time = null;
                               courseTable2s[i].course1s[k].addr = null;
                            }
                        }
                    }
                }
            }
        }
    }
    //利用Jsoup解析网页内容
    private String parsehtml(String result) {
        String total="",results = "";
        int i = 0;
        String []cla = new String[100];
        Document doc = Jsoup.parse(result);
        System.out.println( doc.title());
        Elements link_class = doc.select("a[target=\"_blank\"].infolist,table[width=\"100%\"]");
        // Elements link_class = doc.select("td.center > a");
        for(Element link:link_class){
            String name;
            name = link.tagName();
            cla[i] = link.text();
            if(name.equals("a") && flag == 0){
                total += cla[i]+"#";
                flag = 1;
            } else if (name.equals("a") && flag == 1) {
                total += cla[i]+" ";
            } else {
                total += "#"+cla[i];
                results += total + "||";
               // System.out.println(total);
                flag = 0;
                total = "";
            }
        }
        return results;
    }
    //存储排版后的结果
    public void StoreResult(String a){
        String []splits1 = a.split("\\|\\|");
        String [][]splits2 = new String[splits1.length][] ;
        String [][][]splits3 = null;
        for(int i=0;i<splits1.length;i++){
            splits2[i] = splits1[i].split("#");
        }
        for (String[] aSplits2 : splits2) {
            splits3 = new String[splits1.length][aSplits2.length][];
        }
        for(int i=0;i<splits1.length;i++) {
            for (int j = 0; j < splits2[i].length; j++) {
                assert splits3 != null;
                splits3[i][j] = splits2[i][j].split(" ");
            }
        }
        for(int i=0;i<splits1.length;i++) {
            assert splits3 != null;
            courseTable2s[i].name = splits3[i][0][0];
            for (int j = 0; j < splits2[i].length; j++) {
                if(j == 1) {
                    for (int k = 0; k < splits3[i][j].length; k++) {
                        courseTable2s[i].teacher[k] = splits3[i][j][k];
                    }
                }
                if(j == 2) {
                    int q = 0;
                    for (int k = 0; k < splits3[i][j].length; k++) {
                        if ((k % 4) == 0) {
                            courseTable2s[i].course1s[q++/4].allweeks = splits3[i][j][k];
                        } else if ((k % 4) == 1) {
                            courseTable2s[i].course1s[q++/4].have_calss = splits3[i][j][k];
                        } else if ((k % 4) == 2) {
                            courseTable2s[i].course1s[q++/4].class_time = splits3[i][j][k];
                        } else {
                            courseTable2s[i].course1s[q++/4].addr = splits3[i][j][k];
                        }
                    }
                }
            }
        }
        for(int i=0;i<5;i++){
            for (int j=0;j<7;j++){
                int temps_random = (int)(0+Math.random()*5);
                person_course_tv[i][j].setBackgroundResource(bg_Ids[temps_random]);
                person_course_tv[i][j].setText("");
            }
        }
        for(int i=0;i<courseTable2s.length;i++){
            if(courseTable2s[i].name != null){
                String name = courseTable2s[i].name;
                String s = "";

                int dwe = 0;
                //测试 System.out.println(name);
                for(int j=0;j<courseTable2s[i].teacher.length;j++) {
                    if (courseTable2s[i].teacher[j] != null) {
                        //s += courseTable2s[i].teacher[j] + " ";
                        //System.out.println(courseTable2s[i].teacher[j]);
                    }
                }
                s += "\n";
                for (int k = 0; k < courseTable2s[i].course1s.length; k++) {
                    if (courseTable2s[i].course1s[k].have_calss != null) {
                        String[] week = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
                        String[] time = {"1、2", "3、4", "5、6", "7、8", "9、10"};
                        String[] time1 = {"1-4","5-8"};
                        Pattern pattern = Pattern.compile("、");
                        Matcher matcher = pattern.matcher(courseTable2s[i].course1s[k].class_time);
                        boolean bool = matcher.find();

                        // System.out.println("结果为"+b1+courseTable2s[i].course1s[k].class_time);
                        if(bool) {
                            Pattern pattern1 = Pattern.compile("5、6、7、8");
                            Matcher matcher1 = pattern1.matcher(courseTable2s[i].course1s[k].class_time);
                            boolean bool1 = matcher1.find();
                            Pattern pattern2 = Pattern.compile("1、2、3、4");
                            Matcher matcher2 = pattern2.matcher(courseTable2s[i].course1s[k].class_time);
                            boolean bool2 = matcher2.find();
                            if (bool1) {
                                for (int w = 0; w < week.length; w++) {
                                    if (week[w].equals(courseTable2s[i].course1s[k].have_calss)) {
                                        String temp = courseTable2s[i].name + "@" + courseTable2s[i].course1s[k].addr
                                                +"\n时间:"+courseTable2s[i].course1s[k].allweeks + "\n"/*老师:*/ + s;
                                        System.out.println("上午课"+temp);
                                        person_course_tv[2][w].append(temp);
                                        person_course_tv[3][w].append(temp);


                                    }
                                }
                            }
                            else if(bool2){
                                for (int w = 0; w < week.length; w++) {
                                    if (week[w].equals(courseTable2s[i].course1s[k].have_calss)) {
                                        String temp = courseTable2s[i].name + "@" + courseTable2s[i].course1s[k].addr
                                                +"\n时间:"+courseTable2s[i].course1s[k].allweeks + "\n"/*老师:"*/ + s;
                                        System.out.println("上午课"+temp);
                                        person_course_tv[0][w].append(temp);
                                        person_course_tv[1][w].append(temp);
                                    }
                                }
                            }
                            else {
                                for (int w = 0; w < week.length; w++) {
                                    for (int c = 0; c < time.length; c++) {
                                        if (week[w].equals(courseTable2s[i].course1s[k].have_calss) && time[c].equals(courseTable2s[i].course1s[k].class_time)) {
                                            //week_all_small[c][w] += courseTable2s[i].course1s[k].allweeks;
                                            String temp = courseTable2s[i].name + "@" + courseTable2s[i].course1s[k].addr
                                                    + "\n时间:" + courseTable2s[i].course1s[k].allweeks + "\n"/*老师:"*/ + s;
                                            person_course_tv[c][w].append(temp);
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            for (int w = 0; w < week.length; w++) {
                                for (int c = 0; c < time1.length; c++) {
                                    if (week[w].equals(courseTable2s[i].course1s[k].have_calss) && time1[c].equals(courseTable2s[i].course1s[k].class_time)) {
                                        if(c == 0) {
                                            // week_all_largre[c][w] += courseTable2s[i].course1s[k].allweeks;
                                            String temp = courseTable2s[i].name + "@" + courseTable2s[i].course1s[k].addr
                                                    +"\n时间:"+courseTable2s[i].course1s[k].allweeks + "\n"/*老师:"*/ + s;
                                            //System.out.println("上午课"+temp);
                                            person_course_tv[c][w].append(temp);
                                            person_course_tv[c+1][w].append(temp);
                                        }
                                        else{

                                            String temp = courseTable2s[i].name + "@" + courseTable2s[i].course1s[k].addr
                                                    +"\n时间:"+courseTable2s[i].course1s[k].allweeks + "\n"/*老师:"*/ + s;
                                           // System.out.println("下午课"+temp);
                                            person_course_tv[c+1][w].append(temp);
                                            person_course_tv[c+2][w].append(temp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                s = "";
            }
        }
    }
    //定义类结构体
    public  class Course1{
        String  allweeks;
        String have_calss;
        String class_time;
        String addr;
        public Course1(){
            allweeks = null;
            have_calss = null;
            class_time = null;
            addr = null;
        }
    }
    public class CourseTable2{
        public String name;
        public String []teacher;
        public Course1 []course1s;
        public  CourseTable2(){
            name = "";
            teacher = new String[10];
            course1s = new Course1[20];
            for(int i =0;i<20;i++)
                course1s[i] = new Course1();
        }
    }
}