package com.cnode.wephone.cnode.UI.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cnode.wephone.cnode.R;
import com.cnode.wephone.cnode.Utils.CommonUtils;
import com.cnode.wephone.cnode.Utils.OauthHelper;
import com.cnode.wephone.cnode.Utils.constant.Params;
import com.cnode.wephone.cnode.Utils.volley.UrlHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iwhys.mylistview.CompatOnItemClickListener;

/**
 * Created by ASUS on 2016/3/15.
 *左侧抽屉菜单栏布局，由于还要登陆，所以不可以写死
 */
public class LeftMenuFragment extends BaseFragment implements View.OnClickListener{
    private SimpleDraweeView avatar;//Facebook开发的图片加载库  暂时先跳过XML文件，有空学习下fresco相关文档
//    private ImageView avatar;
    private TextView loginname;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//        Fresco.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_left_menu1, container, false);//App类里使用过了 Fresco.initialize（context）方法
//        view.isInEditMode();
        view.findViewById(R.id.user_info).setOnClickListener(this);
        avatar = (SimpleDraweeView) view.findViewById(R.id.avatar);
//        avatar = (ImageView) view.findViewById(R.id.avatar);
        loginname = (TextView) view.findViewById(R.id.loginname);
        setUserInfo();
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        //配置数组适配器  simple_list_item_1应该是自带默认item布局  这里我换了一个自己写的
        listView.setAdapter(new ArrayAdapter<String>(sActivity, R.layout.simple_list_item, getResources().getStringArray(R.array.left_menu_title)) {

            //菜单图标 array--以array开头声名的XML文件，例如<attr name="only_animate_new_items" format="boolean" />
            //item不是应该textview+imageview吗 怎么只有textview  好像用setCompoundDrawables可以把textview和imageview复合
            private final TypedArray icons = getResources().obtainTypedArray(R.array.left_menu_icon);//类型数组

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {//应该是设置simple_list_item_1内容
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.white_btn_color));
                //white_btn_color <selector> 应该是设置了不同状态不同颜色
                textView.setBackgroundResource(R.drawable.item_left_menu_bg);
                final Drawable drawable = icons.getDrawable(position);//返回drawable对象（图像，不是抽屉）
                assert drawable != null;
                drawable.setBounds(0, 0, 85, 85);//界限 可能因为源码设置的simple_list_item_1应该是自带默认item布局 适配不一样，所以我设的值不一样，否则不好看
                textView.setCompoundDrawables(drawable, null, null, null);//compound 复合
                textView.setCompoundDrawablePadding(CommonUtils.dip2px(8));
                textView.setHeight(CommonUtils.dip2px(50));//设置高度50px
                return textView;
            }
        });
        listView.setOnItemClickListener(new CompatOnItemClickListener() {
            //若点到了listview
        // setOnItemClickListener是adapterview里的，所以CompatOnItemClickListener要实现adapterview接口
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
//                        ActivitySwitcher.pushFragment(sActivity, SettingFragment.class);
                        break;
                    case 1:
//                        ShareUtils.commonShare(sActivity, getString(R.string.share_content));
                        break;
                    case 2:
//                        ActivitySwitcher.pushFragment(sActivity, AboutFragment.class);
                        break;
                    default:
                        break;
                }
            }
        });
        TextView version = (TextView) view.findViewById(R.id.version);
        version.setText(getString(R.string.version) + CommonUtils.getVersionInfo().versionName);//&#160 好像是空格  packageinfo包含了版本信息
        return view;
    }

    //1.assert true/false;当为false时，抛出错误，结束程序
    //2.assert true/false : "error info";当为false时，抛出错误，结束程序，并输出提示信息"error info"
    //返回imageView这个用于显示图片的控件里的图片，getDrawable(position)返回的是Drawable类型。

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_info) {//如果点到了点击登录那一栏
            if (!OauthHelper.needLogin()){//如果不需要登录  将数据传入bundle
                Bundle bundle = new Bundle();//若key不存在则返回缺省值
                bundle.putString(Params.LOGIN_NAME, loginname.getText().toString());//key--value
                bundle.putString(Params.AVATAR_URL, CommonUtils.getStringFromLocal(Params.AVATAR_URL));
//                ActivitySwitcher.pushFragment(sActivity, UserInfoFragment.class, bundle);
            } else {
                OauthHelper.showLogin(sActivity);
            }
        }
    }
    /**
     * 设置用户信息
     */
    public void setUserInfo(){
        String name = CommonUtils.getStringFromLocal(Params.LOGIN_NAME);//懂了，这里取值，Utils力有值就取，没值的话isempty（）返回 true--直接getstring到“点击登录”
        String avatar_url = CommonUtils.getStringFromLocal(Params.AVATAR_URL);
        avatar.setImageURI(Uri.parse(UrlHelper.resolve(UrlHelper.HOST, avatar_url)), sActivity);
        loginname.setText(TextUtils.isEmpty(name) ? getString(R.string.login) : name);//name为空的话 设置为“点击登录”
    }
}
