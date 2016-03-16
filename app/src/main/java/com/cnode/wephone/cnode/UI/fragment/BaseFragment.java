package com.cnode.wephone.cnode.UI.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.cnode.wephone.cnode.UI.baseActivity;
import com.cnode.wephone.cnode.widget.ProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ASUS on 2016/3/15.
 * Fragment基类，如果子类注册了广播，则每个子类的类全名都是默认的广播action
 */
public class BaseFragment extends Fragment {
    protected baseActivity sActivity;
    private ProgressDialog progress;//进度条对话框

    @Override
    public void onAttach(Activity activity) {//连接  当fragment和activity关联之后，调用这个方法。
        super.onAttach(activity);
        sActivity=(baseActivity) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
    /**
     * 正在提交
     */
    public void onCommitting(){
        if (progress == null){
            progress = new ProgressDialog(sActivity);
        }
        progress.onCommitting();//这特么递归？ 不不不两个方法不一样 调用“正在显示对话框”
    }
    /**
     * 显示进度
     * @param resId 资源id
     */
    public void showProgress(int resId){
        if (progress == null){
            progress = new ProgressDialog(sActivity);
        }
        progress.setText(resId);
        progress.show();
    }
    /**
     * 隐藏进度
     */
    public void hideProgress(){
        progress.hide();
    }

    @Override
    public void onDetach() {//分离
        super.onDetach();
    }
}
