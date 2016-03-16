package com.cnode.wephone.cnode.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by ASUS on 2016/3/16.
 * 对话框基类，去除默认样式，必须自定义布局
 * 显示之前对话框只重设宽高尺寸，位置、动画只在初始化设置一次，暴露是否可以取消、是否可以通过按返回键强制取消接口
 */
public abstract class BaseDialog extends Dialog implements DialogInterface.OnDismissListener {
    protected Context mContext;
    protected View mView;

    private Window mWindow;
    //是否允许强制退出
    private boolean cancelForce = true;

    public BaseDialog(Context context, int resourceId) {
        super(context, resourceId);
        mContext = context;
        mView = View.inflate(mContext, resourceId, null);
        mWindow = getWindow();//得到对话框当前所在的窗口
        init();
    }
    //初始化设置
    private  void init(){
        setContentView(mView);
        setCanceledOnTouchOutside(dialogCanceledOnTouchOutside());//传入方法，让设置变活，有需要改动的时候重写方法即可
        setCancelable(dialogCancelable());
        setOnDismissListener(this);
        mWindow.setWindowAnimations(dialogAnimation());//设置窗口动画，暂时不知这个动画用在哪里
        mWindow.setGravity(dialogGravity());//gravity应该是位置的意思
    }

    /**
     * 是否允许取消
     */
    protected boolean dialogCancelable(){
        return true;
    }
    /**
     * 是否允许点击dialog范围之外取消
     */
    protected boolean dialogCanceledOnTouchOutside(){
        return true;
    }
    /**
     * 子类需要修改动画，直接覆写该方法
     */
    protected int dialogAnimation() {
        return android.R.style.Animation_Dialog;//这个应该是默认的动画
    }
    /**
     * 子类需要修改位置，直接覆写该方法
     * 默认屏幕底部（应该是默认中心吧）
     */
    protected int dialogGravity(){
        return Gravity.CENTER;
    }
    public  void setCancelForce(boolean cancelForce){//基类本身默认可以强制退出，由子类重写
        this.cancelForce = cancelForce;
    }
    /**
     * 设置外观并显示
     * boolean android.view.View .post(Runnable action)
     * Causes the Runnable to be added to the message queue. The runnable will be run on the user interface thread.
     * Parameters:
     * action The Runnable that will be executed.
     * Returns:Returns true if the Runnable was successfully placed in to the message queue.
     * Returns false on failure, usually because the looper processing the message queue is exiting.
     */
    @Override
    public void show() {//The runnable will be run on the user interface thread.  runnable就是传递给UI线程！
        setDialogSize();
        //显示之前刷新view，防止view变形
        mView.post(new Runnable() {

            @Override
            public void run() {
                mView.postInvalidate();
            }
        });
        super.show();
    }
    /* 博客抄的，参考理解：Runnable是一个接口，不是一个线程，一般线程会实现Runnable。
     * 所以如果我们使用匿名内部类是运行在UI主线程的，如果我们使用实现这个Runnable接口的线程类，则是运行在对应线程的。
     * 具体来说，这个函数的工作原理如下：View.post(Runnable)方法。
     * 在post(Runnable action)方法里，View获得当前线程（即UI线程）的Handler，然后将action对象post到Handler里。
     * 在Handler里，它将传递过来的action对象包装成一个Message（Message的callback为action），然后将其投入UI线程的消息循环中。
     * 在Handler再次处理该Message时，有一条分支（未解释的那条）就是为它所设，直接调用runnable的run方法。
     * 而此时，已经路由到UI线程里，因此，我们可以毫无顾虑的来更新UI。
     */
    //设置对话框宽、高属性
    private void setDialogSize() {//设置好窗口属性后在show方法里使用
        WindowManager.LayoutParams wlp = mWindow.getAttributes();
        wlp.width = dialogWidth();
        wlp.height = dialogHeight();
        mWindow.setAttributes(wlp);//Attributes--属性
    }
    /**
     * 设置窗口布局宽度，默认适应填充数据之后的View
     * @return
     */
    protected int dialogWidth(){
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);//unspecified--不明确的 spec--规范
        return mView.getMeasuredWidth();//应该意思是根据测来的内容决定长度
    }

    /**
     * 设置dialog高度，默认适应填充数据之后的View
     */
    protected int dialogHeight() {
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return mView.getMeasuredHeight();
    }
    @Override
    public void hide() {
        if (isShowing()){
            this.dismiss();
        }
    }
    /**
     * 对话框消失之前执行
     */
    @Override
    public void onDismiss(DialogInterface dialog) {

    }
    /**
     * 获取字符串
     * @param resId
     * @return
     */
    protected String getString(int resId) {//get谁的属性？
        return mContext.getResources().getString(resId);
    }
    /*
     * 拦截返回键，触发时调用hide方法隐藏dialog
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
            //event.getAction()!=KeyEvent.ACTION_UP 不响应抬起动作，可防止执行两次
            if (cancelForce){//如果当前可以强制退出
                hide();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
