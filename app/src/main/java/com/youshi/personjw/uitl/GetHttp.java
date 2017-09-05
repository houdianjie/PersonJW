package com.youshi.personjw.uitl;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetHttp {
    private static GetHttp getHttp = new GetHttp();
    public  GetHttp(){

    }
    public static GetHttp getGetHttp()
    {
        if(getHttp==null){
            getHttp = new GetHttp();
        }
        return getHttp;
    }

    public  String GetCourse(final String host,final String courhttp,final String  cookie){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Host",host);
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            // urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
         //   urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"gbk"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                   // //System.out.println(ss);
                }
                //System.out.println("获取课表成功!");
            } else {
                //System.out.println("获取课表失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
}
    public  String GetExamPlan(final String host,final String courhttp,final String  cookie){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Host",host);
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            // urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
          //  urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                     ////System.out.println("sss----->"+ss);
                }
                //System.out.println("获取考试安排成功!");
            } else {
                //System.out.println("获取考试安排失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
    }
    public  String GetInfo(final String host,final String courhttp,final String  cookie){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Host",host);
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            // urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
           // urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                    // //System.out.println(ss);
                }
                //System.out.println("获取个人信息成功!");
            } else {
                //System.out.println("获取个人信息失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
    }
    public  String GetTeachPlanHttp(final String host,final String courhttp,final String  cookie){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Host",host);
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            // urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
          //  urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"gbk"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                    // //System.out.println(ss);
                }
                //System.out.println("获取教学计划成功!");
            } else {
                //System.out.println("获取教学计划失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
    }

    public  String GetTeachPlan(final String host,final String courhttp,final String  cookie){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Host",host);
            urlConnection.setInstanceFollowRedirects(true);
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            // urlConnection.setConnectTimeout(5000);
           // urlConnection.setRequestProperty("Connection", "keep-alive");
         //   urlConnection.setRequestProperty("Content-Type",
         //           "application/x-www-form-urlencoded");
         //   urlConnection.setRequestProperty("User-Agent",
         //           "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
          //  urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
           // Log.i("RESPOSE",""+urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"utf-8"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                    // //System.out.println(ss);
                }
                //System.out.println("获取教学计划成功!");
            } else {
                //System.out.println("获取教学计划失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
    }

    public  String GetHomePage(final String courhttp){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Host","jw.qdu.edu.cn");
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            // urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
          //  urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                    // //System.out.println(ss);
                }
                //System.out.println("获取首页成功!");
            } else {
                //System.out.println("获取首页失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
    }
    public  String Get_Person_SP(final String host,final String courhttp,final String cookie){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Host",host);
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            // urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
            //  urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                }
                //System.out.println("获取修读进程成功!");
            } else {
                //System.out.println("获取修读进程失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
    }
    public  String GetUpdate(final String courhttp/*,final String cookie*/){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            //urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Accept-Charset",
                    "utf-8");
            //urlConnection.setRequestProperty("Host","112.74.53.4");
            // 设置请求的超时时间
           // urlConnection.setReadTimeout(5000);
             urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Connection", "keep-alive");
           // urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
           // Log.i("RESPONSE",urlConnection.getResponseCode()+"");
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                   // System.out.println(ss);
                }
                //System.out.println("获取更新成功!");
            } else {
                //System.out.println("获取更新失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
    }
    public  String GetInfo_Maintain(final String host,final String courhttp,final String cookie){
        String ss = null;
        String total = "";
        try {
            URL url = new URL(courhttp);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Cookie",cookie);
            urlConnection.setRequestProperty("Accept-Charset",
                    "utf-8");

            urlConnection.setRequestProperty("Host",host);
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //测试请求网站是否成功
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                //输出结果。校验你是否操作成功
                while ((ss = bufferedReader.readLine()) != null) {
                    total += ss;
                   // System.out.println(ss);
                }
                //System.out.println("获取更新成功!");
            } else {
                //System.out.println("获取更新失败！");
            }
        } catch (Exception e) {
            //System.out.println("出错误！");
            e.printStackTrace();
        }
        return total;
    }
}

