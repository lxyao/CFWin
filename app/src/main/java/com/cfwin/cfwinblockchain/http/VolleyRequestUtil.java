package com.cfwin.cfwinblockchain.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.cfwin.base.App;
import com.cfwin.cfwinblockchain.MyApplication;
import com.cfwin.cfwinblockchain.MyApplicationKt;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class VolleyRequestUtil {

    public static StringRequest stringRequest;
    public static Context context;
    public static int sTimeOut = 30000;

    /*
     * 获取GET请求内容
     * 参数：
     * context：当前上下文；
     * url：请求的url地址；
     * tag：当前请求的标签；
     * volleyListenerInterface：VolleyListenerInterface接口；
     * timeOutDefaultFlg：是否使用Volley默认连接超时；
     * */
    public static void RequestGet(Context context, String url, String tag,
                                  VolleyListenerInterface volleyListenerInterface,
                                  boolean timeOutDefaultFlg) {
        // 清除请求队列中的tag标记请求
//        MyApplication.getHttpQueue().cancelAll(tag);
        // 创建当前的请求，获取字符串内容
        stringRequest = new StringRequest(Request.Method.GET, url,
                volleyListenerInterface.responseListener(), volleyListenerInterface.errorListener());
        // 为当前请求添加标记
        stringRequest.setTag(tag);
        // 默认超时时间以及重连次数
        int myTimeOut = timeOutDefaultFlg ? DefaultRetryPolicy.DEFAULT_TIMEOUT_MS : sTimeOut;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(myTimeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将当前请求添加到请求队列中
        MyApplicationKt.getQueue().add(stringRequest);
    }

    /*
     * 获取POST请求内容（请求的代码为Map）
     * 参数：
     * context：当前上下文；
     * url：请求的url地址；
     * tag：当前请求的标签；
     * params：POST请求内容；
     * volleyListenerInterface：VolleyListenerInterface接口；
     * timeOutDefaultFlg：是否使用Volley默认连接超时；
     * */
    public static void RequestPost(Context context, String url, String tag,
                                   final Map params,
                                   VolleyListenerInterface volleyListenerInterface,
                                   boolean timeOutDefaultFlg) {
        // 清除请求队列中的tag标记请求
//        MyApplication.getHttpQueue().cancelAll(tag);
        // 创建当前的POST请求，并将请求内容写入Map中
        stringRequest = new StringRequest(Request.Method.POST, url,
                volleyListenerInterface.responseListener(), volleyListenerInterface.errorListener()) {
            @Override
            protected Map getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public String getBodyContentType() {
                return super.getBodyContentType();
            }
        };
        // 为当前请求添加标记
        stringRequest.setTag(tag);
        // 默认超时时间以及重连次数
        int myTimeOut = timeOutDefaultFlg ? DefaultRetryPolicy.DEFAULT_TIMEOUT_MS : sTimeOut;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(myTimeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将当前请求添加到请求队列中
        MyApplicationKt.getQueue().add(stringRequest);
    }

}