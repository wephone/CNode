package com.cnode.wephone.cnode.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import com.cnode.wephone.cnode.App;
import com.cnode.wephone.cnode.R;

import com.cnode.wephone.cnode.UI.activity.CaptureActivity;
import com.cnode.wephone.cnode.Utils.constant.Params;



/**
 * Created by ASUS on 2016/3/17.
 * 登陆  授权辅助类
 */
public class OauthHelper {
    /**
     * 是否需要登录
     * @return 登录状态
     */
    public static boolean needLogin(){
        return TextUtils.isEmpty(App.getContext().access_token);//access_token空的话返回true，未登录时access_token为空
    }
    /**
     * 显示登录对话框
     * @param context 上下文
     */
    public static void showLogin(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("用户登录")
                .setMessage("请在PC端登录cnodejs.org，并在设置页面找到授权二维码，点击确定按钮扫码完成登录。")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //设置空就直接退出--是的
                    }//Negative负 Neutral中性
                })
                .setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bd = new Bundle();
//                        bd.putString(Params.FRAGMENT_NAME, Capture Fragment.class.getSimpleName());//传入键值对 key：fragment名字-value：Capture Fragment
                        ActivitySwitcher.pushDefault(context, CaptureActivity.class, bd);
                    }
                });
        builder.show();
    }

    /**
     * 退出
     * 退出后将用户数据保存到sharepreferences里 保存登陆命令，头像url，用户名
     */
    public static void logout(){
        App.getContext().access_token = "";//退出时 把登陆令牌撤销 设置为空
        CommonUtils.saveStringToLocal(Params.ACCESS_TOKEN, "");
        CommonUtils.saveStringToLocal(Params.LOGIN_NAME, "");
        CommonUtils.saveStringToLocal(Params.AVATAR_URL, "");
    }
}
