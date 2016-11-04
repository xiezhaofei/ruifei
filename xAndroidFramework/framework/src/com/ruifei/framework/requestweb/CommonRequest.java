package com.ruifei.framework.requestweb;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/3.
 */

public class CommonRequest {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    // 同步get请求
    private static Response baseSynchGet(String url,Map<String,String> params) throws IOException
    {
        Response response = null;
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();
        Request request = builder.build();
        Call call = new OkHttpClient().newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null)
            return response;
        else{
            throw new IOException();
        }
    }

    private static void baseAsychGet(String url,Map<String,String> params,Callback callback)
    {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();
        Request request = builder.build();
        Call call = new OkHttpClient().newCall(request);
        call.enqueue(callback);
    }


}
