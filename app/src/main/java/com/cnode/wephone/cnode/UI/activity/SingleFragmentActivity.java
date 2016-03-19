package com.cnode.wephone.cnode.UI.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.cnode.wephone.cnode.R;
import com.cnode.wephone.cnode.Utils.SimpleFactory;
import com.cnode.wephone.cnode.Utils.constant.Params;

/**
 * Created by ASUS on 2016/3/19.
 * 单个fragment的activity
 */
public class SingleFragmentActivity extends BaseBackActivity {//动态加载fragment 通过传入bundle 获取fragment名字 再通过反射创建该fragment的实例对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getIntent().getExtras();//在activity swtich里用了跳转，将跳转到这个fragment的数据取出
            String fragmentName = bundle.getString(Params.FRAGMENT_NAME);
            Bundle arguments = bundle.getBundle(Params.ARGUMENTS);
            FrameLayout container = new FrameLayout(this);
            container.setId(R.id.wrap_content);
            setContentView(container);//设置一个wrapcontent的framelayout布局作为此activity的view
            //对于动态添加的fragment，必须为其制定一个container（所依附的布局）然后add
            getSupportFragmentManager().beginTransaction().add(R.id.wrap_content, SimpleFactory.createFragment(fragmentName, arguments)).commit();
        } catch (Exception e){
            e.printStackTrace();
            finish();
        }
    }

}
