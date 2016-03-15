package com.cnode.wephone.cnode.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by ASUS on 2016/3/15.
 */
public class baseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //必须调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法，
        //才能够保证获取正确的新增用户、活跃用户、启动次数、使用时长等基本数据
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //屏蔽menu键
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
