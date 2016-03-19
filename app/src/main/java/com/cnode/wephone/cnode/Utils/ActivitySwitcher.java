package com.cnode.wephone.cnode.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ASUS on 2016/3/15.
 * Activity切换动画
 */
public class ActivitySwitcher {
    //退出程序
    public static void popDefault(Context context) {
        ((Activity) context).finish();
    }
    //点击登录对话框，准备跳转到扫面二维码时，调用的方法  应该是activity跳转
    public static void pushDefault(Context from, Class<?> to, Bundle bundle) {
        Intent intent = new Intent(from, to);
        if (bundle != null) {
            intent.putExtras(bundle);//extra额外的  通过intent传递数据应该
        }
        ((Activity) from).startActivity(intent);
    }

    public static void pushDefault(Context from, Class<?> to, Bundle bundle, int enterAnim, int exitAnim) {
        pushDefault(from, to, bundle);
        ((Activity) from).overridePendingTransition(enterAnim, exitAnim);
    }
}
