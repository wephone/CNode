package com.cnode.wephone.cnode.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnode.wephone.cnode.R;
import com.cnode.wephone.cnode.Utils.CommonUtils;
import com.cnode.wephone.cnode.Utils.volley.UrlHelper;
import com.cnode.wephone.cnode.entity.Topic;
import com.cnode.wephone.cnode.entity.Topics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.ViewHolder;

/**
 * Created by ASUS on 2016/3/21.
 * 主题列表适配器
 */
public class TopicListAdapter extends BaseListAdapter<Topic> {

    private final static int ODD = 0, EVEN = 1;//奇数偶数
    private Context context;

    public TopicListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {//得到intemview的类型 奇数项还是偶数项
        return position % 2 == 0 ? EVEN : ODD;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //正常填充
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_topic, viewGroup, false);
        }
        final Topic topic = (Topic) getItem(position);
        view.setBackgroundResource(getItemViewType(position) == EVEN ? R.drawable.item_topic_even_bg : R.drawable.item_topic_odd_bg);
        //每一行就换一种颜色
        ViewHolder viewHolder = ViewHolder.get(view);
        TextView author = viewHolder.getView(R.id.author);
        TextView last_reply_at = viewHolder.getView(R.id.last_reply_at);
        TextView title = viewHolder.getView(R.id.title);
        SimpleDraweeView avatar = viewHolder.getView(R.id.avatar);
        TextView replyCount = viewHolder.getView(R.id.reply_count);
        TextView visitCount = viewHolder.getView(R.id.visit_count);
        author.setText(topic.getAuthor().getLoginname());
        if (topic.isTop()){//哪里可以给top和good赋值 来判断是不是投top？  settop没有被使用
            appendIcon(author, R.drawable.icon_top);//估计fragment里的handledata方法将json解析后，top等属性就已经被赋值了
        }
        if (topic.isGood()){
            appendIcon(author, R.drawable.icon_good);
        }
        last_reply_at.setText(CommonUtils.commonTime(topic.getLastReplyAt()));
        title.setText(topic.getTitle());
        String avatarUrl = topic.getAuthor().getAvatar_url();
        avatar.setImageURI(Uri.parse(UrlHelper.resolve(UrlHelper.HOST, avatarUrl)), context);
        replyCount.setText(topic.getReply_count() + "");
        visitCount.setText(topic.getVisit_count() + "");
        view.setTag(R.id.first_tag, topic);
        return view;
    }

    //插入图表到TextView 好像是覆盖了一部分textview
    private void appendIcon(TextView textView, int iconId){
        SpannableString string = new SpannableString(" ");//用drawable覆盖掉两个空格
        ImageSpan imageSpan = new ImageSpan(context, iconId);
        string.setSpan(imageSpan, string.length() - 1, string.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(string);//哦 好像append的是string其实append是drawerable string已被替换
//        textView.setCompoundDrawables(drawable, null, null, null);//compound 复合  为何你不用这句???
    }

}
