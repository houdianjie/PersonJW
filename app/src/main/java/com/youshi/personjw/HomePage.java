package com.youshi.personjw;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.youshi.personjw.uitl.GetHttp;
import com.youshi.personjw.uitl.Webview_homepage;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dianjie on 2016/8/4.
 */
public class HomePage extends Activity {
    private GetHttp getHomepage;
    private Person_MyHandler myHandler;
    private String [][]store_parse;
    private String [][]final_result;
    private ListView homepage_lv;
    private String []release_time;
    private ProgressBar update_progress;
    private AlertDialog dialog = null;
    private ImageView update_img_code;
    private  TextView tv;
    private long mExitTime;
    private static final int REQUECT_CODE_SDCARD = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        init();

        new Thread(new Runnable() {
            @Override
            public void run() {

                /***********************************/
                //获取更新
                String update = "http://112.74.53.4/app/qdu_update.html";
                String temp2 = null;
                String title;
                temp2 = getHomepage.GetUpdate(update);
                Document doc = Jsoup.parse(temp2);
                title = doc.title();
               // System.out.println("title---->"+title);
                //向主线程发送带数据的消息
                Bundle bundle = new Bundle();
                bundle.putString("title",title);
                bundle.putString("content",temp2);
                Message msg2 = new Message();
                msg2.what = 20;
                msg2.setData(bundle);
                myHandler.sendMessage(msg2);
                /*******************************/
                String http = "http://jw.qdu.edu.cn/homepage/index.do";
                String temp1 = null;
                //在网络上获取课程表
                temp1 = getHomepage.GetHomePage(http);
                parsehtml(temp1);
                //向主线程发送带数据的消息
                Message msg1 = new Message();
                msg1.what = 100;
                myHandler.sendMessage(msg1);
            }
        }).start();

        //申请权限
        MPermissions.requestPermissions(HomePage.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
        click();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //调用注解函数
    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess()
    {
        Toast.makeText(this, "获取权限成功！", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed()
    {
        Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int group1 = 1;
        menu.add(group1,1,1,"关于");
        menu.add(group1,2,2,"退出");
        return true;
    }


    private void init(){
        getHomepage = GetHttp.getGetHttp();
        myHandler = new Person_MyHandler();
        store_parse = new String[100][2];
        final_result = new String[30][2];
        release_time = new String[30];
        mExitTime = 0;
        homepage_lv = (ListView) findViewById(R.id.homepage_lv);
        for(int i=0;i<release_time.length;i++){
            release_time[i] = null;
        }
        for(int i=0;i<store_parse.length;i++){
            for(int j=0;j<store_parse[i].length;j++){
                store_parse[i][j] = null;
            }
        }
        for(int i=0;i<final_result.length;i++){
            for(int j=0;j<final_result[i].length;j++){
                store_parse[i][j] = null;
            }
        }
        Toast toast = Toast.makeText(HomePage.this,"请稍后，正在获取最新教务通知...",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    public void homepage_click(View v){
        Intent intent = new Intent();
        intent.setClass(HomePage.this,Login.class);
        startActivity(intent);
        finish();
    }
    class Person_MyHandler extends Handler {
        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            // 此处可以更新UI
            if(msg.what == 100){
                int temp_num = -1;
                for(int i=0;i<store_parse.length;i++){
                   if(store_parse[i][0] != null){
                       String temp = "articleId";
                       Pattern pattern = Pattern.compile(temp);
                       Matcher matcher = pattern.matcher(store_parse[i][0]);
                       boolean bool = matcher.find();
                       if(bool){
                           //从解析数据中得到教务通知
                            final_result[++temp_num][0] = store_parse[i][0];
                            final_result[temp_num][1] = store_parse[i][1];
                       }
                   }
                }
                SetData();
            }
    if(msg.what == 20){
        if(dialog!=null){
            dialog.cancel();
            dialog=null;
        }
    Bundle b = msg.getData();
    String title = b.getString("title");
        String content = parse_update(b.getString("content"));
        //此处匹配版本号
        String Version = getVersion();
        Log.i("VERSION",Version+"--"+title);
        if(Version.equals("ERROR")){
            Toast.makeText(HomePage.this,"获取更新失败",Toast.LENGTH_SHORT).show();
            return;
        }
        Pattern pattern = Pattern.compile(Version);
        Matcher matcher = pattern.matcher(title);
        boolean bool = matcher.find();
        if(!bool&&(title!=""||title!=null)){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.udate_dialog,
                    (ViewGroup) findViewById(R.id.update_layout));
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
            builder.setView(layout);
            dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            Window window = dialog.getWindow();
             tv = (TextView) window.findViewById(R.id.update_tv_tip);
            tv.setText(content);
            final Button cancel = (Button) window.findViewById(R.id.Cancel_update);
            update_img_code = (ImageView) window.findViewById(R.id.update_img_code);
            update_progress = (ProgressBar) window.findViewById(R.id.update_progress);
            window.findViewById(R.id.update_OK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancel.setText("后台更新");
                    update_progress.setVisibility(View.VISIBLE);
                    Download_Update();
                }
            });
            window.findViewById(R.id.Cancel_update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    dialog = null;
                }
            });

        }else {
            //System.out.println("不需要更新。");
        }
    }
            if(msg.what == -100){
               File sdFile = new File(Environment.getExternalStorageDirectory()+"/Person_QDU/", "QDU_update.apk");
                if(sdFile.exists()) {
                    installAPK(sdFile);
                }else {
                    Toast t = Toast.makeText(HomePage.this,"更新失败，请扫描二维码下载。。",Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();
                }
            }
            super.handleMessage(msg);
        }
    }
    //利用Jsoup解析更新内容
    private String parse_update(String result){
        String total="";
        Document doc = Jsoup.parse(result);
        //System.out.println( "TITLE"+doc.title());
        Elements link_class = doc.select("body > p");

        for(Element link:link_class){
           total += link.text()+"\n";
        }
        return total;
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
    /**
     2  * 获取版本号
     3  * @return 当前应用的版本号
     4  */
    public String getVersion() {
       try {
               PackageManager manager = this.getPackageManager();
               PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
               String version = info.versionName;
                return version;
            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR";
            }
        }
    //解析网页，得到有关数据
    private void parsehtml(String result) {
        int i = -1, j = -1,k = 0;
        Document doc = Jsoup.parse(result);
        Elements links = doc.select("div > span");
        for(Element link:links){
            String time = link.text();
            if(++j % 3 == 0) {
                release_time[k++] = time;
            }
        }
        Elements link_class = doc.getElementsByTag("a");
        for(Element link:link_class){
            String attr = link.attr("href");
            String text = link.text();
            store_parse[++i][0] = attr;
            store_parse[i][1] = text;
        }
        i = -1;
    }

    //将教务通知显示到窗口
    private void SetData(){
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();/*在数组中存放数据*/
        for(int i=0;i<final_result.length;i++){
                if(final_result[i][0] != null){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("title",final_result[i][1]);
                    map.put("time",release_time[i+1]);
                    listItem.add(map);
                }
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(HomePage.this,listItem,R.layout.homepage_list_item,new String[] {"title","time"},
                new int[]{R.id.hp_item_tv,R.id.hp_item_tv2} );
        homepage_lv.setAdapter(simpleAdapter);
    }
    public void click(){
        homepage_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Toast t = Toast.makeText(HomePage.this,"时间:"+release_time[i+1],Toast.LENGTH_SHORT);
                t.show();*/
                Intent intent = new Intent();
                intent.setClass(HomePage.this, Webview_homepage.class);
                Bundle bundle = new Bundle();
                String s = "http://jw.qdu.edu.cn" + final_result[i][0];
                bundle.putString("http",s);
                intent.putExtras(bundle);
                startActivity(intent);
                HomePage.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    private void Download_Update(){
        String urlStr = "http://112.74.53.4/app/QDU_APP.apk";
        new DownLoadThread(urlStr, HomePage.this).start();

    }
    /**
     * 安装下载完成的APK
     * @param savedFile
     */
    private void installAPK(File savedFile) {
        //调用系统的安装方法
        Intent intent=new Intent();
        intent.setAction(intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(HomePage.this, "com.youshi.personjw.fileprovider", savedFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(savedFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
        finish();
    }
    //实现自定义下载工具操作异步线程类：
    public class DownLoadThread extends Thread {
        private String downLoadUrl;
        private Context context;
        private FileOutputStream out = null;
        private File downLoadFile = null;
        private File sdCardFile = null;
        private InputStream in = null;
        public DownLoadThread(String downLoadUrl, Context context) {
            super();
            this.downLoadUrl = downLoadUrl;
            this.context = context;
        }
        @Override
        public void run() {
            try {
                URL httpUrl = new URL(downLoadUrl);
                HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                conn.setDoInput(true);// 如果打算使用 URL 连接进行输入，则将 DoInput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 true。
               // conn.setDoOutput(true);// 如果下载文件，这个必须删除
                conn.setRequestMethod("GET");
                conn.setUseCaches(true);
                conn.setRequestProperty("Host","112.74.53.4");
                conn.connect();
                in = conn.getInputStream();
                int length=0;
                int FULL_LENGTH = conn.getContentLength();
                Log.i("文件长度：",FULL_LENGTH+"");
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(context, "SD卡不可用！", Toast.LENGTH_SHORT).show();
                    return;
                }
                downLoadFile = Environment.getExternalStorageDirectory();
                File f = new File(downLoadFile+"/Person_QDU/");
                if(!f.exists()){
                    f.mkdir();
                }
                sdCardFile = new File(f, "QDU_update.apk");
                if(sdCardFile.exists()){
                    sdCardFile.delete();
                }
                out = new FileOutputStream(sdCardFile);
                byte[] b = new byte[1024];
                int len;
                while ((len = in.read(b)) != -1) {
                    length += len;
                    float sa = 100*length/FULL_LENGTH;
                    update_progress.setProgress((int)sa);
                    out.write(b, 0, len);
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }

                Message message = new Message();
                message.what = -100;
                myHandler.sendMessage(message);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
