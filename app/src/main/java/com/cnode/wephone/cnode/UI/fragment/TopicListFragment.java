package com.cnode.wephone.cnode.UI.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnode.wephone.cnode.R;
import com.cnode.wephone.cnode.Utils.ActivitySwitcher;
import com.cnode.wephone.cnode.Utils.CommonUtils;
import com.cnode.wephone.cnode.Utils.constant.Params;
import com.cnode.wephone.cnode.adapter.TopicListAdapter;
import com.cnode.wephone.cnode.entity.Topic;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.CommonListView;
import com.iwhys.mylistview.CompatOnItemClickListener;

import java.util.HashMap;
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
            tab = getArguments().getString(Params.TAB);//tab
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = new CommonListView<Topic>(sActivity){

            @Override
            public BaseListAdapter<Topic> getAdapter(Context context) {
                return new TopicListAdapter(context);;
            }

            @Override
            public void getDataFromServer(int page) {
                Map<String, Object> params = new HashMap<>();
                params.put(Params.TAB, tab);
                params.put(Params.LIMIT, PAGE_COUNT);
                params.put(Params.PAGE, page);
                String url = UrlHelper.getTopicsUrl(params);
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (TextUtils.isEmpty(response)) {
                            listView.onGetDataFailure(page);
                            return;
                        }
                        DBHelper.newInstance().save(tab, response);
                        handleData(page, response, System.currentTimeMillis() / 1000);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.getMessage(error, sActivity);
                        listView.onGetDataFailure(page);
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
                Topic topic = (Topic) view.getTag(R.id.first_tag);
                Bundle arguments = new Bundle();
                arguments.putString("id", topic.getId());
                arguments.putString("title", topic.getTitle());
                arguments.putString("author", topic.getAuthor().getLoginname());
                arguments.putString("content", topic.getContent());
                arguments.putString("create_at", CommonUtils.getTimeFormat("yyyy-MM-dd HH:mm", topic.getCreateAt()));
                arguments.putInt("reply_count", topic.getReply_count());
                Bundle bundle = new Bundle();
                bundle.putString(Params.FRAGMENT_NAME, TopicDetailFragment.class.getSimpleName());
                bundle.putBundle(Params.ARGUMENTS, arguments);
                ActivitySwitcher.pushDefault(sActivity, SingleInstanceActivity.class, bundle);
            }
        });
        return listView.getView();
    }
}
