package com.cnode.wephone.cnode;

import android.app.Application;

import com.cnode.wephone.cnode.Utils.CommonUtils;
import com.cnode.wephone.cnode.Utils.constant.Params;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ASUS on 2016/3/17.
 * 全局程序类
 * 记得声名 Manifest    <application
   android:name=".App"
   每次打开应用就会调用这些方法
 */
public class App extends Application {
    /**
     * 程序运行模式（调试、发布）
     */
    //public final static LogUtil.LaunchMode LAUNCH_MODE = LogUtil.LaunchMode.DEBUG;
    // 看不懂这是什么

    /**
     * 程序标签
     */
    public final static String TAG = "CNode";
    /**
     * 访问令牌，用来判断是否登录
     * 用户名
     * 头像地址
     */
    public String access_token;//全局令牌  也是单立模式
    // 应用程序上下文
    private static App instance;//单立模式  全局只有这一个instance 不能再被new context出来
    //context 理解为activity或者application

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        Fresco.initialize(instance);//这里解析过Fresco了
        access_token = CommonUtils.getStringFromLocal(Params.ACCESS_TOKEN);//先从sharepreference里取出 看看有没有上次的登陆信息 有的话 下次就不用登陆
        //禁止默认统计
        MobclickAgent.openActivityDurationTrack(false);//为毛这里禁止默认统计？
    }
    /**
     * 静态方法返回程序上下文
     * @return
     */
    public static synchronized App getContext() {//不知道什么时候才应该用同步修饰符
        return instance;
    }
}
