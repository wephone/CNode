package com.cnode.wephone.cnode.Utils.volley;

import java.util.Map;

/**
 * Created by ASUS on 2016/3/17.
 * 网络数据请求URL辅助类
 */
public class UrlHelper {
    public final static String HOST = "https://cnodejs.org";
    public final static String API = "/api/v1";
    public final static String TOPICS = HOST + API + "/topics";
    public final static String TOPIC = HOST + API + "/topic";
    public final static String USER = HOST + API + "/user";
    public final static String ACCESS_TOKEN = HOST + API + "/accesstoken";
    public final static String REPLY_SUFFIX = "/replies";

    //拼接url路径
    public static String resolve(String host, String path){
        StringBuilder builder = new StringBuilder(host);
        if (path.startsWith("/")&&host.endsWith("/")){
            path = path.substring(1);
        } else if (!path.startsWith("/")&&!host.endsWith("/")){
            builder.append("/");
        }
        builder.append(path);
        return builder.toString();
    }

    //拼接参数
    public static String resolve(String host, Map<String, Object> params){
        StringBuilder builder = new StringBuilder(host);
        if (!params.isEmpty()){
            builder.append("?");
            for (String key:params.keySet()){
                if (!builder.toString().endsWith("?")){
                    builder.append("&");
                }
                builder.append(key);
                builder.append("=");
                builder.append(params.get(key));
            }
        }
        return builder.toString();
    }

    /**
     * 拼接出主题列表url
     * @param params 参数
     */
    public static String getTopicsUrl(Map<String, Object> params){//params tab 15 page
        return resolve(TOPICS, params);
    }
}
