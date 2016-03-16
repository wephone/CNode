package com.cnode.wephone.cnode.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.cnode.wephone.cnode.R;

/**
 * Created by ASUS on 2016/3/15.
 * 提示进度对话框
 */
public class ProgressDialog extends BaseDialog {
    private View mLoadingIcon;
    private TextView mloadingText;
    private Animation loadingAnim;

    public ProgressDialog(Context context){
        super(context, R.layout.dialog_progress);//super指向父类 此处调用的是basedialog的构造方法
        initWidget();
    }
    private void initWidget(){//配置组件
        mLoadingIcon = mView.findViewById(R.id.loading_icon);//抽象父类中的参数 mView = View.inflate(mContext, resourceId, null);
        //mLoadingIcon指定为dialog_progress里的view小图标
        mloadingText = (TextView) mView.findViewById(R.id.loading_text);
        loadingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_loading);
        setCancelForce(false);//设置为不可强退的
    }
    /**
     * 快捷设置loading状态为正在提交，并显示
     */
    public void onCommitting(){
        setText(R.string.on_committing);//&#8230--省略号。。。
        this.show();
    }

    /**
     * 快捷设置loading状态为正在载入，并显示
     */
    public void onLoading(){
        setText(R.string.on_loading);
        this.show();
    }

    /**
     * 设置载入过程显示的提示文本
     * @param text
     */
    public void setText(String text){
        if(!TextUtils.isEmpty(text)){
            mloadingText.setText(text);
        }
    }

    /**
     * 设置载入过程显示的提示文本
     * @param resid
     */
    public void setText(int resid){
        String text = mContext.getResources().getString(resid);
        setText(text);
    }

    @Override
    public void show() {
        if("".equals(mloadingText.getText())){//若未设置文字，则隐藏文字控件，mView背景设置为透明
            mloadingText.setVisibility(View.GONE);
            mView.setBackgroundResource(Color.TRANSPARENT);
        }else{
            mloadingText.setVisibility(View.VISIBLE);
            mView.setBackgroundResource(R.drawable.dialog_loading_bg);//设为灰色
        }
        Animation a = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.50f, Animation.RELATIVE_TO_SELF, 0.50f);
        //设置回滚动画，详细参数暂时不懂
        a.setDuration(1000);
        a.setFillAfter(true);
        a.setInterpolator(new LinearInterpolator());
        a.setRepeatCount(Animation.INFINITE);//a动画有些不懂
        mLoadingIcon.startAnimation(loadingAnim);//开始加载loading动画
        super.show();
    }
    //加载进度的对话框不可取消
    @Override
    protected boolean dialogCancelable() {
        return false;
    }

    @Override
    protected boolean dialogCanceledOnTouchOutside() {
        return false;
    }
}
