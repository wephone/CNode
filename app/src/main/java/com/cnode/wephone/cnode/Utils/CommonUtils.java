package com.cnode.wephone.cnode.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.cnode.wephone.cnode.App;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ASUS on 2016/3/17.
 * 通用方法工具类
 */
public class CommonUtils {
    private final static Context APP_CONTEXT = App.getContext();//怎么感觉是得到了App类的实例对象  上下文有点不理解  App继承于activity，所以可以用Context APP_CONTEXT
    //有人说Context可以理解为调用所在的环境。容器 承上启下


    /**
     * 保存信息
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean saveStringToLocal(String key, String value) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(key, value);
        return editor.commit();//提交修改
    }

    /**
     * 保存信息
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean saveBooleanToLocal(String key, boolean value) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean(key, value);
        return editor.commit();//提交修改
    }

    /**
     * 保存信息
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean saveIntToLocal(String key, int value) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putInt(key, value);
        return editor.commit();//提交修改
    }


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
        //这里只是访问读取SharedPreferences而已 暂时没找到哪里写入SharedPreferences。。
    }

    /**
     * 读取信息
     *
     * @param key
     * @return
     */
    public static boolean getBooleanFromLocal(String key, boolean defValue) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
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
    /**
     * 反射获取类
     *
     * @param className
     * @return class
     */
    public static Class<?> getClassByString(String className) {
        Class<?> cls = null;
        try {
            cls = Class.forName(className);//根据名字获取这个类的类类型 好像名字是要完整的包名.类名
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }
    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();//度量
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 显示提示
     *
     * @param message
     */
    public static void showToast(String message) {
        Toast.makeText(APP_CONTEXT, message, Toast.LENGTH_LONG).show();
    }

    public static void showToast(int resId) {
        Toast.makeText(APP_CONTEXT, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 从sp转化到px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = APP_CONTEXT.getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取时间格式
     *
     * @param format
     * @param date
     * @return
     */
    public static String getTimeFormat(String format, Date date) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(date);
    }

    /**
     * 根据当前时间返回通俗时间值
     *
     * @param date
     * @return
     */
    public static String commonTime(Date date) {
        Calendar c = Calendar.getInstance();//calendar日历
        c.setTime(date);
        int y1 = c.get(Calendar.YEAR);
        int d1 = c.get(Calendar.DAY_OF_YEAR);
        long t1 = c.getTimeInMillis();//y d t1应该是该帖子发上去的时间 y d t2是当前最新时间
        c.setTime(new Date());
        int y2 = c.get(Calendar.YEAR);
        int d2 = c.get(Calendar.DAY_OF_YEAR);
        long t2 = c.getTimeInMillis();//据1970.1.1至今的毫秒数
        int yearGap = y2 - y1;
        int dayGap = d2 - d1; // 与现在时间相差天数
        long timeGap = (t2 - t1) / 1000;//与现在时间相差秒数
        String timeStr = "";
        if (yearGap == 0) {//当年
            if (dayGap == 0) {// 当天，直接显示时间
                if (timeGap > 60 * 60 * 4){// 4小时-24小时
                    timeStr = getTimeFormat("HH:mm", date);
                } else if (timeGap > 60 * 60) {// 1小时-4小时
                    timeStr = timeGap / (60 * 60) + "小时前";
                } else if (timeGap > 60) {// 1分钟-59分钟
                    timeStr = timeGap / 60 + "分钟前";
                } else {// 1秒钟-59秒钟
                    timeStr = "刚刚";
                }
            } else if (dayGap == 1) {// 昨天+时间
                timeStr = "昨天 " + getTimeFormat("HH:mm", date);
            } else if (dayGap == 2) {// 前天+时间
                timeStr = "前天 " + getTimeFormat("HH:mm", date);
            } else {// 大于3天，显示具体月日及时间
                timeStr = getTimeFormat("MM-dd HH:mm", date);
            }
        } else {//非当年现实完整的年月日及时间
            timeStr = getTimeFormat("yyyy-MM-dd", date);
        }
        return timeStr;
    }
}
