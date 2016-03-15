package com.cnode.wephone.cnode.Utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by ASUS on 2016/3/15.
 */
public class DoubleClickExitHelper {
    private final Activity mActivity;
    private boolean isOnKeyBacking;
    private Handler mHandler;
    private Toast mBackToast;
    public DoubleClickExitHelper(Activity activity) {//传一个activity进来监听这个activity的按钮点击？
        mActivity = activity;
        mHandler = new Handler(Looper.getMainLooper());//过后回顾下looper
        isOnKeyBacking = false;//测试过设置默认值为false依旧可以实现双击才退出
    }
    private Runnable onBackTimeRunnable = new Runnable() {

        @Override
        public void run() {
            isOnKeyBacking = false;//设置为false时表示没有按到返回键
            if(mBackToast != null){
                mBackToast.cancel();
            }
        }
    };
    public boolean onKeyDown(int keyCode, KeyEvent event){//key 按键  keydown--按下按钮
        if(keyCode != KeyEvent.KEYCODE_BACK) {//不是按了返回键直接返回false  只有是按了返回键才执行下列方法
            return false;
        }
        if(isOnKeyBacking) {//若已按了一次返回键 并又得到了一个返回的key事件回来 则要进行退出了
            mHandler.removeCallbacks(onBackTimeRunnable);//移除任何即将发布的Runnable 若三秒内按了则把postdelay的runnable移除掉 直接退出（觉得加和不加没必要 退出了一切都没了）
            if(mBackToast != null){
                mBackToast.cancel();
            }
            //退出程序
            ActivitySwitcher.popDefault(mActivity);
            return true;
        } else {
            isOnKeyBacking = true;//按了一次就将isOnKeyBacking设置为true 打印toast
            if(mBackToast == null) {
                mBackToast = Toast.makeText(mActivity, "再按一次退出！", Toast.LENGTH_LONG);
            }
            mBackToast.show();
            mHandler.postDelayed(onBackTimeRunnable, 3000);//三秒时间 应该三秒内再按一次就退出？ 三秒后isOnKeyBacking设置为false 取消toast
            return true;
        }
    }
}
