package com.youshi.personjw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youshi.personjw.uitl.Common_Values;
import com.youshi.personjw.uitl.GetHttp;
import com.youshi.personjw.uitl.PostHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.youshi.personjw.R.layout.login;


/**
 * Created by dianjie on 2016/7/18.
 */
public class Login extends Activity {
    private EditText et1,et2,et3;
    private String s1=null,s2=null,s3=null;
    private String cookieVal=null;
    private String Location=null;
   private ImageView image1,image_school_in;
    private URL myFileUrl = null;
   private  Bitmap bitmap = null;
    private PostHttp postHttp;
    private Login_MyHandler login_myHandler;
    private CheckBox cb_remember;
    private TextView textView;
    private WebView webView;
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        //Activity启动时自动获取cookie，并保存为全局变量
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                String s;
                s = StoreCookie();
                cookieVal = s;
            }
        }).start();*/
        GetSharedPerfer(this);
        String temp = null;
       temp = et2.getText().toString();
        if(!temp.equals("")){
            cb_remember.setChecked(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int group1 = 1;
        menu.add(group1,1,1,"关于");
        menu.add(group1,2,2,"退出");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case 2:
                System.exit(0);
                break;
            case 1:
                AlertDialog.Builder a = new AlertDialog.Builder(this);
                a.setTitle("关于").setMessage("青大掌上教务，可以为你带来诸多便利，省去了登教务网站的麻烦。").show();
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return  true;
    }

    //初始化各种变量
    private void init(){
        cb_remember = (CheckBox) this.findViewById(R.id.cb_remember);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        image1 = (ImageView) findViewById(R.id.img1);
        //image_school_in = (ImageView) findViewById(R.id.img1_school_in);
        postHttp = PostHttp.getPostHttp();
        login_myHandler = new Login_MyHandler();
        textView = (TextView) findViewById(R.id.sub_brow_tv);
        imageButton = (ImageButton) findViewById(R.id.sub_brow_img_click);
        webView = (WebView) findViewById(R.id.sub_brow_webview);
        webview_op();
    }
    //校外登录
    public void btn1_click(View v){
        s1 = et1.getText().toString();
        s2 = et2.getText().toString();
        s3 = et3.getText().toString();
        if(cb_remember.isChecked()){
            SharedPerfer(this,s1,s2);
        }
            if(s1.equals("")) {
                Toast t1 = Toast.makeText(Login.this, "用户名不能为空！", Toast.LENGTH_SHORT);
                t1.setGravity(Gravity.CENTER,0,0);
                t1.show();
            }
                else if(s2.equals("")) {
              Toast t2 =  Toast.makeText(Login.this, "密码不能为空！", Toast.LENGTH_SHORT);
                t2.setGravity(Gravity.CENTER,0,0);
                t2.show();
            }
                else if(s3.equals("")) {
               Toast t3 = Toast.makeText(Login.this, "验证码不能为空！", Toast.LENGTH_SHORT);
                t3.setGravity(Gravity.CENTER,0,0);
                t3.show();
            }
                else{
                new Thread(new Runnable() {
                    @Override
                public void run() {
                        String result = null,title = null;
                        String http = "http://"+Common_Values.SCHOOL_OUT_HOST+"/academic/j_acegi_security_check";
               result = postHttp.PostLogin(Common_Values.SCHOOL_OUT_HOST,http,s1,s2,s3,cookieVal);
                        Document doc = Jsoup.parse(result);
                        title = doc.title();
                        if(title.equals("综合教务管理系统")){
                            Message msg = new Message();
                            msg.what = 0;
                            login_myHandler.sendMessage(msg);
                            Intent intent = new Intent();
                            intent.setClass(Login.this,Main_Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("cookieVal",cookieVal);
                            bundle.putString("host",Common_Values.SCHOOL_OUT_HOST);
                            intent.putExtras(bundle);
                            Login.this.startActivity(intent);
                            finish();
                        }
                        else{
                            Elements elements = doc.select("div[id=\"error\"]");
                            String a = null;
                            a =  elements.text();
                            Message msg = new Message();
                            msg.what = -1;
                            Bundle  b = new Bundle();
                            b.putString("ERROR",a);
                            msg.setData(b);
                            login_myHandler.sendMessage(msg);
                        }


                    }
                }).start();
            }

        }

    public void login_cb(View v) {
        String password = "",username = "";
        password = et2.getText().toString();
        username = et1.getText().toString();
       if(cb_remember.isChecked()){
           SharedPerfer(this,username,password);
          // System.out.println("true");
       }
        else{
          // System.out.println("false");
       }
    }
    //从文件中读取密码 和用户名
    public void GetSharedPerfer(Context context){
        SharedPreferences preferences=context.getSharedPreferences("shared", Context.MODE_PRIVATE);
        String name=preferences.getString("name", "");
        String word=preferences.getString("word", "");
        et1.setText(name);
        et2.setText(word);
    }
    //运用轻量级存储器存储密码和用户名
    public void SharedPerfer(Context context, String name, String word) {
        SharedPreferences sp = context.getSharedPreferences("shared", context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("name", name);
        edit.putString("word", word);
        edit.commit();
    }
        class Login_MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                Toast t = Toast.makeText(Login.this,"登录成功！",Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
            }
            if(msg.what == -1){
                Bundle b = msg.getData();
                String temp = b.getString("ERROR");
                if(temp==null||temp == ""){
                    Toast t = Toast.makeText(Login.this,"内部服务器拒绝访问",Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();
                }else {
                    Toast t = Toast.makeText(Login.this, temp, Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            }
            if(msg.what == 100){
                String host = msg.getData().getString("host");
                //判断校内还是校外
                if(host.equals(Common_Values.SCHOOL_OUT_HOST))
                image1.setImageBitmap(bitmap);
                else
                image_school_in.setImageBitmap(bitmap);
            }
            super.handleMessage(msg);
        }
    }
    //点击刷新校外验证码
    public void btn2_click(View v){
        getBitmap(Common_Values.SCHOOL_OUT_HOST);
        if (bitmap == null) {
            image1.setImageResource(R.drawable.hint);
        }
    }
    //点击刷新校内验证码
    public void btn_school_in_click(View vIew){
        getBitmap(Common_Values.SCHOOL_IN_HOST);
        if (bitmap == null) {
            image_school_in.setImageResource(R.drawable.hint);
        }
    }

    //获得验证码图片
    public  void getBitmap(final String host){
        // image1.setImageBitmap(bitmap);
        new Thread(new Runnable() {
            String http;
            @Override
            public void run() {
                String s;
                s = StoreCookie(host);
                cookieVal = s;
                if(cookieVal==null){
                    cookieVal="";
                }
                http="http://"+host+"/academic/getCaptcha.do?"/*+ Math.random()*/;
                try {
                    String sss;
                    myFileUrl = new URL(http);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("Cookie",cookieVal);
                    //conn.addRequestProperty("Host","jw.qdu.edu.cn");
                    conn.addRequestProperty("Host",host);
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("host",host);
                    msg.what = 100;
                    msg.setData(bundle);
                    login_myHandler.sendMessage(msg);
                    if (conn.getResponseCode() == 200) {
                        //测试cookie是否一致
                        // System.out.println("getBitmap"+cookieVal);
                        /***就下面这一行代码我试了很多办法，到最后才发现放在这里才是合适的***/
                        //  System.out.println("获取图片成功！");
                    } else {
                        //  System.out.println("获取图片失败！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //获得cookie
    private String StoreCookie(String host) {
        String getCookie = null;
        try {
            String http = "http://"+ host+"/academic/common/security/login.jsp";
            //String http = "http://jw.qdu.edu.cn/homepage/index.do";
            URL url = new URL(http);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            urlConnection.setConnectTimeout(5000);
            //测试请求网站是否成功
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                getCookie = urlConnection.getHeaderField("Set-Cookie");
                Log.i("COOKIE获取成功!","OK!");
            } else {
                Log.i("COOKIE获取失败!","ERROR!");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        //Log.i("COOKIE",getCookie);
        return getCookie;
    }
    private void webview_op(){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(false);//不显示缩放按钮
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setAllowFileAccess(true);//设置可访问文件

        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webView.loadUrl("https://www.baidu.com/");

        //webView.setWebChromeClient(new HLcc());
    }
    //校内登录
    public void btn_school_in_login_click(View v){
        s1 = et1.getText().toString();
        s2 = et2.getText().toString();
        s3 = et3.getText().toString();
        if(cb_remember.isChecked()){
            SharedPerfer(this,s1,s2);
        }
        if(s1.equals("")) {
            Toast t1 = Toast.makeText(Login.this, "用户名不能为空！", Toast.LENGTH_SHORT);
            t1.setGravity(Gravity.CENTER,0,0);
            t1.show();
        }
        else if(s2.equals("")) {
            Toast t2 =  Toast.makeText(Login.this, "密码不能为空！", Toast.LENGTH_SHORT);
            t2.setGravity(Gravity.CENTER,0,0);
            t2.show();
        }
        else if(s3.equals("")) {
            Toast t3 = Toast.makeText(Login.this, "验证码不能为空！", Toast.LENGTH_SHORT);
            t3.setGravity(Gravity.CENTER,0,0);
            t3.show();
        }
        else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = null,title = null;
                    String http = "http://"+Common_Values.SCHOOL_IN_HOST+"/academic/j_acegi_security_check";
                   // String http = "http://10.16.164.72/academic/j_acegi_security_check";
                    result = postHttp.PostLogin(Common_Values.SCHOOL_IN_HOST,http,s1,s2,s3,cookieVal);
                    Document doc = Jsoup.parse(result);
                    title = doc.title();
                    if(title.equals("综合教务管理系统")){
                        Message msg = new Message();
                        msg.what = 0;
                        login_myHandler.sendMessage(msg);
                        Intent intent = new Intent();
                        intent.setClass(Login.this,Main_Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("cookieVal",cookieVal);
                        bundle.putString("host",Common_Values.SCHOOL_IN_HOST);
                        intent.putExtras(bundle);
                        Login.this.startActivity(intent);
                        finish();
                    }
                    else{
                        Elements elements = doc.select("div[id=\"error\"]");
                        String a = null;
                        a =  elements.text();
                        Message msg = new Message();
                        msg.what = -1;
                        Bundle  b = new Bundle();
                        b.putString("ERROR",a);
                        msg.setData(b);
                        login_myHandler.sendMessage(msg);
                    }


                }
            }).start();
        }

    }
    //显示浏览器
    public void btn3_click(View v){
        textView.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(imageButton.isPressed()) {
                    imageButton.setImageResource(R.drawable.web_return_press);
                }*/
                if(webView.canGoBack()){
                    webView.goBack(); //goBack()表示返回WebView的上一页面
                }

            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
               /* view.loadUrl(url);*/
                return true;
            }
        });
    }
    //显示关于
    public void btn4_click(View v){
       /* Toast t = Toast.makeText(Login.this,"登录按钮在左边！",Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();*/
        imageButton.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText(R.string.report_debug);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(Login.this,HomePage.class);
            this.startActivity(intent);
            webView.destroy();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}