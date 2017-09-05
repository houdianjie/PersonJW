package com.youshi.personjw.uitl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by dianjie on 2016/7/20.
 */
public class PostHttp {
    private static PostHttp postHttp = new PostHttp();
    public PostHttp(){

    }
    public static PostHttp getPostHttp()
    {
        if(postHttp==null){
            postHttp = new PostHttp();
        }
        return postHttp;
    }
    public String PostLogin(final String host,final String http,final String s1, final String s2, final String s3,final String cookie) {
        String ss = null;
        String total = "";
        try {
            URL url = new URL(http);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String data =
                    "j_username=" + URLEncoder.encode(s1,"gb2312")
                            + "&j_password=" + URLEncoder.encode(s2,"gb2312")
                            + "&j_captcha=" + URLEncoder.encode(s3,"gb2312");
            // 设置请求的方式
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
            //   //System.out.println(String.valueOf(data.getBytes().length));
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
            urlConnection.setRequestProperty("Host",host);
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            urlConnection.setRequestMethod("POST");
            // 设置是否允许缓存，一般POST方法都不允许，防止出错
            urlConnection.setUseCaches(false);
            //测试cookie是否一致
            ////System.out.println("PostHttp:"+cookieVal);
            // 设置请求的头
            //setDoInput的默认值就是true
            //获取输出流
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes("gb2312"));
            os.flush();
            os.close();
            if (urlConnection.getResponseCode() == 200) {

                //输出结果。校验你是否操作成功
                // 获取响应的输入流对象
                InputStream isa = urlConnection.getInputStream();
                BufferedReader bufferedReaders = new BufferedReader(new InputStreamReader(isa,"gbk"));
                while ((ss = bufferedReaders.readLine()) != null) {
                    total += ss;
                }
                //System.out.println("连接教务网站成功！");
            } else {
                //System.out.println("连接教务网站失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    public String PostScore(final String host,final String http,final String s1, final String s2, final String s3,final String s4, final String cookie) {
        String ss = null;
        String total = "";
        try {
            URL url = new URL(http);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String data ="para="+URLEncoder.encode(s4,"gb2312")
                    + "&term=" + URLEncoder.encode(s1,"gb2312")
                            + "&year=" + URLEncoder.encode(s2,"gb2312")
                    +"&Submit=" + URLEncoder.encode(s3,"gb2312");
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置是否允许缓存，一般POST方法都不允许，防止出错
            urlConnection.setUseCaches(false);
            //测试cookie是否一致
            ////System.out.println("PostHttp:"+cookieVal);
            // 设置请求的头
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
           // //System.out.println(String.valueOf(data.getBytes().length));
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
            urlConnection.setRequestProperty("Host",host);
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //setDoInput的默认值就是true
            //获取输出流
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes("gb2312"));
            os.flush();
            os.close();
                    /*
                    //测试POST数据流
                    //System.out.println("数据流"+data.getBytes());
                    //测试返回头信息内容
                    for(int i=0;i<10;i++){
                    Location = urlConnection.getHeaderField(i);
                    //System.out.println("顺序 "+Location);
                    }
                    //测试返回头的名称
                    for(int i=0;i<10;i++){
                        Location = urlConnection.getHeaderFieldKey(i);
                        //System.out.println("KEY顺序 "+Location);
                    }
                    */
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                //输出结果。校验你是否操作成功
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                }
                //System.out.println("连接成绩网站成功！");
            } else {
                //System.out.println("连接成绩网站失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    public String PostPassword(final String host,final String http,final String s1, final String s2, final String s3,final String cookie) {
        String ss = null;
        String total = "";
        try {
            URL url = new URL(http);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String data =
                    "confirmedpasswd=" + URLEncoder.encode(s3,"gb2312")
                            + "&gotoUrl=" + URLEncoder.encode("null","gb2312")
                            + "&newpasswd=" + URLEncoder.encode(s2,"gb2312")
                    +"&oldpasswd="+URLEncoder.encode(s1,"gb2312");
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置是否允许缓存，一般POST方法都不允许，防止出错
            urlConnection.setUseCaches(false);
            //测试cookie是否一致
            ////System.out.println("PostHttp:"+cookieVal);
            // 设置请求的头
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
           // //System.out.println(String.valueOf(data.getBytes().length));
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
            urlConnection.setRequestProperty("Host",host);
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //setDoInput的默认值就是true
            //获取输出流
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes("gb2312"));
            os.flush();
            os.close();
                    /*
                    //测试POST数据流
                    //System.out.println("数据流"+data.getBytes());
                    //测试返回头信息内容
                    for(int i=0;i<10;i++){
                    Location = urlConnection.getHeaderField(i);
                    //System.out.println("顺序 "+Location);
                    }
                    //测试返回头的名称
                    for(int i=0;i<10;i++){
                        Location = urlConnection.getHeaderFieldKey(i);
                        //System.out.println("KEY顺序 "+Location);
                    }
                    */
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                //输出结果。校验你是否操作成功
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"gbk"));

                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                }
                //System.out.println("连接修改密码网站成功！");
            } else {
                //System.out.println("连接修改密码网站失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    public String PostClassRoom(final String host,final String http,final String aid, final String buildingid, final String room,final String week,final String whichweek,final String cookie) {
        String ss = null;
        String total = "";
        try {
            URL url = new URL(http);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String data =
                    "aid=" + URLEncoder.encode(aid,"gb2312")
                            + "&buildingid=" + URLEncoder.encode(buildingid,"gb2312")
                            + "&room=" + URLEncoder.encode(room,"gb2312")
                            +"&week="+URLEncoder.encode(week,"gb2312")
                    +"&whichweek="+URLEncoder.encode(whichweek,"gb2312");
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置是否允许缓存，一般POST方法都不允许，防止出错
            urlConnection.setUseCaches(false);
            //测试cookie是否一致
            ////System.out.println("PostHttp:"+cookieVal);
            // 设置请求的头
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
          //  //System.out.println(String.valueOf(data.getBytes().length));
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
            urlConnection.setRequestProperty("Host",host);
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //setDoInput的默认值就是true
            //获取输出流
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes("gb2312"));
            os.flush();
            os.close();
                    /*
                    //测试POST数据流
                    //System.out.println("数据流"+data.getBytes());
                    //测试返回头信息内容
                    for(int i=0;i<10;i++){
                    Location = urlConnection.getHeaderField(i);
                    //System.out.println("顺序 "+Location);
                    }
                    //测试返回头的名称
                    for(int i=0;i<10;i++){
                        Location = urlConnection.getHeaderFieldKey(i);
                        //System.out.println("KEY顺序 "+Location);
                    }
                    */
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                //输出结果。校验你是否操作成功
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"gbk"));

                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                }
                //System.out.println("连接教室查询网站成功！");
            } else {
                //System.out.println("连接教室查询网站失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    public String PostInfo_Maintain(final String http,final String s1, final String s2, final String s3,final String cookie) {
        String ss = null;
        String total = "";
        try {
            URL url = new URL(http);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String data =
                    "confirmedpasswd=" + URLEncoder.encode(s3,"gb2312")
                            + "&gotoUrl=" + URLEncoder.encode("null","gb2312")
                            + "&newpasswd=" + URLEncoder.encode(s2,"gb2312")
                            +"&oldpasswd="+URLEncoder.encode(s1,"gb2312");
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置是否允许缓存，一般POST方法都不允许，防止出错
            urlConnection.setUseCaches(false);
            //测试cookie是否一致
            ////System.out.println("PostHttp:"+cookieVal);
            // 设置请求的头
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
            // //System.out.println(String.valueOf(data.getBytes().length));
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
            urlConnection.setRequestProperty("Host","jw.qdu.edu.cn");
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //setDoInput的默认值就是true
            //获取输出流
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes("gb2312"));
            os.flush();
            os.close();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                //输出结果。校验你是否操作成功
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"gbk"));

                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                }
                //System.out.println("连接修改密码网站成功！");
            } else {
                //System.out.println("连接修改密码网站失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
