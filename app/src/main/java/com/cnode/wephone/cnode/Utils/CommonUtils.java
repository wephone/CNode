package com.cnode.wephone.cnode.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.cnode.wephone.cnode.App;

/**
 * Created by ASUS on 2016/3/17.
 * 通用方法工具类
 */
public class CommonUtils {
    private final static Context APP_CONTEXT = App.getContext();//怎么感觉是得到了App类的实例对象  上下文有点不理解
    //有人说Context可以理解为调用所在的环境。容器 承上启下
    /**
     * 读取信息
     *
     * @param key
     * @return
     */
    public static String getStringFromLocal(String key) {//为何要用App来getSharedPreferences
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);//分享优先权 应该是之前学过的那个轻量级存储数据方法
        //通过SharedPreferences.edit()函数获得编辑对象，就可以写数据了
        //默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中，可以使用Activity.MODE_APPEND
        return sharedPreferences.getString(key, "");//SharedPreferences key-value存储  不懂getstring返回的是什么
        //getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
        //这里只是访问SharedPreferences
    }
    /**
     * 根据手机分辨率从dp转成px
     *
     * @param dpValue
     * @return
     * 2表示to
     */
    public static int dip2px(float dpValue) {
        final float scale = APP_CONTEXT.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 获取包信息，版本信息
     *
     * @return 包信息
     */
    public static PackageInfo getVersionInfo() {
        try {
            PackageManager pm = APP_CONTEXT.getPackageManager();
            return pm.getPackageInfo(APP_CONTEXT.getPackageName(), PackageManager.GET_CONFIGURATIONS);//configuration 构造
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}