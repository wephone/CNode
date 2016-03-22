package com.cnode.wephone.cnode.UI.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnode.wephone.cnode.R;
import com.cnode.wephone.cnode.Utils.ActivitySwitcher;
import com.cnode.wephone.cnode.Utils.CommonUtils;
import com.cnode.wephone.cnode.Utils.constant.Params;
import com.cnode.wephone.cnode.Utils.volley.UrlHelper;
import com.cnode.wephone.cnode.adapter.TopicListAdapter;
import com.cnode.wephone.cnode.entity.Topic;
import com.cnode.wephone.cnode.entity.Topics;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.CommonListView;
import com.iwhys.mylistview.CompatOnItemClickListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2016/3/21.
 * 栏目列表
 * fragment的生命周期中只初始化控件
 * 所有数据都通过refresh方法加载（通过宿主activity控制，确保fragment初始化完成后再加载数据）
 */
public class TopicListFragment extends BaseFragment {
    private final static int PAGE_COUNT = 15;
    private String tab;
    private CommonListView<Topic> listView;

    /**
     * 刷新
     *
     * @param rightNow 是否立即刷新
     */
    public void refresh(boolean rightNow) {
        if (listView == null) return;
        listView.refresh(rightNow);//应该true的话就是立即刷新
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tab = getArguments().getString(Params.TAB);//tab 根据传进来的argument 决定tab的值
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = new CommonListView<Topic>(sActivity){

            @Override
            public BaseListAdapter<Topic> getAdapter(Context context) {
                return new TopicListAdapter(context);
                //这个是listview的适配器不是pager的适配器
            }

            @Override
            public void getDataFromServer(final int page) {//从服务器取数据
                Map<String, Object> params = new HashMap<>();
                params.put(Params.TAB, tab);//tab由mainactivity传入
                params.put(Params.LIMIT, PAGE_COUNT);//15 15个就重新加载
                params.put(Params.PAGE, page);//页码
                String url = UrlHelper.getTopicsUrl(params);//topic是单个主题，topics是主题列表
                //volley网络请求 StringRequest request response VolleyError
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {//response应该是json
                        if (TextUtils.isEmpty(response)) {
                            listView.onGetDataFailure(page);
                            return;
                        }
                        DBHelper.newInstance().save(tab, response);//保存标签和请求？
                        handleData(page, response, System.currentTimeMillis() / 1000);//传入当前刷新的时间
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.getMessage(error, sActivity);
                        listView.onGetDataFailure(page);//make page final
                    }
                });
                VolleyHelper.addToRequestQueue(request, tab);
            }

            @Override
            public void getDataFromLocal() {
                String[] data = DBHelper.newInstance().get(tab);
                if (data == null) return;
                handleData(1, data[1], Long.valueOf(data[0]));
            }
        };
        listView.setOnItemClickListener(new CompatOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Topic topic = (Topic) view.getTag(R.id.first_tag);//标签
                Bundle arguments = new Bundle();
                arguments.putString("id", topic.getId());
                arguments.putString("title", topic.getTitle());
                arguments.putString("author", topic.getAuthor().getLoginname());
                arguments.putString("content", topic.getContent());
                arguments.putString("create_at", CommonUtils.getTimeFormat("yyyy-MM-dd HH:mm", topic.getCreateAt()));
                arguments.putInt("reply_count", topic.getReply_count());
                Bundle bundle = new Bundle();
//                bundle.putString(Params.FRAGMENT_NAME, TopicDetailFragment.class.getSimpleName());
                bundle.putBundle(Params.ARGUMENTS, arguments);
//                ActivitySwitcher.pushDefault(sActivity, SingleInstanceActivity.class, bundle);
                //先做出布局点击事件暂时先不做
            }
        });
        return listView.getView();//封装好的listview布局
    }
    //处理获取到的数据
    private void handleData(int page, String response, long refreshTime) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        Topics topics = gson.fromJson(response, Topics.class);//topics可能是bean对象  topic(s)的tostring方法暂时不明确用法
        List<Topic> topicList = topics.getData();//把json封装成topics数据 再取出真正有用的data值
        //        List<Topic> topicList = (List<Topic>) gson.fromJson(response, Topic.class);  不行····为毛
        listView.onGetDataSuccess(page, topicList, refreshTime);//可能这里需要的是个list
    }
}
