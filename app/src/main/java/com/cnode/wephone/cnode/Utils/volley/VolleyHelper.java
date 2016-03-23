package com.cnode.wephone.cnode.Utils.volley;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.cnode.wephone.cnode.App;

/**
 * Created by ASUS on 2016/3/23.
 * volley请求辅助类
 */
public class VolleyHelper {
    private static final String TAG = "Volley";
    //网络请求队列
    private static RequestQueue requestQueue;

    /**
     * 获取网络请求队列
     * @return 请求队列
     */
    public static RequestQueue getRequestQueue(){
        if (requestQueue==null){
            synchronized (App.class){
                requestQueue = Volley.newRequestQueue(App.getContext());
            }
        }
        return requestQueue;//基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了
    }

    /**
     * 添加请求到队列
     * @param request 请求
     * @param tag 标签
     * @param <T> 类型
     */
    public static <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        //对一个request的重新请求策略的设置，不同的项目是否需要重新请求，重新请求几次，请求超时的时间，这些就在这设置到里面。
        VolleyLog.d("添加请求%s到队列:%s", tag, request.getUrl());
        getRequestQueue().add(request);
    }

    public static <T> void addToRequestQueue(Request<T> request) {
        addToRequestQueue(request, null);
    }

    /**
     * 取消带有特定标签的网络请求
     * @param tag 标签
     */
    public static void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
