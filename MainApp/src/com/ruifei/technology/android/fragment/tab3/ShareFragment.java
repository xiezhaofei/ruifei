package com.ruifei.technology.android.fragment.tab3;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ruifei.technology.android.R;
import com.ruifei.framework.fragment.BaseFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/24.
 */
public class ShareFragment extends BaseFragment {

    private Button button;
    private TextView text;


    @Override
    public int getContainerLayoutId() {
        return R.layout.share_fragment_lay;
    }

    @Override
    public void initUi() {
        button = (Button)findViewById(R.id.button2);
        text = (TextView)findViewById(R.id.textView3);
        super.initUi();
    }

    @Override
    public void loadData() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Request.Builder builder = new Request.Builder()
                                .url("http://192.168.1.104:8080/pxzx/log.do")
                                .get();
                        Request request = builder.build();
                        Call call = new OkHttpClient().newCall(request);
                        // 同步
                        try {
                            Response response = call.execute();
                            String string = response.body().string();
                            Log.i("xzf","string:"+string);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpURLConnection connection = null;
//                        try {
//                            URL url = new URL("http://192.168.1.104:8080/pxzx/log.do");
//                            connection = (HttpURLConnection) url.openConnection();
//                            // 设置请求方法，默认是GET
//                            connection.setRequestMethod("GET");
//                            // 设置字符集
//                            connection.setRequestProperty("Charset", "UTF-8");
//                            // 设置文件类型
//                            connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//                            // 设置请求参数，可通过Servlet的getHeader()获取
//                            connection.setRequestProperty("Cookie", "AppName=" + URLEncoder.encode("你好", "UTF-8"));
//                            // 设置自定义参数
//                            connection.setRequestProperty("MyProperty", "this is me!");
//
//                            if(connection.getResponseCode() == 200){
//                                InputStreamReader in = new InputStreamReader(connection.getInputStream());
//                                BufferedReader buffer = new BufferedReader(in);
//                                String inputLine = null;
//                                StringBuffer resultData = new StringBuffer();
//                                while (((inputLine = buffer.readLine()) != null)){
//                                    resultData.append(inputLine);
//                                }
//                                System.out.println(resultData.toString());
//                                Log.i("xzf","successful=====");
//                                Log.i("xzf",""+resultData.toString());
//                                in.close();
//                            }
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } finally {
//                            if(connection != null){
//                                connection.disconnect();
//                            }
//                        }
//                    }
//                }).start();
            }
        });
        super.loadData();
    }
}
