package com.cnode.wephone.library_mylistview;

import android.view.View;
import android.widget.AdapterView;

/**
 * 列表条目单击事件辅助类
 * 屏蔽短时间内多次点击时间
 * Created by ASUS on 2016/3/17.
 */
public abstract class CompatOnItemClickListener implements AdapterView.OnItemClickListener {

    private final static long TIME_INTERVAL = 500;

    private long lastClickTime = 0;

    /**
     * 列表单击事件
     * @param view view
     * @param position 位置
     */
    public abstract void onItemClick(View view, int position);
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//应实现的方法本应是这个 onItemClick(View view, int position)方法是自己重编的
    //好像这里实现了onItemClick(AdapterView<?> parent, View view, int position, long id)，leftMenufragment那里的new CompatOnItemClickListener就不用再实现一次
        if (isFastDoubleClick()) return;
        onItemClick(view, position);
    }
    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < TIME_INTERVAL) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
