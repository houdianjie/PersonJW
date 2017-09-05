package com.youshi.personjw.uitl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.youshi.personjw.HomePage;
import com.youshi.personjw.R;
import com.zhy.m.permission.MPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dianjie on 2016/8/5.
 */
public class Webview_homepage extends Activity {
   private String Https_news;
   private WebView webview_news;
    private ProgressBar progressBar;
    private ImageButton img_btn;
    private MyHandler myHandler;
    private File downLoadFile = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_webview);
        init();
        Intent intent = Webview_homepage.this.getIntent();
        Bundle bundle = intent.getExtras();
        Https_news = bundle.getString("http");
        webview_op();

    }
    private void init(){
        webview_news = (WebView) findViewById(R.id.news_webview);
        progressBar = (ProgressBar) findViewById(R.id.web_pogressbar);
        Https_news = null;
        img_btn = (ImageButton) findViewById(R.id.news_img_btn);
        myHandler = new MyHandler();
    }
    private void webview_op(){
        WebSettings settings = webview_news.getSettings();
        settings.setJavaScriptEnabled(true);
       settings.setBuiltInZoomControls(true);
       settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(false);//不显示缩放按钮
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setAllowFileAccess(true);//设置可访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口s
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview_news.loadUrl(Https_news);
        webview_news.setWebChromeClient(new HLcc());
        webview_news.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webview_news.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                //Log.v("ldm", url);
                new DownLoadThread(url, Webview_homepage.this).start();
            }
        });
    }

    private  class HLcc extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, final int newProgress) {
            setProgress(newProgress * 100);
            //System.out.println(webview_news.getProgress());
            if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview_news.canGoBack()) {
            webview_news.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(Webview_homepage.this, HomePage.class);
            startActivity(intent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void news_return(View v){
        if(img_btn.isPressed()){
            img_btn.setImageDrawable(getResources().getDrawable(R.drawable.web_return_press));
        }
        Intent intent = new Intent(Webview_homepage.this, HomePage.class);
        startActivity(intent);
        this.finish();
    }
    //实现自定义下载工具操作异步线程类：
    public class DownLoadThread extends
            Thread {
        private String downLoadUrl,File_Format;
        private Context context;
        private FileOutputStream out = null;

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
                conn.setRequestProperty("Host","jw.qdu.edu.cn");
                conn.connect();
                in = conn.getInputStream();
                int length=0;
                int FULL_LENGTH = conn.getContentLength();
                //System.out.println("文件长度："+FULL_LENGTH);
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(context, "SD卡不可用！", Toast.LENGTH_SHORT).show();
                    return;
                }
                downLoadFile = Environment.getExternalStorageDirectory();
                File f = new File(downLoadFile+"/Person_QDU/");
                downLoadFile = f;
                if(!f.exists()){
                    f.mkdir();
                }
                String files="";
               files = GetFileName(conn);
                sdCardFile = new File(f, files);
                if(sdCardFile.exists()){
                    sdCardFile.delete();
                }
                    out = new FileOutputStream(sdCardFile);

                byte[] b = new byte[1024];
                int len;
                while ((len = in.read(b)) != -1) {
                    length += len;
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("NAME",sdCardFile);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == -100){
                Bundle bundle = msg.getData();
                File file = (File) bundle.get("NAME");
                Toast.makeText(Webview_homepage.this, "已下载至:"+downLoadFile.getPath(), Toast.LENGTH_SHORT).show();
                StartFileIntent(file);
            }
            super.handleMessage(msg);
        }
    }
    //自动打开文件
    public void StartFileIntent(File file){
       // Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type="+type);
        Intent intent = new Intent("android.intent.action.VIEW");
       // intent.addCategory("android.intent.category.DEFAULT");
       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.setDataAndType(uri, type);
        ////////此处判断安卓版本是否为7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(Webview_homepage.this, "com.youshi.personjw.fileprovider", file);
            intent.setDataAndType(contentUri, type);
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        Webview_homepage.this.startActivity(intent);
    }
    //获取文件类型
    private String getMIMEType(File f){
        String type="";
        String fName=f.getName();
      /* 取得扩展名 */
        String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
        if(end.equals("pdf")){
            type = "application/pdf";//
        }
        else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            type = "audio/*";
        }
        else if(end.equals("3gp")||end.equals("mp4")||end.equals("avi")||end.equals("wmv")||
                 end.equals("mkv")||end.equals("flv")||end.equals("rmvb")){
            type = "video/*";
        }
        else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            type = "image/*";
        }
        else if(end.equals("apk")){
        /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
     else if(end.equals("pptx")||end.equals("ppt")){
        type = "application/vnd.ms-powerpoint";
    }else if(end.equals("docx")||end.equals("doc")){
       type = "application/vnd.ms-word";
      }else if(end.equals("xlsx")||end.equals("xls")){
        type = "application/vnd.ms-excel";
     }
        else{
//        /*如果无法直接打开，就跳出软件列表给用户选择 */
            type="*/*";
        }
        return type;
    }
    private String GetFileName(HttpURLConnection  conn) throws UnsupportedEncodingException {
        boolean isok = false;
        String filename = "";
        Map<String,List<String>> lis_name= conn.getHeaderFields();
        //Set<String> key = lis_name.keySet();
        Set<Map.Entry<String, List<String>>> key = lis_name.entrySet();
        if (key == null) {
            return "NULL";
        }
        for(/*String*/Map.Entry<String, List<String>> ss:key){
            List<String> values = ss.getValue();
            for(String name:values){
                String result;
                try {
                    result = new String(name.getBytes("ISO-8859-1"),"UTF-8");
                    //result = new String(name);
                    int location = result.indexOf("filename");
                    if (location >= 0) {
                        result = result.substring(location + "filename".length());
                        filename = result.substring(result.indexOf("=") + 1);
                        //System.out.println("REsult===>"+filename);
                        isok = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }// ISO-8859-1 UTF-8 gb2312
                if(isok){
                    break;
                }
            }
        }
        String files = filename.substring(1,filename.length()-1);
        files ="中文会乱码" + files.length()+files.replace("?","");
        return files;
    }
}
