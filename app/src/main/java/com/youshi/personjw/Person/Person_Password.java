package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.youshi.personjw.R;
import com.youshi.personjw.uitl.PostHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by dianjie on 2016/7/25.
 */
public class Person_Password extends Activity {
    private String COOK,HOST,ev1,ev2,ev3;
    private EditText password_ev1,password_ev2,password_ev3;
    private PostHttp postHttp;
    private String GetPostResu;
    private Person_MyHandler myHandler;
    private TextView passsword_tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_password);
        init();
        Intent intent = Person_Password.this.getIntent();
        Bundle bundle = intent.getExtras();
        COOK = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
    }
    private void init(){
        password_ev1 = (EditText) findViewById(R.id.person_password_ev1);
        password_ev2 = (EditText) findViewById(R.id.person_password_ev2);
        password_ev3 = (EditText) findViewById(R.id.person_password_ev3);
        passsword_tv1 = (TextView) findViewById(R.id.person_password_tv1);
        postHttp = PostHttp.getPostHttp();
        myHandler = new Person_MyHandler();
        COOK = null;
        GetPostResu = null;
    }
    public void person_password_click(View v){
        ev1  = password_ev1.getText().toString();
        ev2  = password_ev2.getText().toString();
        ev3  = password_ev3.getText().toString();
        if(ev1.equals(ev2)){
            Toast t = Toast.makeText(Person_Password.this,"原密码不能与新密码相同！",Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }
        else if(!ev2.equals(ev3)){
            Toast t = Toast.makeText(Person_Password.this,"新密码不一致！",Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }
        else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String http = "http://"+HOST+"/academic/sysmgr/modifypasswd_user.jsdo";
                    String temp1 = null,temp2 = null;
                    //在网络上获取课程表
                    temp1 = postHttp.PostPassword(HOST,http,ev1,ev2,ev3,COOK);
                    Document doc = Jsoup.parse(temp1);
                    temp2 = doc.title();
                    //向主线程发送带数据的消息
                    Message msg1 = new Message();
                    msg1.what = 100;
                    Bundle b = new Bundle();
                    b.putString("course_temp",temp2);
                    msg1.setData(b);
                    myHandler.sendMessage(msg1);

                }
            }).start();
            //执行新线程修改密码
        }
    }
    class Person_MyHandler extends Handler {
        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            // 此处可以更新UI
            if(msg.what == 100){
                Bundle b = msg.getData();
                String result = b.getString("course_temp");
                if(result.equals("提示信息")){
                    Toast t = Toast.makeText(Person_Password.this,"原密码不正确，修改失败！",Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();
                }else{
                    Toast t = Toast.makeText(Person_Password.this,"修改成功！请重新登录。",Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();
                    passsword_tv1.setText("重要提示:\n请将修改后的密码记在纸上，以防出错！");
                    passsword_tv1.setTextColor(getResources().getColor(R.color.password_tv1_tip));
                }
            }
            super.handleMessage(msg);
        }
    }
    private String parsehtml(String result) {
        String total="",results = "";
        int i = 0;
        String []cla = new String[100];
        Document doc = Jsoup.parse(result);
        //System.out.println( doc.title());
        Elements link_class = doc.select("a[target=\"_blank\"].infolist,table[width=\"100%\"]");
        // Elements link_class = doc.select("td.center > a");
        for(Element link:link_class){
            String name;
            name = link.tagName();
            cla[i] = link.text();
            if(name.equals("a")){
                total += cla[i]+"#";
            } else if (name.equals("a") ) {
                total += cla[i]+" ";
            } else {
                total += "#"+cla[i];
                results += total + "||";
                // System.out.println(total);
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
    }
}
