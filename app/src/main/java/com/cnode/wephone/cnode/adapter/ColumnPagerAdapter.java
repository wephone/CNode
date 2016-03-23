package com.cnode.wephone.cnode.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.cnode.wephone.cnode.UI.fragment.TopicListFragment;

import java.util.List;

/**
 * Created by ASUS on 2016/3/20.
 * viewpager适配器
 */
public class ColumnPagerAdapter extends FragmentPagerAdapter {

    private List<TopicListFragment> mFragments;
    private String[] columnTitles;

    public ColumnPagerAdapter(FragmentManager manager, List<TopicListFragment> fragments, String[] columnTitles) {
        super(manager);
        mFragments = fragments;
        this.columnTitles = columnTitles;//就是五个标签栏 all good share ask job 特么五个标签栏就不能叫统一名字tab么···
    }

    /**
     * 刷新页面
     * @param position 位置
     */
    public void refreshItem(int position){
        getItem(position).refresh(false);
    }//getitem就是得到topiclistfragment  调用那里面的刷新方法

    @Override
    public TopicListFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return columnTitles[position];//得到标题
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
