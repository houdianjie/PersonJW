package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.youshi.personjw.R;
import com.youshi.personjw.uitl.GetHttp;
import com.youshi.personjw.uitl.OPT_Data;
import com.youshi.personjw.uitl.PostHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dianjie on 2016/7/25.
 */
public class Person_Classroom extends Activity {

    private GetHttp getHttp;
    private Person_MyHandler myHandler;
    private Spinner spn_xiaoqu,spn_build,spn_week,spn_whichweek;
    private PostHttp postHttp;
    private String cook,HOST,xiaoqu_id,build_id,week_id,whichweek_id;
    private String [][]adds;
    private String [][]classes;
    private ListView person_classroom_lv;
    private String []build_fushan;
    private String []build_songshan;
    private String []build_jinjialing;
    private ArrayAdapter<String> arrayAdapter;
private EditText classroom_search_ev;
    private static int xiaoqu_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_classroom);
        init();
        Spinner_click();
        Intent intent = Person_Classroom.this.getIntent();
        Bundle bundle = intent.getExtras();
        cook = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
        if(OPT_Data.File_Exist("classroom")){
            try {
                StoreResult(OPT_Data.Read_File("classroom"));
            } catch (IOException e) {
                Toast.makeText(Person_Classroom.this,"读取缓存数据失败！\n请重新获取！",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private void init(){
        getHttp = GetHttp.getGetHttp();
        myHandler = new Person_MyHandler();
        postHttp = PostHttp.getPostHttp();
        person_classroom_lv = (ListView) findViewById(R.id.person_classroom_lv1);
        cook = null;
        adds =null;
        classes = null;
        xiaoqu_flag = 0;
        classroom_search_ev = (EditText) findViewById(R.id.classroom_search_ev);
        build_fushan = getResources().getStringArray(R.array.classroom_build_fushan);
        build_jinjialing = getResources().getStringArray(R.array.classroom_build_jinjialing);
        build_songshan = getResources().getStringArray(R.array.classroom_build_songshan);
        spn_xiaoqu = (Spinner) findViewById(R.id.person_classroom_spn_xiaoqu);
        spn_build = (Spinner) findViewById(R.id.person_classroom_spn_build);
        spn_week = (Spinner) findViewById(R.id.person_classroom_spn_week);
        spn_whichweek = (Spinner) findViewById(R.id.person_classroom_spn_whichweek);
    }
    private void Spinner_click(){
        spn_xiaoqu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                     xiaoqu_id = 1709+"";
                    arrayAdapter =new ArrayAdapter<String>(Person_Classroom.this,android.R.layout.simple_spinner_item, build_fushan);
                }
                else if(i == 1){
                    xiaoqu_id = 2312+"";
                    arrayAdapter =new ArrayAdapter<String>(Person_Classroom.this,android.R.layout.simple_spinner_item, build_songshan);
                }
                else{
                    xiaoqu_id = 13041+"";
                    arrayAdapter =new ArrayAdapter<String>(Person_Classroom.this,android.R.layout.simple_spinner_item, build_jinjialing);
                }
                xiaoqu_flag = i;
                spn_build.setAdapter(arrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spn_build.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int []fushan_ids = {2501,2470,2503,2260,1847,1783,2237,2204,1954,1904,1756,2050,2349,2419,2278,2198,5893,5059,2139,2099,2186,1769,1748,2308,2036,4842,2306,1738,2310,1710,2177,2299};
                int []jinjialing_ids = {2556,2585,2610,2641,2670,2702,4844,5743,2685,2706,2791,2748,2821,2782,5741,4121};
                int []songshan_ids = {2313,2332,5742,2346};
                if(xiaoqu_flag == 0){
                    build_id = fushan_ids[i]+"";
                }
                else if(xiaoqu_flag == 1){
                    build_id = songshan_ids[i]+"" ;
                }
                else{
                    build_id = jinjialing_ids[i]+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spn_week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                week_id = (i + 1)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spn_whichweek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                whichweek_id = (i + 1)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void classroom_btn1_click(View v){
        Toast toast = Toast.makeText(Person_Classroom.this,"请稍后...",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null,temp = null;
                String http = "http://"+HOST+"/academic/teacher/teachresource/roomschedule_week.jsdo";
                result = postHttp.PostClassRoom(HOST,http,xiaoqu_id,build_id,"-1",whichweek_id,week_id,cook);
                if(result != null){
                    temp = parsehtml(result);
                }
                Message msg1 = new Message();
                msg1.what = 100;
                Bundle b = new Bundle();
                b.putString("course_temp",temp);
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
                //写排版代码
                StoreResult(result);
                try {
                    OPT_Data.WriteDatas("classroom",Person_Classroom.this,result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        }
    }

    public void classroom_search_btn(View v){
        String temp = "-1";
        temp = classroom_search_ev.getText().toString();
        Pattern pattern = Pattern.compile(temp);
        if(adds == null){
            Toast t = Toast.makeText(Person_Classroom.this,"请先进行教学楼查询！",Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }else {
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();/*在数组中存放数据*/
            for (int i = 0; i < adds.length; i++) {
                Matcher matcher = pattern.matcher(adds[i][0]);
                boolean bool = matcher.find();
                if (bool) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("classroom", adds[i][0]);
                    map.put("num", adds[i][1]);
                    map.put("style", adds[i][5]+","+adds[i][4]);
                    map.put("1", classes[i][0]);
                    map.put("2", classes[i][1]);
                    map.put("3", classes[i][2]);
                    map.put("4", classes[i][3]);
                    map.put("t1", classes[i][4]);
                    map.put("5", classes[i][5]);
                    map.put("6", classes[i][6]);
                    map.put("7", classes[i][7]);
                    map.put("8", classes[i][8]);
                    map.put("t2", classes[i][9]);
                    map.put("9", classes[i][10]);
                    map.put("10", classes[i][11]);
                    map.put("11", classes[i][12]);
                    listItem.add(map);

                }
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(Person_Classroom.this, listItem, R.layout.person_classroom_list_items, new String[]
                    {"classroom", "num", "style", "1", "2", "3", "4", "t1", "5", "6", "7", "8", "t2", "9", "10", "11"}, new int[]
                    {R.id.classroom_item_class, R.id.classroom_item_num, R.id.classroom_item_style, R.id.classroom_item_tv1, R.id.classroom_item_tv2, R.id.classroom_item_tv3, R.id.classroom_item_tv4,
                            R.id.classroom_item_tvt1, R.id.classroom_item_tv5, R.id.classroom_item_tv6, R.id.classroom_item_tv7, R.id.classroom_item_tv8,
                            R.id.classroom_item_tvt2, R.id.classroom_item_tv9, R.id.classroom_item_tv10, R.id.classroom_item_tv11});
            person_classroom_lv.setAdapter(simpleAdapter);
        }
        }

    //利用Jsoup解析网页内容
    private String parsehtml(String result){
        String total="",results = "";
        int i = 0;
        String []score = new String[100];
        Document doc = Jsoup.parse(result);
        //System.out.println( doc.title());
        Elements link_class = doc.select("tr.infolist_common[style=\"display:\"],tr[align=\"center\"] > td");
        for(Element link:link_class){
            String name;
            name = link.tagName();
            score[i] = link.text();
            if(name.equals("td")){
                total += score[i] + "#";
            }
            else{
                total += "||"+score[i]+"||";
            }
           // System.out.println(score[i]);
        }
        total += "||";
        results = total;
        return results;
    }
    //排版函数
    private void StoreResult(String result){
        String []temp_one = result.split("\\|\\|");
        int s1=0,s2=0;
        String []temp_add = new String[temp_one.length/2];
        String []temp_class = new String[temp_one.length/2];
        for(int i=1;i<temp_one.length;i++){
            if((i%2)==0){
                temp_class[s1++] = temp_one[i];
                //System.out.println("1->"+temp_one[i]);
            }
            if((i%2)==1){
                temp_add[s2++] = temp_one[i];
               // System.out.println("2->"+temp_one[i]);
            }
        }
        String [][]temp_adds = new String[temp_add.length][];
        String [][]temp_classes = new String [temp_class.length][];
        for(int i=0;i<temp_add.length;i++){
            temp_adds[i] = temp_add[i].split(" ");
            temp_classes[i] = temp_class[i].split("#");
        }
            adds = new String[temp_add.length][18];
            classes = new String[temp_class.length][26];
            for(int i=0;i<temp_add.length;i++){
                for(int a=0;a<6;a++){
                    adds[i][a] = temp_adds[i][a];
                   // System.out.println("adds[i][a]="+adds[i][a]);
                }
            }
        for(int i=0;i<temp_class.length;i++){
            for(int a=13;a<26;a++){
                if(temp_classes[i][a].equals(" ")){
                    classes[i][a-13] = "空";
                }else{
                    classes[i][a-13] = "占";
                }
                //System.out.println("temp_classes[i][a]="+temp_classes[i][a]);
               // System.out.println("calsses[i][a]="+classes[i][a-13]);
            }
        }
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();/*在数组中存放数据*/
        for(int i=0;i<adds.length;i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("classroom",adds[i][0]);
            map.put("num",adds[i][1]);
            map.put("style",adds[i][5]+","+adds[i][4]);
            map.put("1",classes[i][0]);
            map.put("2",classes[i][1]);
            map.put("3",classes[i][2]);
            map.put("4",classes[i][3]);
            map.put("t1",classes[i][4]);
            map.put("5",classes[i][5]);
            map.put("6",classes[i][6]);
            map.put("7",classes[i][7]);
            map.put("8",classes[i][8]);
            map.put("t2",classes[i][9]);
            map.put("9",classes[i][10]);
            map.put("10",classes[i][11]);
            map.put("11",classes[i][12]);
            listItem.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(Person_Classroom.this,listItem,R.layout.person_classroom_list_items,new String[]
                {"classroom","num","style","1","2","3","4","t1","5","6","7","8","t2","9","10","11"},new int[]
                {R.id.classroom_item_class,R.id.classroom_item_num,R.id.classroom_item_style,R.id.classroom_item_tv1,R.id.classroom_item_tv2,R.id.classroom_item_tv3,R.id.classroom_item_tv4,
                        R.id.classroom_item_tvt1,R.id.classroom_item_tv5,R.id.classroom_item_tv6,R.id.classroom_item_tv7,R.id.classroom_item_tv8,
                        R.id.classroom_item_tvt2,R.id.classroom_item_tv9,R.id.classroom_item_tv10,R.id.classroom_item_tv11});
        person_classroom_lv.setAdapter(simpleAdapter);
    }
}
