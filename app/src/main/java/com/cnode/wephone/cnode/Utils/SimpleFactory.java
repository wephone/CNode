package com.cnode.wephone.cnode.Utils;

import android.os.Bundle;

import com.cnode.wephone.cnode.UI.fragment.BaseFragment;

/**
 * Created by ASUS on 2016/3/19.
 * 简单工厂方法
 */
public class SimpleFactory {

    /**
     * Fragment工厂，通过反射获取Fragment实例
     * @param fragmentName 类名称
     * directoryName 多级目录用.分隔，如"column.detail"
     * @param arguments 初始化时传入的参数
     * @return
     */

    public static BaseFragment createFragment(String fragmentName, Bundle arguments){
        return createFragment(fragmentName, null, arguments);
    }
    //创建一个fragment 动态加载 根据名字反射加载 更活用
    public static BaseFragment createFragment(String fragmentName, String directoryName, Bundle arguments){
        StringBuilder path = new StringBuilder();
        path.append(BaseFragment.class.getPackage().getName());
        path.append(".");//TopicListFragment path=com.iwhys.cnode.ui.fragment
        if (directoryName!=null&&directoryName.length()>0){
            //如果存在目录名，则把目录添加到路径中
            path.append(directoryName);
            path.append(".");
        }
        path.append(fragmentName);//com.iwhys.cnode.ui.fragment.CaptureFragment
        String className = path.toString();
        Class<?> cls = CommonUtils.getClassByString(className);
        if (cls!=null){
            BaseFragment fragment = null;
            try {
                fragment = (BaseFragment)cls.newInstance();//实例化CaptureFragment
                if (arguments!=null){
                    fragment.setArguments(arguments);//好像只有一个key pushFragment里好像有对argument赋值  好像未登录时argument是空的
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;//返回需要创建的fragment的实例对象
        }
        return null;
    }

}
