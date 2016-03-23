package com.cnode.wephone.cnode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
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
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.cnode.wephone.cnode.UI.baseActivity;
import com.cnode.wephone.cnode.UI.fragment.LeftMenuFragment;
import com.cnode.wephone.cnode.UI.fragment.TopicListFragment;
import com.cnode.wephone.cnode.Utils.ActivitySwitcher;
import com.cnode.wephone.cnode.Utils.CommonUtils;
import com.cnode.wephone.cnode.Utils.DoubleClickExitHelper;
import com.cnode.wephone.cnode.Utils.OauthHelper;
import com.cnode.wephone.cnode.Utils.SimpleFactory;
import com.cnode.wephone.cnode.Utils.constant.IntentAction;
import com.cnode.wephone.cnode.Utils.constant.Params;
import com.cnode.wephone.cnode.adapter.ColumnPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends baseActivity implements ViewPager.OnPageChangeListener {
    //按两次返回键退出
    private DoubleClickExitHelper doubleClickExitHelper;
    //抽屉布局类
    private DrawerLayout drawerLayout;
    //左侧菜单
    private LeftMenuFragment leftMenu;
    //主内容容器
    private ViewPager viewPager;
    //栏目标签
    private PagerSlidingTabStrip tabs;
    //页面适配器
    private ColumnPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doubleClickExitHelper = new DoubleClickExitHelper(this);
        initActionBarAndDrawer();
        initLayout();
//        所有控件加载完开始加载数据
        viewPager.post(new Runnable() {
            @Override
            public void run() {//runnable 放入UI线程使用
                refreshItem();
            }
        });
        IntentFilter filter = new IntentFilter(IntentAction.LOGIN);//隐式意图 通过过滤器识别intent指向哪个组件
        filter.addAction(IntentAction.NEW_TOPIC);
        registerReceiver(myReceiver, filter);//寄存器
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //这个onWindowFocusChanged指的是这个Activity得到或者失去焦点的时候 就会call。。
        //也就是说 如果你想要做一个Activity一加载完毕，就触发什么的话 完全可以用这个！！！
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {//此activity已被聚焦
            refreshItem();//应该是一进入就刷新的意思
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {//抽屉有打开且存在式 点了一次返回就先关了抽屉
                toggleDrawer();
                return true;
            }
            return doubleClickExitHelper.onKeyDown(keyCode, event);//让另一个类去处理这个逻辑
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);//设置右上角菜单按钮
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            if (OauthHelper.needLogin()) {//是否需要登录
                OauthHelper.showLogin(this);
            } else {
//                ActivitySwitcher.pushFragment(this, NewTopicFragment.class);
            }
        }
        return true;
    }

    @Override//动态设置菜单选项
    public boolean onPrepareOptionsMenu(Menu menu) {//每次点击menu键都会重新调用，所以，如果菜单需要更新的话，就用此方法
        menu.findItem(R.id.add).setVisible(!(drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)));//怎么这里有错还能跑？好像提示要改成GravityCompat
        return super.onPrepareOptionsMenu(menu);//弹出左侧菜单时，右上角菜单隐藏
    }

    //初始化布局内容
    private void initLayout(){
        //左侧菜单
        leftMenu = new LeftMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.left_menu, leftMenu).commit();//动态加载左侧菜单fragment
        String[] columnTabs = getResources().getStringArray(R.array.column_tab);//获得标签栏数组
        ArrayList<TopicListFragment> fragments = new ArrayList<>();
        for (int i = 0; i < columnTabs.length; i++) {//应该是通过bundle数据 改变viewpager fragment的显示  最终得出五个不同的fragment 整个fragments数组放入viewpager适配器中即可
            Bundle bundle = new Bundle();
            bundle.putString(Params.TAB, columnTabs[i]);
            TopicListFragment fragment = (TopicListFragment) SimpleFactory.createFragment(TopicListFragment.class.getSimpleName(), bundle);////bundle用于argument 传值通信
            //主内容fragment 不清楚怎样通过bundle做出5个的逻辑
            fragments.add(i, fragment);
        }
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ColumnPagerAdapter(getSupportFragmentManager(), fragments, getResources().getStringArray(R.array.column_title));//好像将数组fragments加入这里就可以达成，tab标签更改转换fragment[i]
        //回头看ColumnPagerAdapter构造方法，理清fragments{i}和tabs
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setTextSize(CommonUtils.sp2px(14));
        tabs.setTabBackground(android.R.color.transparent);
        tabs.setOnPageChangeListener(this);

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
                if (!TextUtils.isEmpty(App.getContext().access_token)) {//好像是取登录信息，应该这个方法是用于刚登陆后 改变Menu的
                    leftMenu.setUserInfo();//若App.getContext().access_token 不为空--已登陆则执行
                }
                invalidateOptionsMenu();//废止
                //当你要update你的menu时,必须调用invalidateOptionsMenu(),因为
                //action bar是一直出现的。然后系统将调用onPrepareOptionsMenu()更改menu
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

    //刷新页面
    private void refreshItem() {
        updateTabStatus();
        pagerAdapter.refreshItem(viewPager.getCurrentItem());
    }

    //更新标签状态
    private void updateTabStatus() {
        ViewGroup container = (ViewGroup) tabs.getChildAt(0);
        for (int i = 0; i < container.getChildCount(); i++) {
            TextView child = (TextView) container.getChildAt(i);
            child.setTextColor(getResources().getColor(i == viewPager.getCurrentItem() ? R.color.tab_selected : R.color.tab_normal));
            child.setTypeface(Typeface.defaultFromStyle(i == viewPager.getCurrentItem() ? Typeface.BOLD : Typeface.NORMAL), 0);//typeface--字体
        }
    }

    //广播监听器
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {//通过传递intent来调用这个组件
            final String action = intent.getAction();
            if (action.equals(IntentAction.LOGIN)) {//暂时理解为广播发出后，若已登陆则左侧菜单更改布局
                leftMenu.setUserInfo();
            } else if (action.equals(IntentAction.NEW_TOPIC)) {
                pagerAdapter.getItem(0).refresh(true);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        refreshItem();//换页就刷新
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
