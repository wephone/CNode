package com.cnode.wephone.cnode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.cnode.wephone.cnode.UI.baseActivity;
import com.cnode.wephone.cnode.UI.fragment.LeftMenuFragment;
import com.cnode.wephone.cnode.Utils.DoubleClickExitHelper;
import com.cnode.wephone.cnode.Utils.constant.IntentAction;

public class MainActivity extends baseActivity {
    //按两次返回键退出
    private DoubleClickExitHelper doubleClickExitHelper;
    //抽屉布局类
    private DrawerLayout drawerLayout;
    //左侧菜单
    private LeftMenuFragment leftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doubleClickExitHelper = new DoubleClickExitHelper(this);
        initActionBarAndDrawer();
        initLayout();
        //所有控件加载完开始加载数据
        IntentFilter filter = new IntentFilter(IntentAction.LOGIN);//隐式意图 通过过滤器识别intent指向哪个组件
        filter.addAction(IntentAction.NEW_TOPIC);
        registerReceiver(myReceiver, filter);//寄存器
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {//抽屉有打开且存在式
                toggleDrawer();
                return true;
            }
            return doubleClickExitHelper.onKeyDown(keyCode, event);//让另一个类去处理这个逻辑
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initLayout(){
        //左侧菜单
        leftMenu = new LeftMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.left_menu, leftMenu).commit();
    }

    private void initActionBarAndDrawer(){//配置工具栏和抽屉
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {
            //作用：1.改变android.R.id.home返回图标。2.Drawer拉出、隐藏，带有android.R.id.home动画效果。3.监听Drawer拉出、隐藏；
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
            //当有action bar时，你就应该实现可以通过App icon来开启和关闭抽屉。而且这个app icon能够预示抽屉是否存在。
            //通过创建一个ActionBarDrawerToggle实现

            @Override
            public void onDrawerOpened(View view) {
                if (!TextUtils.isEmpty(App.getContext().access_token)) {//好像是取登录信息，回过头来细看 抛BUG显示这里错
                    leftMenu.setUserInfo();//若App.getContext().access_token 不为空则执行
                }
                invalidateOptionsMenu();//废止
            }

            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
        };
        drawerToggle.syncState();//应该是同步状态的意思 同步drawer指示器
        drawerLayout.setDrawerListener(drawerToggle);
    }
    //切换抽屉菜单 如果关了就开 开了就关
    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    //广播监听器
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {//通过传递intent来调用这个组件
            final String action = intent.getAction();
            if (action.equals(IntentAction.LOGIN)) {
                leftMenu.setUserInfo();
            } else if (action.equals(IntentAction.NEW_TOPIC)) {
//                pagerAdapter.getItem(0).refresh(true);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
