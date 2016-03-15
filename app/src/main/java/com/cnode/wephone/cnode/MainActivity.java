package com.cnode.wephone.cnode;

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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {//抽屉有打开且存在式
//                toggleDrawer();
                return true;
            }
            return doubleClickExitHelper.onKeyDown(keyCode, event);//让另一个类去处理这个逻辑
        }
        return super.onKeyDown(keyCode, event);
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
                if (!TextUtils.isEmpty(App.getContext().access_token)) {
                    leftMenu.setUserInfo();
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
        };
        drawerToggle.syncState();//应该是同步状态的意思 同步drawer指示器
        drawerLayout.setDrawerListener(drawerToggle);
    }
}
