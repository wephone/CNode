package com.cnode.wephone.cnode.Utils;

import android.app.Activity;
import android.content.Context;

/**
 * Created by ASUS on 2016/3/15.
 */
public class ActivitySwitcher {
    //退出程序
    public static void popDefault(Context context) {
        ((Activity) context).finish();
    }
}
