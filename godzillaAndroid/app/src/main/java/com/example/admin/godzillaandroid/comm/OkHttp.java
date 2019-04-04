package com.example.admin.godzillaandroid.comm;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2019/4/2.
 */
public class OkHttp {

    private Call call;

    public OkHttp(){

    }

    public OkHttp(String url,String value){
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
            //字符串
            //1.创建OkHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //2.通过RequestBody.create 创建requestBody对象
            RequestBody requestBody =RequestBody.create(mediaType, value);
            //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
            Request request = new Request.Builder().url(url).post(requestBody).build();
            //4.创建一个call对象,参数就是Request请求对象
            Call calls = okHttpClient.newCall(request);
            call = calls;
    }

    public Call getCall(){
        return call;
    }
    /**
     * okHttp get同步请求
     */
    public String getDatasync(String Url){
        String result = "";
        try {
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url(Url)//请求接口。如果需要传参拼接到接口后面。
                    .build();//创建Request 对象
            Response response = null;
            response = client.newCall(request).execute();//得到Response 对象
            if (response.isSuccessful()) {
//                System.out.println("response.code()=="+response.code());
//                System.out.println("response.message()=="+response.message());
//                System.out.println("res=="+response.body().string());
                //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                result = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
